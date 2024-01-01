package org.thoughtj.bls.arith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.thoughtj.bls.HKDF256.hash;
import static org.thoughtj.bls.arith.Field.*;
import static org.thoughtj.bls.arith.Params.*;
import static org.thoughtj.bls.arith.Conversion.*;
import static org.thoughtj.bls.arith.Verify.*;
import static org.thoughtj.bls.arith.Curve.*;

public abstract class Mapping {

    /**
     * TODO: WRITE A BETTER DESCRIPTION <br>
     * <strong>**Required for BLS12-381**</strong>
     * @param DST
     * @param msg
     * @param length_in_bytes
     * @return
     */
    public static byte[] expand_message_xmd(String DST, String msg, int length_in_bytes) {
        // Expands a byte string and domain separation tag into a uniformly random byte string
        // DST = BLS12381G1_XMD:SHA-256_SSWU_RO_
        // msg = a message to be hashed based on the Ciphersuite (DST)

        int L = BLS_L_VALUE.intValue();
        int s_in_bytes = 64; // input block size of hash function, for SHA-256 this is 64

        if (L > 255 || length_in_bytes > 65535 || DST.length() > 255) {
            return null;
        }

        // Initialize DST buffer and array
        ByteBuffer DST_buffer = ByteBuffer.wrap(new byte[DST.getBytes().length + integerToOctets(DST.length(),1).length]);
        DST_buffer.put(DST.getBytes());
        DST_buffer.put(integerToOctets(DST.length(), 1));
        byte[] DST_prime = DST_buffer.array();

        byte[] Z_pad = integerToOctets(0, s_in_bytes);

        byte[] l_i_b_str = integerToOctets(length_in_bytes, 2);

        // Initialize msg_prime buffer and array
        ByteBuffer msg_prime_buffer = ByteBuffer.wrap(new byte[Z_pad.length + msg.length() + l_i_b_str.length + integerToOctets(0,1).length + DST_prime.length]);
        msg_prime_buffer.put(Z_pad);
        msg_prime_buffer.put(msg.getBytes());
        msg_prime_buffer.put(l_i_b_str);
        msg_prime_buffer.put(integerToOctets(0, 1));
        msg_prime_buffer.put(DST_prime);
        byte[] msg_prime = msg_prime_buffer.array();

        // Set first two
        byte[] b_0 = hash(msg_prime);
        byte[] hash_storage = b_0;

        // Concat b_1 byte strings into a buffer, then hash
        ByteBuffer b_1_prehash = ByteBuffer.wrap(new byte[hash_storage.length + integerToOctets(1,1).length + DST_prime.length]);
        b_1_prehash.put(hash_storage);
        b_1_prehash.put(integerToOctets(1,1));
        b_1_prehash.put(DST_prime);
        byte[] b_1 = hash(b_1_prehash.array()); // put b_1 in buffer

        ByteBuffer buffer = ByteBuffer.wrap(b_1);
        // loop the remaining
        for (int i = 2 ; i<= L ; i++){
            // place into buffer
            buffer.put(hash_storage);
            buffer.put(integerToOctets(i, 1));
            buffer.put(DST_prime);
            // hash the buffer
            byte[] result = hash(buffer.array());
            // place the hash into buffer
            buffer.put(result);
            // place the hash into storage to be rehashed on next iteration.
            hash_storage = result;
        }

        // append separate bytes into a uniform byte string
        byte[] uniform_bytes = buffer.array();

        return substr(uniform_bytes, 0, length_in_bytes);
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
        return map_to_curve_simple_swu_special(u);
    }

