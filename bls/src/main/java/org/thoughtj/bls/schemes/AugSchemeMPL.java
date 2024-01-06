package org.thoughtj.bls.schemes;

import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.utils.Uint8VectorVector;

public class AugSchemeMPL extends CoreMPL {

    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params

    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_AUG_";

    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_AUG_";


    // Constructor
    public AugSchemeMPL() {
        super();
    }


    public static String getCIPHERSUITE_ID() {
        return CIPHERSUITE_G1; // TODO: Determine which ID is necessary
    }
    public G2Element sign(PrivateKey seckey, byte[] message) {

//        Inputs:
//        - SK, a secret key in the format output by KeyGen.
//        - message, an octet string.
//
//        Outputs:
//        - signature, an octet string.
//
//        Procedure:
//        1. PK = SkToPk(SK)
//        2. return CoreSign(SK, PK || message)

    }

    public G2Element sign(PrivateKey seckey, byte[] message, G1Element prepend_pk) {

//        Inputs:
//        - SK, a secret key in the format output by KeyGen.
//        - message, an octet string.
//
//        Outputs:
//        - signature, an octet string.
//
//        Procedure:
//        1. PK = SkToPk(SK)
//        2. return CoreSign(SK, PK || message)

    }

    public boolean verify(byte[] pubkey, byte[] message, byte[] signature) {

//        result = Verify(PK, message, signature)
//
//        Inputs:
//        - PK, a public key in the format output by SkToPk.
//                - message, an octet string.
//        - signature, an octet string in the format output by CoreSign.
//
//        Outputs:
//        - result, either VALID or INVALID.
//
//                Procedure:
//        1. return CoreVerify(PK, PK || message, signature)

    }

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {

//        result = AggregateVerify((PK_1, ..., PK_n),
//        (message_1, ..., message_n),
//        signature)
//
//        Inputs:
//        - PK_1, ..., PK_n, public keys in the format output by SkToPk.
//                - message_1, ..., message_n, octet strings.
//                - signature, an octet string output by Aggregate.
//
//                Outputs:
//        - result, either VALID or INVALID.
//
//                Precondition: n >= 1, otherwise return INVALID.
//
//                Procedure:
//        1. for i in 1, ..., n:
//        2.     mprime_i = PK_i || message_i
//        3. return CoreAggregateVerify((PK_1, ..., PK_n),
//        (mprime_1, ..., mprime_n),
//        signature)

    }

    public boolean aggregateVerify(G1ElementVector pubkeys, Uint8VectorVector messages, G2Element signature) {

//        result = AggregateVerify((PK_1, ..., PK_n),
//        (message_1, ..., message_n),
//        signature)
//
//        Inputs:
//        - PK_1, ..., PK_n, public keys in the format output by SkToPk.
//                - message_1, ..., message_n, octet strings.
//                - signature, an octet string output by Aggregate.
//
//                Outputs:
//        - result, either VALID or INVALID.
//
//                Precondition: n >= 1, otherwise return INVALID.
//
//                Procedure:
//        1. for i in 1, ..., n:
//        2.     mprime_i = PK_i || message_i
//        3. return CoreAggregateVerify((PK_1, ..., PK_n),
//        (mprime_1, ..., mprime_n),
//        signature)

    }

}
