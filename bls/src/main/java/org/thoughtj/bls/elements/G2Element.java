package org.thoughtj.bls.elements;

public class G2Element {

    // A G2Element is a signature that is 96 bytes in size or 768 bits in compressed form
    // A G2Element is a signature that is 192 bytes in size or 1536 bits in uncompressed form
    // Relic uses Minimum pubkey sizes and compression
    public final static int SIZE = 96;

    protected G2Element(long cPtr, boolean cMemoryOwn) {

    }


    public G2Element() {

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

    public G2Element(G2Element other) {

    }

}