    /**
     * The Shallue and van de Woestijne method of mapping, a universal mapping function<br>
     * that is the most computationally expensive.<br>
     * <br>
     * <strong>**WIP**</strong>
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
        BigInteger A = G1_CONST_A;
        BigInteger B = G1_CONST_B;
        BigInteger Z = BigInteger.ZERO;
        BigInteger tv1 = inv0(
                fieldAddition(
                    fieldMultiplication(fieldExponentiation(Z, BigInteger.valueOf(2), EMBEDDING_DEGREE), fieldExponentiation(u, BigInteger.valueOf(4), EMBEDDING_DEGREE), EMBEDDING_DEGREE),
                    fieldMultiplication(Z, fieldExponentiation(u, BigInteger.valueOf(2), EMBEDDING_DEGREE), EMBEDDING_DEGREE),
                    EMBEDDING_DEGREE));
        
        BigInteger x1 = fieldAddition(
                fieldInversion(
                        fieldNegation(B, EMBEDDING_DEGREE),
                        fieldMultiplication(A, BigInteger.valueOf(1), EMBEDDING_DEGREE)),
                tv1, EMBEDDING_DEGREE);

        if (tv1.equals(BigInteger.valueOf(0))) {
            x1 = fieldInversion(B, (fieldMultiplication(Z, A, EMBEDDING_DEGREE)));
        }

        BigInteger gx1 = fieldAddition(
                fieldExponentiation(
                        x1,
                        BigInteger.valueOf(3), EMBEDDING_DEGREE),
                fieldAddition(
                        fieldMultiplication(
                                A,
                                x1, EMBEDDING_DEGREE),
                        B, EMBEDDING_DEGREE), EMBEDDING_DEGREE);

        BigInteger x2 = fieldMultiplication(
                fieldMultiplication(
                        Z,
                        fieldExponentiation(u, BigInteger.valueOf(2), EMBEDDING_DEGREE), EMBEDDING_DEGREE),
                x1, EMBEDDING_DEGREE);

        BigInteger gx2 = fieldAddition(
                fieldExponentiation(x2, BigInteger.valueOf(3), EMBEDDING_DEGREE),
                fieldAddition(
                        fieldMultiplication(A, x2, EMBEDDING_DEGREE),
                        B, EMBEDDING_DEGREE), EMBEDDING_DEGREE);

        BigInteger x;
        BigInteger y;

        if (is_square(gx1)) {
            x = x1;
            y = fieldSquareRoot(gx1, EMBEDDING_DEGREE);
        }
        else {
            x = x2;
            y = fieldSquareRoot(gx2, EMBEDDING_DEGREE);
        }
        if (sgn0(u) != sgn0(y)) {
            y = fieldNegation(y, EMBEDDING_DEGREE);
        }
        return new Point(x, y);
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
        Point xy_prime = map_to_curve_simple_swu(u);    // (x', y') is on E'    // (x, y) is on E
        return isogeny_map_G2(xy_prime);
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
        return clear_cofactor_bls12381_g2(input);
    }

    public static BigInteger frobenius(BigInteger element) {
        // frobenius(x)
        // Input: x, an element of GF(p^2).
        //        Output: a, an element of GF(p^2).
        //
        //        Notation: x = x0 + I * x1, where x0 and x1 are elements of GF(p).
        //
        //        Steps:
        // 1. a = x0 - I * x1
        // 2. return a
        //BigInteger a = x0 + I * x1;
        return fieldExponentiation(element, BigInteger.valueOf(2), EMBEDDING_DEGREE);
    }

    public static Point frobenius(Point element) {
        // frobenius(X)
        // Input: X, a point of GF(p^2).
        //        Output: a, an element of GF(p^2).
        //
        //        Notation: x = x0 + I * x1, where x0 and x1 are elements of GF(p).
        //
        //        Steps:
        // 1. a = x0 - I * x1
        // 2. return a
        //BigInteger a = x0 + I * x1;

        // (1, I) is the basis for F, where I^2 + 1 == 0 in F
        // I think I is the generator for BLS12-381?

        return new Point(fieldExponentiation(element.x, BigInteger.valueOf(2), EMBEDDING_DEGREE), element.y);
    }

    public static Point psi(Point point) {
//        psi(xn, xd, yn, yd)
//
//        Input: P, a point (xn / xd, yn / yd) on the curve E (see above).
//        Output: Q, a point on the same curve.
//
//                Constants:
//        1. c1 = 1 / (1 + I)^((p - 1) / 3)           # in GF(p^2)
//        2. c2 = 1 / (1 + I)^((p - 1) / 2)           # in GF(p^2)
//
//        Steps:
//        1. qxn = c1 * frobenius(xn)
//        2. qxd = frobenius(xd)
//        3. qyn = c2 * frobenius(yn)
//        4. qyd = frobenius(yd)
//        5. return (qxn, qxd, qyn, qyd)

        // Coordinates are represented as ratios, i.e., (xn, xd, yn, yd) corresponds to the point (xn / xd, yn / yd)
        // When points are represented in affine coordinates, one can simply ignore the denominators (xd == 1 and yd == 1).

        // Assume p is the characteristic of the base field
        BigInteger p = G1_CONST_P;

        // Calculate (p - 1) / 3
        BigInteger exponent = p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(3));

        // Calculate (1 + i) ^ ((p - 1) / 3)
        BigInteger psiReal = BigInteger.ONE;
        BigInteger psiImaginary = BigInteger.ONE;

        for (BigInteger i = BigInteger.ZERO; i.compareTo(exponent) < 0; i = i.add(BigInteger.ONE)) {
            // Multiply (1 + i) by itself
            BigInteger newReal = psiReal.subtract(psiImaginary);
            BigInteger newImaginary = psiReal.add(psiImaginary);

            // Update (1 + i)
            psiReal = newReal;
            psiImaginary = newImaginary;

            // Ensure the values stay within the field
            psiReal = psiReal.mod(p);
            psiImaginary = psiImaginary.mod(p);
        }

        BigInteger xn = point.x.multiply(psiReal).subtract(point.y.multiply(psiImaginary)).mod(p);
        BigInteger yn = point.x.multiply(psiImaginary).add(point.y.multiply(psiReal)).mod(p);

        return new Point(xn,yn);
    }

    public static Point psi2(Point point) {
//    The following function efficiently computes psi(psi(P)).
//
//    psi2(xn, xd, yn, yd)
//
//    Input: P, a point (xn / xd, yn / yd) on the curve E (see above).
//    Output: Q, a point on the same curve.
//
//    Constants:
//            1. c1 = 1 / 2^((p - 1) / 3)                 # in GF(p^2)
//
//    Steps:
//            1. qxn = c1 * xn
//      2. qyn = -yn
//      3. return (qxn, xd, qyn, yd)

        // Calculate (p - 1) / 3
        BigInteger exponent = G1_CONST_P.subtract(BigInteger.ONE).divide(BigInteger.valueOf(3));

        // Calculate c1 = 1 / 2^((p - 1) / 3)
        BigInteger c1 = BigInteger.valueOf(2).modPow(exponent.negate(), G1_CONST_P);

        // Apply psi to the x-coordinate
        BigInteger qxn = point.x.multiply(c1).mod(G1_CONST_P);
        // Negate the y-coord
        BigInteger qyn = fieldNegation(point.y, EMBEDDING_DEGREE);

        return new Point(qxn, qyn);

    }

    public static Point clear_cofactor_bls12381_g2(Point point) {
//    The following function maps any point on the elliptic curve E (Section 8.8.2) into the prime-order subgroup G2. This function
//    returns a point equal to h_eff * P, where h_eff is the parameter given in Section 8.8.2.
//
//    clear_cofactor_bls12381_g2(P)
//
//    Input: P, a point (xn / xd, yn / yd) on the curve E (see above).
//    Output: Q, a point in the subgroup G2 of BLS12-381.
//
//    Constants:
//            1. c1 = -15132376222941642752       # the BLS parameter for BLS12-381
//            # i.e., -0xd201000000010000
//
//    Notation: in this procedure, + and - represent elliptic curve point
//    addition and subtraction, respectively, and * represents scalar
//    multiplication.
//
//            Steps:
//              1.  t1 = c1 * P
//              2.  t2 = psi(P)
//              3.  t3 = 2 * P
//              4.  t3 = psi2(t3)
//              5.  t3 = t3 - t2
//              6.  t2 = t1 + t2
//              7.  t2 = c1 * t2
//              8.  t3 = t3 + t2
//              9.  t3 = t3 - t1
//              10.  Q = t3 - P
//              11. return Q
        Point t1 = montgomeryLadder(G1_CONST_H_EFF, point);
        Point t2 = psi(point);
        Point t3 = pointDouble(point);
        t3 = psi2(t3);
        t3 = pointSubtraction(t3, t1);
        t2 = pointAddition(t1, t2);
        t2 = montgomeryLadder(G1_CONST_H_EFF, t2);
        t3 = pointAddition(t3, t2);
        t3 = pointSubtraction(t3, t1);
        return pointSubtraction(t3, point);
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

        BigInteger x_prime = point.x;
        BigInteger y_prime = point.y;
        BigInteger x_num = BigInteger.ZERO;
        BigInteger x_den = fieldExponentiation(x_prime, BigInteger.valueOf(10), G1_CONST_P);
        BigInteger y_num = BigInteger.ZERO;;
        BigInteger y_den = fieldExponentiation(x_prime, BigInteger.valueOf(15), G1_CONST_P);;


        // x_num loop
        for (int i = G1_ISO_K[0].length - 1; i > 1 ; i--) {
            x_num = x_num.add(fieldMultiplication(G1_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        x_num = x_num.add(G1_ISO_K[0][0]);
        // x_den loop
        for (int i = G1_ISO_K[1].length - 1; i > 1 ; i--) {
            x_den = x_den.add(fieldMultiplication(G1_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        x_den = x_den.add(G1_ISO_K[1][0]);
        // y_num loop
        for (int i = G1_ISO_K[2].length - 1; i > 1 ; i--) {
            y_num = y_num.add(fieldMultiplication(G1_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        y_num = y_num.add(G1_ISO_K[2][0]);
        // y_den loop
        for (int i = G1_ISO_K[3].length - 1; i > 1 ; i--) {
            y_den = y_den.add(fieldMultiplication(G1_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        y_den = y_den.add(G1_ISO_K[3][0]);


        BigInteger x = fieldInversion(x_num, x_den); // Division currently using field inversion
        BigInteger y = fieldInversion(fieldMultiplication(y_prime, y_num, EMBEDDING_DEGREE), y_den);

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

        BigInteger x_prime = point.x;
        BigInteger y_prime = point.y;
        BigInteger x_num = BigInteger.ZERO;
        BigInteger x_den = fieldExponentiation(x_prime, BigInteger.valueOf(2), G1_CONST_P);
        BigInteger y_num = BigInteger.ZERO;;
        BigInteger y_den = fieldExponentiation(x_prime, BigInteger.valueOf(3), G1_CONST_P);;


        // x_num loop
        for (int i = G2_ISO_K[0].length - 1; i > 1 ; i--) {
            x_num = x_num.add(fieldMultiplication(G2_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        x_num = x_num.add(G2_ISO_K[0][0]);
        // x_den loop
        for (int i = G2_ISO_K[1].length - 1; i > 1 ; i--) {
            x_den = x_den.add(fieldMultiplication(G2_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        x_den = x_den.add(G2_ISO_K[1][0]);
        // y_num loop
        for (int i = G2_ISO_K[2].length - 1; i > 1 ; i--) {
            y_num = y_num.add(fieldMultiplication(G2_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        y_num = y_num.add(G2_ISO_K[2][0]);
        // y_den loop
        for (int i = G2_ISO_K[3].length - 1; i > 1 ; i--) {
            y_den = y_den.add(fieldMultiplication(G2_ISO_K[0][i], fieldExponentiation(x_prime, BigInteger.valueOf(i), G1_CONST_P), G1_CONST_P));
        }
        y_den = y_den.add(G2_ISO_K[3][0]);


        BigInteger x = fieldInversion(x_num, x_den); // Division currently using field inversion
        BigInteger y = fieldInversion(fieldMultiplication(y_prime, y_num, EMBEDDING_DEGREE), y_den);

        return new Point(x, y);

    }

    public static Point pairing(Point point_on_G1, Point point_on_G2) {

        // Let c = t for a parameter t and c_0, c_1, ... , c_L in {-1,0,1}
        // such that the sum of c_i * 2^i (i = 0, 1, ..., L) equals c.

        //The following algorithm shows the computation of the optimal Ate pairing
        // on Barreto-Lynn-Scott curves. It takes P in G_1, Q in G_2, an integer c, c_0, ...,c_L in {-1,0,1}
        // such that the sum of c_i * 2^i (i = 0, 1, ..., L) equals c, and the order r of G_1 as input, and outputs e(P, Q).

        Point P = point_on_G1;
        Point Q = point_on_G2;
        //int[] c = {-1, 0, 1}; // an integer c, c_0, ...,c_L in {-1,0,1} such that the sum of c_i * 2^i (i = 0, 1, ..., L) equals c
        BigInteger k = EMBEDDING_DEGREE;
        BigInteger t = G1_CONST_P;
        BigInteger p = G1_CONST_P;
        byte[] c = t.toByteArray();

        Point f = G1_GENERATOR_POINT;
        Point T = Q;
        if (c[BLS_L_VALUE.intValue()] == -1) {
            T = pointInversion(T);
        }
        for (int i = BLS_L_VALUE.intValue()-1 ; i>0 ; i--){
            f = montgomeryLadder(line_function(T, T, P), pointDouble(f));
            T = pointAddition(T, T);
            if (c[i] == 1) {
                f = montgomeryLadder(line_function(T, Q, P), f);
                T = pointAddition(T, Q);
            } else if (c[i] == -1) {
                f = montgomeryLadder(line_function(T, pointInversion(Q), P), f);
                T = pointSubtraction(T, Q);
            }
        }
        //f = f^{(p^k - 1) / r};
        f = montgomeryLadder(fieldInversion(fieldSubtraction(fieldExponentiation(p, k, EMBEDDING_DEGREE), BigInteger.valueOf(1), EMBEDDING_DEGREE), G1_CONST_M), f);
        return f;

    }

    public static BigInteger line_function(Point q1, Point q2, Point p) {
        //The p-power Frobenius endomorphism pi for a point Q = (x, y) over E' is pi(p, Q) = (x^p, y^p).
        //        if (Q_1 = Q_2) then
        //        l := (3 * x_1^2) / (2 * y_1);
        //    else if (Q_1 = -Q_2) then
        //        return x - x_1;
        //    else
        //        l := (y_2 - y_1) / (x_2 - x_1);
        //        end if;
        //        return (l * (x - x_1) + y_1 - y);
        BigInteger l;
        if (q1 == q2) {
            l = (fieldInversion
                    (fieldMultiplication
                            (BigInteger.valueOf(3),
                            fieldExponentiation(
                                    q1.x,
                                    BigInteger.valueOf(2),
                                    EMBEDDING_DEGREE),
                            EMBEDDING_DEGREE),
                    (fieldMultiplication(
                            BigInteger.valueOf(2),
                            q1.y,
                            EMBEDDING_DEGREE))));

        } else if (q1 == pointInversion(q2)) {
            return fieldSubtraction(p.x, q1.x, EMBEDDING_DEGREE);
        }
        else {
            l = fieldInversion(fieldSubtraction(q2.y, q1.y, EMBEDDING_DEGREE), fieldSubtraction(q2.x, q1.x, EMBEDDING_DEGREE));

        }
        BigInteger out = fieldAddition(
                fieldMultiplication(
                        fieldSubtraction(p.x, q1.x, EMBEDDING_DEGREE),
                        l,
                        EMBEDDING_DEGREE),
                fieldSubtraction(q1.y, p.y, EMBEDDING_DEGREE),
                EMBEDDING_DEGREE);
        return out;
    }



}


