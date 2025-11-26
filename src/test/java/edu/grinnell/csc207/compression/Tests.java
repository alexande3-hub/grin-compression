package edu.grinnell.csc207.compression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class Tests {
    /**
     * Tests huffman-example.txt equality with decode.
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        Grin.decode("../../../../../../../files/huffman-example.grin", 
        "../../../../../../../files/huffman-example2.txt");
        assertEquals("../../../../../../../files/huffman-example.txt", 
            "../../../../../../../files/huffman-example2.txt"
        );
    }

    /**
     * Tests wikipedia-huffman-coding.txt equality with decode.
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        Grin.decode("../../../../../../../files/wikipedia-huffman-coding.grin", 
        "../../../../../../../files/wikipedia-huffman-coding2.txt");
        assertEquals("../../../../../../../files/wikipedia-huffman-coding.txt", 
            "../../../../../../../files/wikipedia-huffman-coding2.txt"
        );
    }

    /**
     * Tests pg2600.txt equality with decode.
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        Grin.decode("../../../../../../../files/pg2600.grin", 
        "../../../../../../../files/pg2600.txt");
        assertEquals("../../../../../../../files/pg2600.txt", 
            "../../../../../../../files/pg2600.txt"
        );
    }
}
