package org.thoughtj.bls.schemes;

import org.thoughtj.bls.*;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.keys.PrivateKey;

public class PopSchemeMPL extends CoreMPL {

    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params

    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_POP_";

    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_POP_";


    // Constructor
    protected PopSchemeMPL(long cPtr, boolean cMemoryOwn) {
        super();
    }
    // Constructor
    public PopSchemeMPL() {

    }


    public static String getCIPHERSUITE_ID() {
        return CIPHERSUITE_G1; // TODO: Determine which ID is necessary
    }
    public G2Element popProve(PrivateKey seckey) {

    }

    public boolean popVerify(G1Element pubkey, G2Element signature_proof) {

    }

    public boolean popVerify(byte[] pubkey, byte[] proof) {

    }

    public boolean fastAggregateVerify(G1ElementVector pubkeys, byte[] message, G2Element signature) {

    }

    public boolean fastAggregateVerify(Uint8VectorVector pubkeys, byte[] message, byte[] signature) {

    }

}