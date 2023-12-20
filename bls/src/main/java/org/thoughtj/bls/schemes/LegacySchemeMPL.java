package org.thoughtj.bls.schemes;

import org.thoughtj.bls.*;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.elements.G2ElementVector;
import org.thoughtj.bls.keys.PrivateKey;

public class LegacySchemeMPL extends CoreMPL {

    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params

    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_POP_";

    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_POP_";


    // Constructor
    protected LegacySchemeMPL(long cPtr, boolean cMemoryOwn) {
        super();
    }
    // Constructor
    public LegacySchemeMPL() {

    }


    public static String getCIPHERSUITE_ID() {
        return CIPHERSUITE_G1; // TODO: Determine which ID is necessary
    }

    public byte[] skToPk(PrivateKey seckey) {

    }

    public G2Element sign(PrivateKey seckey, byte[] message) {

    }

    public boolean verify(byte[] pubkey, byte[] message, byte[] signature) {

    }

    public byte[] aggregate(Uint8VectorVector signatures) {

    }

    public G2Element aggregateSecure(G1ElementVector vecPublicKeys, G2ElementVector vecSignatures, byte[] message) {

    }

    public boolean verifySecure(G1ElementVector vecPublicKeys, G2Element signature, byte[] message) {

    }

    public boolean aggregateVerify(G1ElementVector pubkeys, Uint8VectorVector messages, G2Element signature) {

    }

}