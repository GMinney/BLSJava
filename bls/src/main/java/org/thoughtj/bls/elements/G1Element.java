package org.thoughtj.bls.elements;

public class G1Element {

    // A G1Element is a public key that is 48 bytes in size or 384 bits in compressed form
    // A G1Element is a public key that is 96 bytes in size or 768 bits in uncompressed form
    public final static int SIZE = 48;

    protected G1Element(long cPtr, boolean cMemoryOwn) {

    }


    public G1Element() {

    }

    public static G1Element fromBytes(byte[] bytes, boolean fLegacy) {

    }

    public static G1Element fromBytes(byte[] bytes) {

    }

    public static G1Element fromBytesUnchecked(byte[] bytes, boolean fLegacy) {

    }

    public static G1Element fromBytesUnchecked(byte[] bytes) {

    }

    public static G1Element fromMessage(byte[] message, byte[] dst, int dst_len) {

    }

    public static G1Element generator() {

    }

    public boolean isValid() {

    }

    public void checkValid() {

    }

    public G1Element negate() {

    }

    public GTElement pair(G2Element b) {

    }

    public long getFingerprint(boolean fLegacy) {

    }

    public long getFingerprint() {

    }

    public byte[] serialize(boolean fLegacy) {

    }

    public byte[] serialize() {

    }

    public G1Element(G1Element other) {

    }

}
