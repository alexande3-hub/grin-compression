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
        Grin.decode("/home/alexande3/CSC207/grin-compression/files/huffman-example.grin", 
        "/home/alexande3/CSC207/grin-compression/files/huffman-example2.txt");
        assertEquals("/home/alexande3/CSC207/grin-compression/files/huffman-example.txt", 
            "/home/alexande3/CSC207/grin-compression/files/huffman-example2.txt"
        );
    }

    /**
     * Tests wikipedia-huffman-coding.txt equality with decode.
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        Grin.decode("/home/alexande3/CSC207/grin-compression/files/wikipedia-huffman-coding.grin", 
        "/home/alexande3/CSC207/grin-compression/files/wikipedia-huffman-coding2.txt");
        assertEquals("/home/alexande3/CSC207/grin-compression/files/wikipedia-huffman-coding.txt", 
            "/home/alexande3/CSC207/grin-compression/files/wikipedia-huffman-coding2.txt"
        );
    }

    /**
     * Tests pg2600.txt equality with decode.
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        Grin.decode("/home/alexande3/CSC207/grin-compression/files/pg2600.grin", 
        "/home/alexande3/CSC207/grin-compression/files/pg26002.txt");
        assertEquals("/home/alexande3/CSC207/grin-compression/files/pg2600.txt", 
            "/home/alexande3/CSC207/grin-compression/files/pg26002.txt"
        );
    }
}
