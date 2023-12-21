package org.thoughtj.bls.elements;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class G1Element {

    // A G1Element is a public key that is 48 bytes in size or 384 bits in compressed form (odd or even byte + x coord )
    // A G1Element is a public key that is 96 bytes in size or 768 bits in uncompressed form
    public final static int SIZE = 48;

    private BigInteger g1_element;


    public G1Element() {
        this.g1_element = new BigInteger(ByteBuffer.allocate(SIZE).array());
    }

    public G1Element(G1Element other) {
        this.g1_element = other.g1_element;
    }

    public static G1Element fromBytes(byte[] bytes, boolean fLegacy) {
        return null;
    }

    public static G1Element fromBytes(byte[] bytes) {
        return null;
    }

    public static G1Element fromBytesUnchecked(byte[] bytes, boolean fLegacy) {
        return null;
    }

    public static G1Element fromBytesUnchecked(byte[] bytes) {
        return null;
    }

    public static G1Element fromMessage(byte[] message, byte[] dst, int dst_len) {
        return null;
    }

    public static G1Element generator() {
        return null;
    }

    public boolean isValid() {
        return false;
    }

    public void checkValid() {

    }

    public G1Element negate() {
        return null;
    }

    public GTElement pair(G2Element b) {
        return null;
    }

    public long getFingerprint(boolean fLegacy) {

    }

    public long getFingerprint() {

    }

    public byte[] serialize(boolean fLegacy) {
        return null;
    }

    public byte[] serialize() {
        return null;
    }



}
