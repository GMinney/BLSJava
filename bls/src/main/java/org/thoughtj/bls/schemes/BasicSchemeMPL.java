package org.thoughtj.bls.schemes;

import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.utils.Uint8VectorVector;

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
    public BasicSchemeMPL() {
        super();
    }


    public static String getCIPHERSUITE_ID() {
        return CIPHERSUITE_G1; // TODO: Determine which ID is necessary
    }

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {

        //result = AggregateVerify((PK_1, ..., PK_n),
        //                         (message_1, ..., message_n),
        //                         signature)
        //
        //Inputs:
        //- PK_1, ..., PK_n, public keys in the format output by SkToPk.
        //- message_1, ..., message_n, octet strings.
        //- signature, an octet string output by Aggregate.
        //
        //Outputs:
        //- result, either VALID or INVALID.
        //
        //Precondition: n >= 1, otherwise return INVALID.
        //
        //Procedure:
        //1. If any two input messages are equal, return INVALID.
        //2. return CoreAggregateVerify((PK_1, ..., PK_n),
        //                              (message_1, ..., message_n),
        //                              signature)

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
//        1. If any two input messages are equal, return INVALID.
//        2. return CoreAggregateVerify((PK_1, ..., PK_n),
//        (message_1, ..., message_n),
//        signature)

    }

}