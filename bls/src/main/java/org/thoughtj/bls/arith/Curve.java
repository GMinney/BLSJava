package org.thoughtj.bls.arith;
import org.thoughtj.bls.schemes.BasicSchemeMPL;

import static org.thoughtj.bls.arith.Params.*;
import static org.thoughtj.bls.arith.Field.*;

import java.math.BigInteger;
public abstract class Curve {

    /**
     * calcPubKey takes a Private Key k and returns the corresponding Public Key using the generator point determined by the BLS12-381 Curve
     * @param k the private key
     * @return a String at the moment
     */
    public static String calcPubKey(BigInteger k){
        Point pubKey = montgomeryLadder(k, G1_GENERATOR_POINT);
        String x_value = pubKey.x.toString(16);
        String y_value = pubKey.y.toString(16);
        return ("04" + x_value + y_value);
    }

    /**
     * Point addition on the BLS12-381 curve using curve arithmetic - see notes.txt for more information
     * @param P a Point to add
     * @param Q a Point to add
     * @return returns the new Point
     */
    public static Point pointAddition(Point P, Point Q) {
        if (P.equals(POINT_AT_INFINITY)) {
            return Q;
        }
        if (Q.equals(POINT_AT_INFINITY)) {
            return P;
        }

        // Compute the slope
        BigInteger lambda;
        if (P.x.equals(Q.x)) {
            lambda = (BigInteger.valueOf(3).multiply(P.x.pow(2)).add(G1_CONST_B))
                    .multiply(P.y.multiply(BigInteger.valueOf(2)).modInverse(G1_CONST_P));
        } else {
            lambda = (Q.y.subtract(P.y))
                    .multiply(Q.x.subtract(P.x).modInverse(G1_CONST_P));
        }

        // Compute the new point
        BigInteger xR = lambda.pow(2).subtract(P.x).subtract(Q.x).mod(G1_CONST_P);
        BigInteger yR = lambda.multiply(P.x.subtract(xR)).subtract(P.y).mod(G1_CONST_P);

        return new Point(xR, yR);
    }

    /**
     * Point doubling on the BLS12-381 curve using curve arithmetic - see notes.txt for more information
     * @param P a Point
     * @return a new Point on the curve that is effectively 2P
     */
    public static Point pointDouble(Point P) {
        if (P.equals(POINT_AT_INFINITY)) {
            return POINT_AT_INFINITY;
        }

        // Compute the slope
        BigInteger lambda = (BigInteger.valueOf(3).multiply(P.x.pow(2)).add(G1_CONST_B))
                .multiply(P.y.multiply(BigInteger.valueOf(2)).modInverse(G1_CONST_P));

        // Compute the new point
        BigInteger xR = lambda.pow(2).subtract(P.x.multiply(BigInteger.valueOf(2))).mod(G1_CONST_P);
        BigInteger yR = lambda.multiply(P.x.subtract(xR)).subtract(P.y).mod(G1_CONST_P);

        return new Point(xR, yR);
    }

    /**
     * Scalar Multiplication on the BLS12-381 curve using the Montgomery ladder
     * @param k a Private Key
     * @param P a Point
     * @return a new Point that is the Public Key
     */
    public static Point montgomeryLadder(BigInteger k, Point P) {
        // TODO: Rename this to Scalar multiplication
        Point R0 = POINT_AT_INFINITY;
        Point R1 = P;

        int bit_length = k.bitLength();
        for (int i = bit_length - 1; i >= 0; i--) {
            if (k.testBit(i)) {
                R0 = pointAddition(R0, R1);
                R1 = pointDouble(R1);
            } else {
                R1 = pointAddition(R0, R1);
                R0 = pointDouble(R0);
            }
        }

        return R0;
    }


    /**
     * Point Inversion that takes in a Point and uses the BigInteger.negate() on the y param, and returns the resulting point <br>
     * Inversion: Find the additive inverse of a point by negating the y-cord (this is for curves)
     * (for fields, use the Extended Euclidean Algorithm to find the modular multiplicative inverse)
     * @param point a Point to invert
     * @return the inverse of the point
     */
    public static Point pointInversion(Point point) {

        BigInteger y = point.y;
        return new Point(point.x, y.negate());

    }

    public static Point pointSubtraction(Point point1, Point point2) {

        return pointAddition(point1, pointInversion(point2));
    }


}
