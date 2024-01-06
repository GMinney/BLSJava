package org.thoughtj.bls.keys;

import org.thoughtj.bls.ChainCode;
import org.thoughtj.bls.elements.G1Element;

public class ExtendedPublicKey {

    // Members

    // Version number and Size bytes pulled from bls-signatures
    public final static long VERSION = 1;
    public final static long SIZE = 93;


    // Constructors

    public static ExtendedPublicKey fromBytes(byte[] bytes, boolean fLegacy) {

    }

    public static ExtendedPublicKey fromBytes(byte[] bytes) {

    }

    public ExtendedPublicKey publicChild(long i, boolean fLegacy) {

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

    public G1Element getPublicKey() {

    }

    public void serialize(byte[] buffer, boolean fLegacy) {

    }

    public void serialize(byte[] buffer) {

    }

    public byte[] serialize(boolean fLegacy) {

    }

    public byte[] serialize() {

    }

    public ExtendedPublicKey() {

    }


}