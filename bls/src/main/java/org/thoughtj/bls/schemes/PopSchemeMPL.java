package org.thoughtj.bls.schemes;

import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.utils.Uint8VectorVector;
import org.thoughtj.bls.arith.*;

import java.math.BigInteger;

public class PopSchemeMPL extends CoreMPL {
    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params
    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    // protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_POP_";

    // Members

    protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_POP_";


    // Constructor

    public PopSchemeMPL() {
        super(CIPHERSUITE_G1);
    }


    // Methods and Functions

    public G2Element popProve(PrivateKey seckey) {
        BigInteger pubkey = Curve.calcPubKey(new BigInteger(seckey.serialize()));
        Point signature_point = Conversion.octetToCurvePointG2(seckey.serialize(), true);
        Point R_value = Curve.scalarMultiplication(pubkey, signature_point);
        return G2Element.fromBytes(Conversion.curvePointToOctetG2(R_value));
    }

    public boolean popVerify(G1Element pubkey, G2Element signature_proof) {
        return CoreAPI.CoreVerify(pubkey.serialize(), pubkey.serialize(), signature_proof.serialize());
    }

    public boolean popVerify(byte[] pubkey, byte[] proof) {
        return CoreAPI.CoreVerify(pubkey, pubkey, proof);
    }

    public boolean fastAggregateVerify(G1ElementVector pubkeys, byte[] message, G2Element signature) {
        Point aggregate = Conversion.octetToCurvePointG1(pubkeys.get(0).serialize(), true);
        for (int i = 1; i < pubkeys.size(); i++) {
            Point next = Conversion.octetToCurvePointG1(pubkeys.get(i).serialize(), true);
            assert aggregate != null;
            aggregate = Curve.pointAddition(aggregate, next);
        }
        assert aggregate != null;
        byte[] pubkey = Conversion.curvePointToOctetG1(aggregate);
        return CoreAPI.CoreVerify(pubkey, message, signature.serialize());
    }

    public boolean fastAggregateVerify(Uint8VectorVector pubkeys, byte[] message, byte[] signature) {
        Point aggregate = Conversion.octetToCurvePointG1(pubkeys.get(0).toByteArray(), true);
        for (int i = 1; i < pubkeys.size(); i++) {
            Point next = Conversion.octetToCurvePointG1(pubkeys.get(i).toByteArray(), true);
            assert aggregate != null;
            aggregate = Curve.pointAddition(aggregate, next);
        }
        assert aggregate != null;
        byte[] pubkey = Conversion.curvePointToOctetG1(aggregate);
        return CoreAPI.CoreVerify(pubkey, message, signature);
    }

}