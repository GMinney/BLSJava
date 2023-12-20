package org.thoughtj.bls.keys;

import org.thoughtj.bls.ChainCode;
import org.thoughtj.bls.DASHJBLSJNI;
import org.thoughtj.bls.elements.G1Element;

public class ExtendedPublicKey {

    public final static long VERSION = DASHJBLSJNI.ExtendedPublicKey_VERSION_get();
    public final static long SIZE = DASHJBLSJNI.ExtendedPublicKey_SIZE_get();

    protected ExtendedPublicKey(long cPtr, boolean cMemoryOwn) {

    }

    public static ExtendedPublicKey fromBytes(byte[] bytes, boolean fLegacy) {

    }

    public static ExtendedPublicKey fromBytes(byte[] bytes) {

    }

    public ExtendedPublicKey publicChild(long i, boolean fLegacy) {

    }

    public ExtendedPublicKey publicChild(long i) {

    }

    public long getVersion() {

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