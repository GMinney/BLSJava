package org.thoughtj.bls.arith;

import org.thoughtj.bls.schemes.BasicSchemeMPL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.thoughtj.bls.arith.Conversion.octetToInteger;
import static org.thoughtj.bls.arith.Curve.pointAddition;
import static org.thoughtj.bls.arith.Mapping.*;
import static org.thoughtj.bls.arith.Field.*;
import static org.thoughtj.bls.arith.Params.*;

public abstract class Hashing {

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * <strong>**Need two, one for G1Element and one for G2Elements depending on whether we're using a public key or private key**</strong>
     * @param msg
     * @param num_elements
     * @return
     */
    private static Point hashToField(byte[] msg, int num_elements) {
        BigInteger m = Params.EMBEDDING_DEGREE;
        BigDecimal k = BigDecimal.valueOf(256); // Target security level from ciphersuite
        BigDecimal middle = BigDecimal.valueOf(ln(new BigDecimal(G1_CONST_P), 2).doubleValue()).add(k);
        BigDecimal L = middle.divide(BigDecimal.valueOf(8), RoundingMode.CEILING);
        int len_in_bytes = num_elements * m * L; // m is the extension degree for the curve which is a const, L is the ceil((ceil(log2(p)) + k) / 8 where k is is the bit security level (128)
        byte[] uniform_bytes = expand_message_xmd(BasicSchemeMPL.getCIPHERSUITE_ID(), msg, len_in_bytes); //Determines bytes
        for (int i ; i <= 0; i++) {
            for (int j ; j <= 0; j++) {
                elm_offset = L * (j + i * m);
                tv = substr(uniform_bytes, elm_offset, L);
                e_j = octetToInteger(tv) mod p;
            }
            u_i = (e_0, ..., e_(m - 1));
        }
        return (u_0, ..., u_(num_elements - 1))
    }

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * hashToCurveG1
     * When the signature variant is minimal-signature-size, this function MUST output a point in G1.
     * @param message The message to be mapped input as an octet string (byte[])
     * @return a {@link Point}
     */
    public static Point hashToCurvePointG1(byte[] message) {
        Point u = hashToField(message, 2);
        Point Q0 = mapToCurve(u[0]);
        Point Q1 = mapToCurve(u[1]);
        Point R = pointAddition(Q0, Q1);
        return clearCofactor(R);
    }

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * hashToCurveG2
     * When the signature variant is minimal-pubkey size, this function MUST output a point in G2.
     * @param signature The message to be mapped input as an octet string (byte[])
     * @return a {@link Point}
     */
    public static Point hashToCurvePointG2(byte[] signature) {
        Point u = hashToField(signature, 2);
        Point Q0 = mapToCurve(u[0]);
        Point Q1 = mapToCurve(u[1]);
        Point R = pointAddition(Q0, Q1);
        return clearCofactor(R);
    }


    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * encodeToCurve
     * @param input
     * @return
     */
    public static Point encodeToCurve(byte[] input) {
        Point u = hashToField(input, 1);
        Point Q = mapToCurve(u[0]);
        return clearCofactor(Q);
    }

}
