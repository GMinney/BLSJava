package org.thoughtj.bls.elements;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class G2Element {

    // A G2Element is a signature that is 96 bytes in size or 768 bits in compressed form
    // A G2Element is a signature that is 192 bytes in size or 1536 bits in uncompressed form
    // Relic uses Minimum pubkey sizes and compression - check notes for details
    public final static int SIZE = 96;

    private BigInteger g2_element;

    // Constructors
    public G2Element() {
        this.g2_element = new BigInteger(ByteBuffer.allocate(SIZE).array());
    }

    public G2Element(G2Element other) {
        this.g2_element = other.g2_element;
    }

    public static G2Element fromBytes(byte[] bytes, boolean fLegacy) {

    }

    public static G2Element fromBytes(byte[] bytes) {

    }

    public static G2Element fromBytesUnchecked(byte[] bytes, boolean fLegacy) {

    }

    public static G2Element fromBytesUnchecked(byte[] bytes) {

    }

    public static G2Element fromMessage(byte[] message, byte[] dst, int dst_len, boolean fLegacy) {

    }

    public static G2Element fromMessage(byte[] message, byte[] dst, int dst_len) {

    }


    // Functions and Methods
    public static G2Element generator() {

    }

    public boolean isValid() {

    }

    public void checkValid() {

    }

    public G2Element negate() {

    }

    public GTElement pair(G1Element a) {

    }

    public byte[] serialize(boolean fLegacy) {

    }

    public byte[] serialize() {

    }



}

