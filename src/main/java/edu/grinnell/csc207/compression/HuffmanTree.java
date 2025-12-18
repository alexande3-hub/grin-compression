package edu.grinnell.csc207.compression;


import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * A HuffmanTree derives a space-efficient coding of a collection of byte
 * values.
 *
 * The huffman tree encodes values in the range 0--255 which would normally
 * take 8 bits.  However, we also need to encode a special EOF character to
 * denote the end of a .grin file.  Thus, we need 9 bits to store each
 * byte value.  This is fine for file writing (modulo the need to write in
 * byte chunks to the file), but Java does not have a 9-bit data type.
 * Instead, we use the next larger primitive integral type, short, to store
 * our byte values.
 */
public class HuffmanTree {
    private static class Node implements Comparable<Node>{
        Short ch;
        int value;
        Node left;
        Node right;
        Boolean down;
        public Node(int value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.ch = null;
            this.down = false;
        }
        Map.Entry<Short, Integer> val2;
        public Node(Map.Entry<Short, Integer> val2) {
            this.val2 = val2;
            this.ch = val2.getKey();
            this.left = null;
            this.right = null;
            this.value = val2.getValue();
            this.down = true;
        }
        public Node(Short ch) {
            this.ch = ch;
            this.left = null;
            this.right = null;
        }
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.ch = null;
        }
        @Override
        public int compareTo(Node n) {
            if (down == true) {
                if (this.val2 == null) {
                    return 1;
                } else if (n.val2 == null) {
                    return -1;
                } else if (this.value < n.value){
                    return -1;
                } else if(this.value > n.value){
                    return 1;
                } else {
                    return 0;
                }
            } else {
                if (this.value < n.value){
                    return -1;
                } else if(this.value > n.value){
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    private Node first;

    /**
     * Constructs a new HuffmanTree from a frequency map.
     * @param freqs a map from 9-bit values to frequencies.
     */
    public HuffmanTree(Map<Short, Integer> freqs) {
        freqs.put((short) 256, 1);
        Set<Map.Entry<Short, Integer>> set = freqs.entrySet();
        PriorityQueue<Node> priFreq = new PriorityQueue<Node>();
        for (Map.Entry<Short, Integer> pair : set) {
            Node node = new Node(pair);
            priFreq.add(node);
        }
        while (priFreq.size() > 1) {
            Node p1 = priFreq.poll();
            Node p2 = priFreq.poll();
            int v1 = p1.value;
            int v2 = p2.value;
            if (v1 == 0) {
                v1 = p1.val2.getValue();
            }
            if (v2 == 0) {
                v2 = p2.val2.getValue();
            }
            Node node = new Node((v1 + v2), p1, p2);
            priFreq.add(node);
            this.first = node;
        }
    }

    /**
     * Constructs a new HuffmanTree from the given file.
     * @param in the input file (as a BitInputStream)
     */
    public HuffmanTree(BitInputStream in) {
        int bit = in.readBit();
        if (bit == 0) {
            this.first = new Node((short) in.readBits(9));
        } else {
            HuffmanTree t1 = new HuffmanTree(in);
            HuffmanTree t2 = new HuffmanTree(in);
            this.first = new Node(t1.first, t2.first);
        }
    }

    /**
     * Writes this HuffmanTree to the given file as a stream of bits in a
     * serialized format.
     * @param out the output file as a BitOutputStream
     * @param node the node we detect for any values (writes 0 for leaf, 1 for interior node).
     */
    public void serial(BitOutputStream out, Node node) {
        if (node.left == null && node.right == null) {
            out.writeBit(0);
            out.writeBits((int) node.ch, 9);
        } else {
            out.writeBit(1);
            serial(out, node.left);
            serial(out, node.right);
        }
    }

    /**
     * Writes this HuffmanTree to the given file as a stream of bits in a
     * serialized format.
     * @param out the output file as a BitOutputStream
     */
    public void serialize(BitOutputStream out) {
        serial(out, this.first);
    }

    /**
     * Adds short-string pairings based on the path to get to each short in the tree.
     * @param map the starting map (empty).
     * @param node the current node we are looking at.
     * @param s the string we manipulate to pair unique binary strings with each short
     * in the map.
     * @return the completed map with short-string pairings.
     */
    public Map<Short, String> codeMap(Map<Short, String> map, Node node, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.ch, s);
        } else {
            codeMap(map, node.left, s + "0");
            codeMap(map, node.right, s + "1");
        }
        return map;
    }
   
    /**
     * Encodes the file given as a stream of bits into a compressed format
     * using this Huffman tree. The encoded values are written, bit-by-bit
     * to the given BitOuputStream.
     * @param in the file to compress.
     * @param out the file to write the compressed output to.
     */
    public void encode(BitInputStream in, BitOutputStream out) {
        Map<Short, String> map = codeMap(new HashMap<Short, String>(), this.first, "");
        int bits = in.readBits(8);
        while (bits != -1) {
            String sh = map.get((short) bits);
            for (int i = 0; i < sh.length(); i++) {
                out.writeBit(Integer.parseInt(String.valueOf(sh.charAt(i))));
            }
            bits = in.readBits(8);
        }
        String sh = map.get((short) 256);
        for (int i = 0; i < sh.length(); i++) {
            out.writeBit(Integer.parseInt(String.valueOf(sh.charAt(i))));
        }
    }

    /**
     * Finds a value based on the binary pattern in 'in' (left if 0, right if 1).
     * @param in the input stream we read bits from.
     * @param node the current node we look at to find a value.
     * @return the value found from what we read from 'in'.
     */
    public Short findVal(BitInputStream in, Node node) {
        if (node.ch != null) {
            if (node.ch == 256) {
                return 256;
            } else {
                return node.ch;
            }
        } else {
            if (in.readBit() == 1) {
                return findVal(in, node.right);
            } else {
                return findVal(in, node.left);
            }
        }
    }

    /**
     * Decodes a stream of huffman codes from a file given as a stream of
     * bits into their uncompressed form, saving the results to the given
     * output stream. Note that the EOF character is not written to out
     * because it is not a valid 8-bit chunk (it is 9 bits).
     * @param in the file to decompress.
     * @param out the file to write the decompressed output to.
     */
    public void decode(BitInputStream in, BitOutputStream out) {
        while (true) {
            Short outVal = findVal(in, this.first);
            if (outVal != 256) {
                out.writeBits(outVal, 8);
            } else {
                break;
            }
        }
    }
}
