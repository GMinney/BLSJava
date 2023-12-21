package org.thoughtj.bls.arith;

import java.math.BigInteger;

import static org.thoughtj.bls.arith.Field.fieldMultiplication;
import static org.thoughtj.bls.arith.Params.*;
import static org.thoughtj.bls.arith.Conversion.*;

public abstract class Mapping {

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * <strong>**Required for BLS12-381**</strong>
     * @param DST
     * @param msg
     * @param length_in_bytes
     * @return
     */
    private static BigInteger expand_message_xmd(String DST, String msg, long length_in_bytes) {
        //Expands a byte string and domain separation tag into a uniformly random byte string
        // DST = BLS12381G1_XMD:SHA-256_SSWU_RO_
        // msg = a message to be hashed based on the Ciphersuite (DST)
        ell = ceil(len_in_bytes / b_in_bytes);
        if (ell > 255 || len_in_bytes > 65535 || len(DST) > 255) {
            ABORT;
        }
        DST_prime = DST + integerToOctet(DST.length(), 1);
        Z_pad = integerToOctet(0, s_in_bytes);
        l_i_b_str = integerToOctet(len_in_bytes, 2);
        msg_prime = Z_pad + msg + l_i_b_str + integerToOctet(0, 1) + DST_prime;
        b_0 = Hash(msg_prime);
        b_1 = Hash(b_0 + integerToOctet(1, 1) + DST_prime);
        for (i in (2, ..., ell):
            b_i = Hash(strxor(b_0, b_(i - 1)) + integerToOctet(i, 1) + DST_prime);
        uniform_bytes = b_1 + ... + b_ell
        return substr(uniform_bytes, 0, len_in_bytes);
    }


    /**
     * General form of the mapping function. <br>
     * Choose a specific function per curve type. <br>
     * if target curve is Montgomery curve -> Elligator 2 method. <br>
     * if target curve is twisted Edwards curve -> twisted Edwards Elligator 2 method. <br>
     * if target curve is Weierstrass curve -> Simplified SWU method or Simplified SWU method for AB == 0 (for best performance) or Shallue-van de Woestijne (for simplicity) <br>
     * Shallue-van de Woestijne method works with any curve, this mapping is almost always more computationally expensive. <br>
     * <br>
     * For BLS BLS12-381 G1: <br>
     *    BLS12381G1_XMD:SHA-256_SSWU_RO_ <br>
     *    BLS12381G1_XMD:SHA-256_SSWU_NU_ <br>
     * For BLS BLS12-381 G2: <br>
     *    BLS12381G2_XMD:SHA-256_SSWU_RO_ <br>
     *    BLS12381G2_XMD:SHA-256_SSWU_NU_ <br>
     * Suits are indicated by SSWU which for both G1 and G2 is the "Simplified SWU for AB == 0" (No other functions are necessary unless building a cryptographic suite). <br>
     * @param u
     * @return
     */
    public static Point mapToCurve(BigInteger u) {
        // Call the mapping function for BLS12_381
    }

    /**
     * The Shallue and van de Woestijne method of mapping, a universal mapping function<br>
     * that is the most computationally expensive.<br>
     * <br>
     * <strong>**WIP**</strong>
     * @param u
     * @return
     */
    /*
    private static Point mapToCurveShallue(BigInteger u) {
        1. tv1 = u^2 * g(Z)
        2. tv2 = 1 + tv1
        3. tv1 = 1 - tv1
        4. tv3 = inv0(tv1 * tv2)
        5. tv4 = sqrt(-g(Z) * (3 * Z^2 + 4 * A))    # can be precomputed
        6. If sgn0(tv4) == 1, set tv4 = -tv4        # sgn0(tv4) MUST equal 0
        7. tv5 = u * tv1 * tv3 * tv4
        8. tv6 = -4 * g(Z) / (3 * Z^2 + 4 * A)      # can be precomputed
        9.  x1 = -Z / 2 - tv5
        10. x2 = -Z / 2 + tv5
        11. x3 = Z + tv6 * (tv2^2 * tv3)^2
        12. If is_square(g(x1)), set x = x1 and y = sqrt(g(x1))
        13. Else If is_square(g(x2)), set x = x2 and y = sqrt(g(x2))
        14. Else set x = x3 and y = sqrt(g(x3))
        15. If sgn0(u) != sgn0(y), set y = -y
        16. return (x, y)
    }
    */
    /**
     * The Simplified Shallue-van de Woestijne-Ulas method of mapping, a universal mapping function
     * that is computationally expensive.<br>
     * <br>
     * <strong>**WIP**</strong>
     * @param u
     * @return
     */

    private static Point map_to_curve_simple_swu(BigInteger u) {
        tv1 = inv0(Z^2 * u^4 + Z * u^2);
        x1 = (-B / A) * (1 + tv1);

        if (tv1 == 0) {
            set x1 = B / (Z * A);
        }
        gx1 = x1^3 + A * x1 + B;
        x2 = Z * u^2 * x1;
        gx2 = x2^3 + A * x2 + B;

        if (is_square(gx1)) {
            x = x1;
            y = sqrt(gx1);
        }
        else {
            x = x2;
            y = sqrt(gx2);
        }
        if (sgn0(u) != sgn0(y)) {
            y = -y;
        }
        return (x, y);
    }

    /**
     * The Simplified SWU for A or B == 0 method of mapping, a universal mapping function<br>
     * that is computationally expensive. Used for BLS and secp256k1. <br>
     * Needs an iso_map<br>
     * <br>
     * <strong>**Required for BLS12-381**</strong>
     * @param u
     * @return
     */
    private static Point map_to_curve_simple_swu_special(BigInteger u) {
        // (x', y') notation for a jacobian projection
        (x', y') = map_to_curve_simple_swu(u)    # (x', y') is on E'
        (x, y) = isogeny_map_G2(x', y')               # (x, y) is on E
        return (x, y);
    }

    /**
     * The Elligator 2 mapping function that applies to any montgomery curve with an order of 2.<br>
     * <br>
     * <strong>**WIP**</strong>
     * @param u
     * @return
     */
    /*
    private static Point map_to_curve_elligator2(BigInteger u) {
        1.  x1 = -(J / K) * inv0(1 + Z * u^2)
        2.  If x1 == 0, set x1 = -(J / K)
        3. gx1 = x1^3 + (J / K) * x1^2 + x1 / K^2
        4.  x2 = -x1 - (J / K)
        5. gx2 = x2^3 + (J / K) * x2^2 + x2 / K^2
        6.  If is_square(gx1), set x = x1, y = sqrt(gx1) with sgn0(y) == 1.
        7.  Else set x = x2, y = sqrt(gx2) with sgn0(y) == 0.
        8.   s = x * K
        9.   t = y * K
        10. return (s, t)
    }
    */

    /**
     * The Elligator 2 mapping function can be used to map to a twisted edwards curve via the process of<br>
     * 1. Hash to an equivalent montgomery curve, then transform to a twisted edwards via rational map. <br>
     * This method of hashing to a twisted Edwards curve thus requires identifying a corresponding Montgomery curve and rational map.<br>
     * <br>
     * <strong>**WIP**</strong>
     * @param u
     * @return
     */
    /*
    private static Point map_to_curve_elligator2_edwards(BigInteger u) {
        1. (s, t) = map_to_curve_elligator2(u)      # (s, t) is on M
        2. (v, w) = rational_map(s, t)              # (v, w) is on E
        3. return (v, w)
    }
    */

    /**
     * The cofactor can always be cleared via scalar multiplication by h. For elliptic curves where h = 1, i.e., <br>
     * the curves with a prime number of points, no operation is required. This applies, for example, to the NIST curves P-256, P-384, and P-521.<br>
     * In some cases, it is possible to clear the cofactor via a faster method than scalar multiplication by h. These methods are equivalent to<br>
     * (but usually faster than) multiplication by some scalar h_eff whose value is determined by the method and the curve.<br>
     * <br>
     * The following function computes the Frobenius endomorphism for an element of F = GF(p^2) with basis (1, I), where I^2 + 1 == 0 in F.
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/draft-irtf-cfrg-hash-to-curve-16#hashtofield">Chapt. 7 IETF</a>
     * @param input
     * @return
     */
    public static Point clearCofactor(Point input) {
        // Can just do this - h_eff * P

        // frobenius(x)
        // Input: x, an element of GF(p^2).
        //        Output: a, an element of GF(p^2).
        //
        //        Notation: x = x0 + I * x1, where x0 and x1 are elements of GF(p).
        //
        //        Steps:
        // 1. a = x0 - I * x1
        // 2. return a
    }


    /**
     * The computation for an 11_isogeny_map required by the SWU mapping function for G1, using rational functions. Required for BLS12-381<br>
     * Inputs are in terms of affine coordinates (single x or y coordinate)<br>
     * To avoid performing modular inversion, Wahby and Boneh describe a method of evaluating to a projective coordinate system<br>
     * The 11-isogeny map from (x', y') on E' to (x, y) on E is given by the following rational functions<br>
     * <br>
     * <strong>**Required for BLS12-381**</strong>
     * @see Params For constants used in this function
     * @return {@link Point} as an affine point (x or y)
     */
    private static Point isogeny_map_G1 (Point point) {

        x_num = k_(1,11) * x'^11 + k_(1,10) * x'^10 + k_(1,9) * x'^9 + ... + k_(1,0)
        x_den = x'^10 + k_(2,9) * x'^9 + k_(2,8) * x'^8 + ... + k_(2,0)

        x = x_num / x_den, where

        y_num = k_(3,15) * x'^15 + k_(3,14) * x'^14 + k_(3,13) * x'^13 + ... + k_(3,0)
        y_den = x'^15 + k_(4,14) * x'^14 + k_(4,13) * x'^13 + ... + k_(4,0)

        y = y' * y_num / y_den, where


        return new Point(x, y);
    }

    /**
     * The computation for a 3_isogeny_map required by the SWU mapping function for G2, using rational functions. Required for BLS12-381<br>
     * Inputs are in terms of affine coordinates (single x or y coordinate)<br>
     * To avoid performing modular inversion, Wahby and Boneh describe a method of evaluating to a projective coordinate system<br>
     * The 3-isogeny map from (x', y') on E' to (x, y) on E is given by the following rational functions<br>
     * <br>
     * <strong>**Required for BLS12-381**</strong>
     * @see Params For constants used in this function
     * @return {@link Point} as an affine point (x or y)
     */
    private static Point isogeny_map_G2 (Point point) {

        x_num = k_(1,3) * x'^3 + k_(1,2) * x'^2 + k_(1,1) * x' + k_(1,0)
        x_den = x'^2 + k_(2,1) * x' + k_(2,0)

        x = x_num / x_den, where

        y_num = k_(3,3) * x'^3 + k_(3,2) * x'^2 + k_(3,1) * x' + k_(3,0)
        y_den = x'^3 + k_(4,2) * x'^2 + k_(4,1) * x' + k_(4,0)

        y = y' * y_num / y_den

        return new Point(x, y);

    }

    public static Point pairing(Point point_on_G1, Point point_on_G2) {

        BigInteger k = EMBEDDING_DEGREE;
        BigInteger p_characteristic = G1_CONST_P;
        BigInteger group_order_r = G1_CONST_R;
        BigInteger t_depth = G1_CONST_H; // Check this
        BigInteger t = G2_CONST_H_EFF; //frobenius();

        Point optimal_ate_pairing;
        s = 6 t + 2 as s = ∑L−1 i = 0 si2i, where si ∈{−1, 0, 1
        } ;
        BigInteger s = fieldMultiplication(t, BigInteger.valueOf(6), BigInteger.valueOf(12));
        Point T = point_on_G2; // Q is the point_on_G2
        f = 1;


        // Miller Loop - calculates the value of the rational function at point P for function f6t+2,Q
        // It involves iterating over the points on the curve and evaluating certain line functions.
        for (int i = L −2; i <= 0;i--) {
            // Doubling step
            f = (f ·lT, T(point_on_G1));
            T = fieldMultiplication(T, 2, 12);
        }

        if (si == −1) {
            // addition step
            f = (f ·lT , −point_on_G2(point_on_G1));
            T = T −point_on_G2;
        }
        if (si = 1) {
            // addition step
            f = (f ·lT, point_on_G2(point_on_G1));
            T = T + point_on_G2;
        }

        // Frobenius application and final addition step
        Point Q1 = (πp(point_on_G2));
        Point Q2 = (πp2(point_on_G2));

        f = (f ·lT, Q1(point_on_G1));
        T = T + Q1;

        f = (f ·lT ,−Q2(point_on_G1));
        T = T −Q2;

        // Final exponentiation using frobenius
        Point f = fp12 ^ (−1 / r);


        return optimal_ate_pairing;

    }

    private static Point frobenius(){


        return point;
    }

}


