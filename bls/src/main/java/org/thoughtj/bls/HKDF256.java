package org.thoughtj.bls;

public class HKDF256 {

    public final static short HASH_LEN = 64;
    protected HKDF256(long cPtr, boolean cMemoryOwn) {

    }

    public static void extract(byte[] prk_output, byte[] salt, long saltLen, byte[] ikm, long ikm_len) {

    }

    public static void expand(byte[] okm, long L, byte[] prk, byte[] info, long infoLen) {

    }

    public static void extractExpand(byte[] output, long outputLen, byte[] key, long keyLen, byte[] salt, long saltLen, byte[] info, long infoLen) {

    }

    public HKDF256() {

    }


}