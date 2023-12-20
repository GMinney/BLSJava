package org.thoughtj.bls.schemes;

import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.Uint8VectorVector;

public class BasicSchemeMPL extends CoreMPL {

    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params

    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_NUL_";

    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_NUL_";


    // Constructor
    protected BasicSchemeMPL(long cPtr, boolean cMemoryOwn) {
        super();
    }
    // Constructor
    public BasicSchemeMPL() {

    }


    public static String getCIPHERSUITE_ID() {
        return CIPHERSUITE_G1; // TODO: Determine which ID is necessary
    }

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {

    }

    public boolean aggregateVerify(G1ElementVector pubkeys, Uint8VectorVector messages, G2Element signature) {

    }

}