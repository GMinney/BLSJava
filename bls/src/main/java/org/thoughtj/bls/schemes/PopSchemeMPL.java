package org.thoughtj.bls.schemes;

import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.keys.Uint8VectorVector;

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
    public PopSchemeMPL() {
        super();
    }


    public static String getCIPHERSUITE_ID() {
        return CIPHERSUITE_G1; // TODO: Determine which ID is necessary
    }
    public G2Element popProve(PrivateKey seckey) {

//        Inputs:
//        - SK, a secret key in the format output by KeyGen.
//
//        Outputs:
//        - proof, an octet string.
//
//        Procedure:
//        1. PK = SkToPk(SK)
//        2. Q = hash_pubkey_to_point(PK)
//        3. R = SK * Q
//        4. proof = point_to_signature(R)
//        5. return proof

    }

    public boolean popVerify(G1Element pubkey, G2Element signature_proof) {


//        Inputs:
//        - PK, a public key in the format output by SkToPk.
//                - proof, an octet string in the format output by PopProve.
//
//        Outputs:
//        - result, either VALID or INVALID
//
//        Procedure:
//        1. R = signature_to_point(proof)
//        2. If R is INVALID, return INVALID
//        3. If signature_subgroup_check(R) is INVALID, return INVALID
//        4. If KeyValidate(PK) is INVALID, return INVALID
//        5. xP = pubkey_to_point(PK)
//        6. Q = hash_pubkey_to_point(PK)
//        7. C1 = pairing(Q, xP)
//        8. C2 = pairing(R, P)
//        9. If C1 == C2, return VALID, else return INVALID

    }

    public boolean popVerify(byte[] pubkey, byte[] proof) {
//      For byte array
//        Inputs:
//        - PK, a public key in the format output by SkToPk.
//                - proof, an octet string in the format output by PopProve.
//
//        Outputs:
//        - result, either VALID or INVALID
//
//        Procedure:
//        1. R = signature_to_point(proof)
//        2. If R is INVALID, return INVALID
//        3. If signature_subgroup_check(R) is INVALID, return INVALID
//        4. If KeyValidate(PK) is INVALID, return INVALID
//        5. xP = pubkey_to_point(PK)
//        6. Q = hash_pubkey_to_point(PK)
//        7. C1 = pairing(Q, xP)
//        8. C2 = pairing(R, P)
//        9. If C1 == C2, return VALID, else return INVALID

    }

    public boolean fastAggregateVerify(G1ElementVector pubkeys, byte[] message, G2Element signature) {

//        Inputs:
//        - PK_1, ..., PK_n, public keys in the format output by SkToPk.
//                - message, an octet string.
//        - signature, an octet string output by Aggregate.
//
//                Outputs:
//        - result, either VALID or INVALID.
//
//                Preconditions:
//        - n >= 1, otherwise return INVALID.
//                - The caller MUST know a proof of possession for all PK_i, and the
//        result of evaluating PopVerify on PK_i and this proof MUST be VALID.
//        See discussion above.
//
//                Procedure:
//        1. aggregate = pubkey_to_point(PK_1)
//        2. for i in 2, ..., n:
//        3.     next = pubkey_to_point(PK_i)
//        4.     aggregate = aggregate + next
//        5. PK = point_to_pubkey(aggregate)
//        6. return CoreVerify(PK, message, signature)

    }

    public boolean fastAggregateVerify(Uint8VectorVector pubkeys, byte[] message, byte[] signature) {

//        Inputs:
//        - PK_1, ..., PK_n, public keys in the format output by SkToPk.
//                - message, an octet string.
//        - signature, an octet string output by Aggregate.
//
//                Outputs:
//        - result, either VALID or INVALID.
//
//                Preconditions:
//        - n >= 1, otherwise return INVALID.
//                - The caller MUST know a proof of possession for all PK_i, and the
//        result of evaluating PopVerify on PK_i and this proof MUST be VALID.
//        See discussion above.
//
//                Procedure:
//        1. aggregate = pubkey_to_point(PK_1)
//        2. for i in 2, ..., n:
//        3.     next = pubkey_to_point(PK_i)
//        4.     aggregate = aggregate + next
//        5. PK = point_to_pubkey(aggregate)
//        6. return CoreVerify(PK, message, signature)

    }

}