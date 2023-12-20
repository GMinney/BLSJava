package org.thoughtj.bls.arith;
import org.thoughtj.bls.schemes.BasicSchemeMPL;

import static org.thoughtj.bls.arith.Params.*;
import static org.thoughtj.bls.arith.Field.*;

import java.math.BigInteger;
public class Curve {

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
    private static Point pointDouble(Point P) {
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
     * @param P the Generator point of the BLS12-381 curve
     * @return a new Point that is the Public Key
     */
    private static Point montgomeryLadder(BigInteger k, Point P) {
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

    public static boolean pairing(Point point_on_G1, Point point_on_G2) {
        // undefined when its input points are not in the prime-order subgroups of E1 and E2. The resulting behavior is unpredictable, and may enable forgeries.
        // A pairing is defined as a bilinear map e: (G_1, G_2) -> G_T satisfying the following properties:
        // Bilinearity: for any S in G_1, T in G_2, and integers K and L, e([K]S, [L]T) = e(S, T)^{K * L}.
        // Non-degeneracy: for any T in G_2, e(S, T) = 1 if and only if S = O_E. Similarly, for any S in G_1, e(S, T) = 1 if and only if T = O_E.
        // a pairing over BLS curves constructs optimal Ate pairings
        //
        //
        //   BLS12:
        //       p = (t - 1)^2 * (t^4 - t^2 + 1) / 3 + t
        //       r = t^4 - t^2 + 1
        //
        //   BLS12-381:
        //      t = -2^63 - 2^62 - 2^60 - 2^57 - 2^48 - 2^16
        //      where the size of p becomes 381-bit length
        //
        //
        //  Functions in the Optimal Ate Pairing
        BigInteger k = EMBEDDING_DEGREE;
        BigInteger p_characteristic = G1_CONST_P;
        BigInteger group_order_r = G1_CONST_R;
        BigInteger t_depth = G1_CONST_H; // Check this
        BigInteger t = G2_CONST_H_EFF; //frobenius();
        // Hamming weight
        // Bitdepth L = "65"
        // t should have security level for bls12-381 with roughly 126 bits, AES-128 would be roughly 64 bits

        // u = G2_CONST_H_EFF;
        // q = p_characteristic which is roughly 2^381 bit depth
        // t = t must be 1 (mod 3). Turns out this is the G2_CONST_H_EFF
        // p = p_characteristic
        // r = group_order_r
        // p(t) =
        // r(t) =
        // t(sub)r(t) =
        // πp = frobenius endomorphism given by πp(x, y) = (xp, yp).

        // T = Intermediary point
        // P = the point_on_G1
        // s =
        // f =

        // → is part of a type signature
        // For sets X and Y, f:X→Y is a function "from X to Y ", meaning that f has domain X and codomain Y.

        // ↦ is part of a function definition


        Point optimal_ate_pairing;
        s = 6t + 2 as s = ∑L−1 i=0 si2i , where si ∈ {−1, 0, 1};
        BigInteger s = fieldMultiplication(t, BigInteger.valueOf(6), BigInteger.valueOf(12));
        Point T = point_on_G2; // Q is the point_on_G2
        f = 1;


        // Miller Loop - calculates the value of the rational function at point P for function f6t+2,Q
        // It involves iterating over the points on the curve and evaluating certain line functions.
        for (int i = L − 2; i <=0; i--){
            // Doubling step
            f = (f · lT ,T (point_on_G1));
            T = fieldMultiplication(T, 2, 12);
        }

        if (si == −1) {
            // addition step
            f = (f · lT , −point_on_G2(point_on_G1));
            T = T − point_on_G2;
        }
        else if (si = 1) {
            // addition step
            f = (f · lT , point_on_G2(point_on_G1));
            T = T + point_on_G2;
        }

        // Frobenius application and final addition step
        Q1 = (πp(point_on_G2));
        Q2 = (πp2 (point_on_G2));

        f = (f · lT ,Q1(point_on_G1));
        T = T + Q1;

        f = (f · lT ,−Q2(point_on_G1));
        T = T − Q2;

        // Final exponentiation using frobenius
        Point f = fp12^(−1/r);


        return optimal_ate_pairing;


    }






}
