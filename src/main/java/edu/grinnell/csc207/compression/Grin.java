package edu.grinnell.csc207.compression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The driver for the Grin compression program.
 */
public class Grin {
    /**
     * Decodes the .grin file denoted by infile and writes the output to the
     * .grin file denoted by outfile.
     * @param infile the file to decode
     * @param outfile the file to ouptut to
     * @throws IOException 
     */
    public static void decode(String infile, String outfile) throws IOException {
        BitInputStream bin = new BitInputStream(infile);
        BitOutputStream bout = new BitOutputStream(outfile);
        bin.readBits(32);
        HuffmanTree tree = new HuffmanTree(bin);
        tree.decode(bin, bout);
        bout.close();
        bin.close();
    }

    /**
     * Creates a mapping from 8-bit sequences to number-of-occurrences of
     * those sequences in the given file. To do this, read the file using a
     * BitInputStream, consuming 8 bits at a time.
     * @param file the file to read
     * @return a freqency map for the given file
     * @throws IOException 
     */
    public static Map<Short, Integer> createFrequencyMap(String file) throws IOException {
        BitInputStream b = new BitInputStream(file);
        Map<Short, Integer> map = new HashMap<Short, Integer>();
        while (true) {
            int bits = b.readBits(8);
            if (bits != -1) {
                if (map.containsKey((short) bits)) {
                    map.replace((short) bits, map.get((short) bits), map.get((short) bits) + 1);
                } else {
                    map.put((short) bits, 1);
                }
            } else {
                break;
            }
        }
        b.close();
        return map;
    }

    /**
     * Encodes the given file denoted by infile and writes the output to the
     * .grin file denoted by outfile.
     * @param infile the file to encode.
     * @param outfile the file to write the output to.
     * @throws IOException 
     */
    public static void encode(String infile, String outfile) throws IOException {
        Map<Short, Integer> map = createFrequencyMap(infile);
        BitInputStream bin = new BitInputStream(infile);
        BitOutputStream bout = new BitOutputStream(outfile);
        HuffmanTree tree = new HuffmanTree(map);
        bout.writeBits(1846, 32);
        tree.serialize(bout);
        tree.encode(bin, bout);
        bout.close();
        bin.close();
    }

    /**
     * The entry point to the program.
     * @param args the command-line arguments.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        if (args[0].equals("encode")) {
            encode(args[1], args[2]);
        } else if (args[0].equals("decode")) {
            decode(args[1], args[2]);
        } else {
            System.out.println("First argument must be 'encode' or 'decode'.");
        }
    }
}
