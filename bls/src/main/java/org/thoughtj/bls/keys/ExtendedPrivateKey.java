package org.thoughtj.bls.keys;

import org.thoughtj.bls.ChainCode;
import org.thoughtj.bls.elements.G1Element;

public class ExtendedPrivateKey {

    // Members

    // Size of an extended private key in bytes, pulled from bls-signatures
    public final static long SIZE = 77;

    // Constructors

    public static ExtendedPrivateKey fromSeed(byte[] bytes) {

    }

    public static ExtendedPrivateKey fromBytes(byte[] bytes) {

    }

    public ExtendedPrivateKey privateChild(long i, boolean fLegacy) {

    }

    public ExtendedPrivateKey privateChild(long i) {

    }

    public ExtendedPublicKey publicChild(long i) {

    }


    // Public Functions and Methods

    public long getVersion() {
        return ExtendedPublicKey.VERSION;
    }

    public short getDepth() {

    }

    public long getParentFingerprint() {

    }

    public long getChildNumber() {

    }

    public ChainCode getChainCode() {

    }

    public PrivateKey getPrivateKey() {

    }

    public G1Element getPublicKey() {

    }

    public ExtendedPublicKey getExtendedPublicKey(boolean fLegacy) {

    }

    public ExtendedPublicKey getExtendedPublicKey() {

    }

    public void serialize(byte[] buffer) {

    }

    public byte[] serialize() {

    }

    public ExtendedPrivateKey() {

    }

}
