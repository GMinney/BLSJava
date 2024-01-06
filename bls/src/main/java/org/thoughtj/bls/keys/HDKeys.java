package org.thoughtj.bls.keys;

import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G2Element;

public class HDKeys {

    // Members

    // Length of the hash
    public final static short HASH_LEN = 32;



    // Constructors

    public HDKeys() {

    }


    // Public Methods and Functions

    public static PrivateKey keyGen(byte[] seed) {

    }

    public static void iKMToLamportSk(byte[] outputLamportSk, byte[] ikm, long ikmLen, byte[] salt, long saltLen) {

    }

    public static void parentSkToLamportPK(byte[] outputLamportPk, PrivateKey parentSk, long index) {

    }

    public static PrivateKey deriveChildSk(PrivateKey parentSk, long index) {

    }

    public static PrivateKey deriveChildSkUnhardened(PrivateKey parentSk, long index) {

    }

    public static G1Element deriveChildG1Unhardened(G1Element pk, long index) {

    }

    public static G2Element deriveChildG2Unhardened(G2Element pk, long index) {

    }

}