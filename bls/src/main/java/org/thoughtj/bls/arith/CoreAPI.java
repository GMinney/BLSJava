package org.thoughtj.bls.arith;

import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.security.SecureRandom;

import static org.thoughtj.bls.arith.Curve.calcPubKey;
import static org.thoughtj.bls.arith.Curve.pointAddition;
import static org.thoughtj.bls.arith.Point.*;

public class CoreAPI {

    /**
     * KeyGen <br>
     * a key generation algorithm that outputs a secret key SK.
     * @return
     */
    public static byte[] KeyGen(){
        // Get random bytes
        // Should probably salt this: Setting salt to the value H("BLS-SIG-KEYGEN-SALT-") (i.e., the hash of an ASCII string comprising 20 octets) results in a KeyGen algorithm that is compatible with version 4
        SecureRandom r = new SecureRandom();
        byte[] bytes = new byte[64];
        r.nextBytes(bytes);
        // CipherSuite wants SHA-256, thought uses SHA-512
        // Hash with SHA-512
        /*
        Procedure:
            1. while True:
            2.     PRK = HKDF-Extract(salt, IKM || I2OSP(0, 1)) HKDF-Extract is as defined in RFC5869
            3.     OKM = HKDF-Expand(PRK, key_info || I2OSP(L, 2), L) HKDF-Expand is as defined in RFC5869
            4.     SK = OS2IP(OKM) mod r
            5.     if SK != 0:
            6.         return SK
            7.     salt = H(salt)
         */
        return Hashing.sha512().hashBytes(bytes).asBytes();
    }

    /**
     * SkToPk <br>
     * an algorithm that takes as input a secret key and outputs the corresponding public key.
     * @param secret_key
     * @return
     */
    public static byte[] SkToPk(byte[] secret_key){
        BigInteger element = Point.octetToFieldElement(secret_key);
        return calcPubKey(element).getBytes();
    }

    /**
     * KeyValidate <br>
     * ensures that a public key is valid.<br>
     * In particular, it ensures that a public key represents a valid,<br>
     * non-identity point that is in the correct subgroup.<br>
     * implementations MAY cache the result of KeyValidate in order to avoid unnecessarily repeating validation for known keys.
     * @param public_key
     * @return
     */
    public static boolean KeyValidate(byte[] public_key){
        // subgroup check needs to choose the correct one based on either min-pubkey-sizes or min-signature-sizes
        Point xP = octetToCurvePointG1(public_key, true);
        if (xP == INVALID) {
            return INVALID;
        }
        if (xP == the identity element) {
            return INVALID;
        }
        if (subgroup_check_E1(xP) == INVALID) {
            return INVALID;
        }
        return VALID;
    }

    /**
     * CoreSign<br>
     * a signing algorithm that generates a deterministic signature given a secret key SK and a message.<br>
     * <br>
     * Provided a secret key and a message, hash the message to a curve point and use the result to calculate the pubKey. <br>
     * Finally, take the resulting signature point and convert to a byte array.
     * @param secret_key
     * @param message
     * @return
     */
    public static byte[] CoreSign(BigInteger secret_key, byte[] message) {
        // Convert the message into a point
        Point Q = octetToCurvePointG1(message, true);
        // Calculate the public key with the secret key and the message point
        Point R = Curve.calcPubKey(secret_key, Q);
        // return the signature as a byte array
        return curvePointToOctetG2(R);
    }

    /**
     * CoreVerify<br>
     * Verification function for validating the pubKey is responsible for the signature.<br>
     * outputs VALID if signature is a valid signature of message under public key PK, and INVALID otherwise
     * @see <a href="https://datatracker.ietf.org/doc/html/draft-irtf-cfrg-bls-signature#name-keyvalidate">Documentation</a>
     * @param pub_key
     * @param message
     * @param signature
     * @return
     */
    public static boolean CoreVerify(byte[] pub_key, byte[] message, byte[] signature) {
        Point R = octetToCurvePointG2(signature, true);
        if (R == INVALID){
            return INVALID;
        }
        if (subgroup_check_E2(R) == INVALID){
            return INVALID;
        }
        if (KeyValidate(pub_key) == INVALID) {
            return INVALID;
        }
        Point xP = octetToCurvePointG1(pub_key, true);
        Point Q = octetToCurvePointG1(message);
        C1 = pairing(Q, xP);
        // P is the generator for either P1 or P2 based on the signature scheme
        C2 = pairing(R, Params.G1_CONST_P);
        if (C1 == C2){
            return VALID;
        }
        else {
            return INVALID;
        }
    }

    /**
     * CoreAggregate<br>
     * Aggregation function to combine multiple signatures into a single signature through curve arithmetic via point addition.
     * @param signatures
     * @return
     */
    private static byte[] CoreAggregate(byte[][] signatures) {
        // signature_to_point: uses the function octets_to_point_E1 for minimal-signature-size, uses the function octets_to_point_E2 for minimal-pubkey-size
        // Since relic uses minimum public key sizes, we will use the E2 function
        Point aggregate = octetToCurvePointG2(signatures[0], true);
        if (aggregate == INVALID) {
            return INVALID
        }
        for (byte[] i : signatures){
            Point next = octetToCurvePointG2(signatures[i], true);
            if (next == INVALID) {
                return INVALID;
            }
            aggregate = pointAddition(aggregate, next);
        }
        return curvePointToOctetG2(aggregate);
    }

    /**
     * CoreAggregateVerify<br>
     * an aggregate verification algorithm that outputs VALID if signature is a valid aggregated<br>
     * signature for a collection of public keys and messages, and outputs INVALID otherwise.
     * @return
     */
    public static boolean CoreAggregateVerify(byte[][] public_keys, byte[][] messages, byte[] aggregate_signature){
        Point R = octetToCurvePointG2(aggregate_signature, true);
        if (R == INVALID) {
            return INVALID;
        }
        if (subgroup_check_E2(R) == INVALID) {
            return INVALID;
        }
        C1 = 1 //(the identity element in GT)
        for (byte[] public_key : public_keys) {
            if (KeyValidate(public_key) == INVALID) {
                return INVALID;
            }
            Point xP = octetToCurvePointG1(public_key, true);
            Point Q = octetToCurvePointG1(message_i, true);
            C1 = C1 * pairing(Q, xP);
        }
        C2 = pairing(R, P);
        if (C1 == C2) {
            return VALID;
        }
        else {
            return INVALID;
        }
    }
    /*

     {
     {01000101, 010100100, 01001010} = pub_key1
     {01000101, 010100100, 01001010} = pub_key2
     {01000101, 010100100, 01001010} = pub_key3
     {01000101, 010100100, 01001010}
     {01000101, 010100100, 01001010}
     {01000101, 010100100, 01001010}





     }

     */


}
