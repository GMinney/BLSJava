package org.thoughtj.bls.arith;

import org.thoughtj.bls.schemes.BasicSchemeMPL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

import static org.thoughtj.bls.arith.Conversion.octetsToInteger;
import static org.thoughtj.bls.arith.Curve.pointAddition;
import static org.thoughtj.bls.arith.Mapping.*;
import static org.thoughtj.bls.arith.Field.*;
import static org.thoughtj.bls.arith.Params.*;
import static org.thoughtj.bls.arith.Verify.*;

public abstract class Hashing {

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * <strong>**Need two, one for G1Element and one for G2Elements depending on whether we're using a public key or private key**</strong>
     * @param msg
     * @param num_elements
     * @return
     */
    private static BigInteger[][] hashToField(byte[] msg, int num_elements) {
        //BigInteger m = Params.EMBEDDING_DEGREE;
        //BigDecimal k = BigDecimal.valueOf(256); // Target security level from ciphersuite
        //BigDecimal middle = BigDecimal.valueOf(ln(new BigDecimal(G1_CONST_P), 2).doubleValue()).add(k);
        //BigDecimal L_value = middle.divide(BigDecimal.valueOf(8), RoundingMode.CEILING);
        //BigInteger L = L_value.toBigInteger();

        int L = BLS_L_VALUE.intValueExact();
        BigInteger m = G1_CONST_M; // the extension degree of F, m >= 1 for G1 it's "1" and for G2 it's "2"
        BigInteger p = G1_CONST_P;

        // If failure, abort
        byte[] uniform_bytes = expand_message_xmd(BasicSchemeMPL.getCIPHERSUITE_ID(), msg.toString(), L); // Hashes the message according to the Ciphersuite rules and returns a uniform byte string

        BigInteger[] element_data = new BigInteger[m.intValueExact()];
        BigInteger[][] field_elements = new BigInteger[num_elements][m.intValueExact()];

        // Check the loop amount
        for (int i = 0; i < num_elements; i++) {
            for (int j = 0; j < m.intValueExact(); j++) {
                int elm_offset = L * (j+i*m.intValueExact());
                byte[] tv = substr(uniform_bytes, elm_offset, L);
                element_data[j] = octetsToInteger(tv).mod(p);
            }
            field_elements[i] = element_data; // for G2

        }
        // output a list of field elements
        return field_elements;
    }

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * hashToCurveG1
     * When the signature variant is minimal-signature-size, this function MUST output a point in G1.
     * @param message The message to be mapped input as an octet string (byte[])
     * @return a {@link Point}
     */
    public static Point hashToCurvePointG1(byte[] message) {
        BigInteger[][] u = hashToField(message, 1);
        Point Q0 = mapToCurve(u[0][0]);
        return clearCofactor(Q0);
    }

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * hashToCurveG2
     * When the signature variant is minimal-pubkey size, this function MUST output a point in G2.
     * @param signature The message to be mapped input as an octet string (byte[])
     * @return a {@link Point}
     */
    public static Point hashToCurvePointG2(byte[] signature) {
        BigInteger[][] u = hashToField(signature, 2);
        Point Q0 = mapToCurve(u[0][0]);
        Point Q1 = mapToCurve(u[1][0]);
        Point R = pointAddition(Q0, Q1);
        return clearCofactor(R);
    }

}
