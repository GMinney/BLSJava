package org.thoughtj.bls.arith;

import java.math.BigInteger;
import java.util.Arrays;

import static org.thoughtj.bls.arith.Curve.*;
import static org.thoughtj.bls.arith.Mapping.frobenius;
import static org.thoughtj.bls.arith.Params.*;

public abstract class Verify {

    public static boolean subgroup_check_E1 (Point point) {
        // where z is the parameter of the BLS12-381 elliptic curve construction.
        // z = −2^16 -2^48 -2^57 -2^60 -2^62 -2^63

        // πp be the p-power Frobenius on E,  let φ be the twisting isomorphism from E to Eprime
        // e “untwist-Frobenius-twist” endomorphism ψ = φ^−1πpφ
        // We propose to use endomorphisms to multiply the q-torsion components of P by q while not killing any h-torsion components of P

        // q is the group order
        // z which is roughly a fourth of the size of q,
        Point temp = pointDouble(frobenius(point)); // first order frobenius multiplied by 2 on P
        Point temp2 = frobenius(temp); // second order frobenius on P
        Point temp3 = pointSubtraction(pointSubtraction(pointSubtraction(temp, point), temp2), temp2);
        Point result = scalarMultiplication(BLS_CONST_P, temp3); //   "(z^2-1)/3"       scalar multiplied by      (2 endomorphism(P) - (P) - endomorphism^2(P)) - (endomorphism^2(P))
        return result == POINT_AT_INFINITY;
    }

    public static boolean subgroup_check_E2 (Point point) {
        // where z is the parameter of the BLS12-381 elliptic curve construction.
        // z = −2^16 -2^48 -2^57 -2^60 -2^62 -2^63

        // πp be the p-power Frobenius on E,  let φ be the twisting isomorphism from E to Eprime
        // e “untwist-Frobenius-twist” endomorphism ψ = φ^−1πpφ
        // We propose to use endomorphisms to multiply the q-torsion components of P by q while not killing any h-torsion components of P

        // q is the group order
        // z which is roughly a fourth of the size of q,
        Point temp = frobenius(frobenius(point)); // second order frobenius on P
        Point temp2 = frobenius(temp); // third order frobenius on P
        Point temp3 = pointAddition(pointSubtraction(temp2, temp), point);
        Point result = scalarMultiplication(BLS_CONST_P, temp3); // "z" scalar multiplied by (endomorphism^3(P)) - (endomorphism^2(P)) + (P)
        return result == POINT_AT_INFINITY;

    }

    public static int sgn0 (BigInteger element) {
        //        Parameters:
        //        - F, a finite field of characteristic p and order q = p^m.
        //        - p, the characteristic of F (see immediately above).
        //        - m, the extension degree of F, m >= 1 (see immediately above).
        //        Input: x, an element of F.
        //                Output: 0 or 1.
        //        Steps:
        //        1. sign = 0
        //        2. zero = 1
        //        3. for i in (1, 2, ..., m):
        //        4.   sign_i = x_i mod 2
        //        5.   zero_i = x_i == 0
        //        6.   sign = sign OR (zero AND sign_i) # Avoid short-circuit logic ops
        //        7.   zero = zero AND zero_i
        //        8. return sign


        //        int sign = 0;
        //        int zero = 1;
        //        for (int i = 0; i < m; i++) {
        //
        //        }
        return element.signum();
    }

    public static int sgn0_m_eq_1(BigInteger element){
        //    When m == 1, sgn0 can be significantly simplified:
        //        Input: x, an element of GF(p).
        //                Output: 0 or 1.
        //        Steps:
        //        1. return x mod 2
        return element.signum();
    }

    public static int sgn0_m_eq_2(BigInteger element){
        //    The case m == 2 is only slightly more complicated:
        //        Input: x, an element of GF(p^2).
        //                Output: 0 or 1.
        //        Steps:
        //        1. sign_0 = x_0 mod 2
        //        2. zero_0 = x_0 == 0
        //        3. sign_1 = x_1 mod 2
        //        4. s = sign_0 OR (zero_0 AND sign_1) # Avoid short-circuit logic ops
        //        5. return s
        return element.signum();
    }

    public static BigInteger inv0(BigInteger element){
//        inv0(x): This function returns the multiplicative inverse of x in F, extended to all of F by fixing inv0(0) == 0.
//        A straightforward way to implement inv0 in constant time is to compute
//
//        inv0(x) := x^(q - 2).
        // q is the order
        return element.modInverse(Params.BLS_CURVE_ORDER_R);
    }



    public static boolean is_square(BigInteger element){
//    is_square(x): This function returns True whenever the value x is a square in the field F. By Euler's criterion,
//    this function can be calculated in constant time as
//
//    is_square(x) := { True,  if x^((q - 1) / 2) is 0 or 1 in F;
//        { False, otherwise.
//
        BigInteger square = element.modPow(Params.BLS_CURVE_ORDER_R.subtract(BigInteger.valueOf(1)), Params.EMBEDDING_DEGREE);
        BigInteger zero = BigInteger.valueOf(0);
        BigInteger one = BigInteger.valueOf(1);
        return square.equals(zero) || square.equals(one);
    }

    public static byte[] substr(byte[] str, int sbegin, int slen){
//        substr(str, sbegin, slen): for a byte string str, this function returns the slen-byte substring starting at position sbegin;
//        positions are zero indexed. For example, substr("ABCDEFG", 2, 3) == "CDE".
        return Arrays.copyOfRange(str, sbegin, sbegin + slen);
    }

    public static byte[] strxor(byte[] str1, byte[] str2){
//      strxor(str1, str2): for byte strings str1 and str2, strxor(str1, str2) returns the bitwise XOR of the two strings. For example,
//      strxor("abc", "XYZ") == "9;9" (the strings in this example are ASCII literals, but strxor is defined for arbitrary byte strings).
//      In this document, strxor is only applied to inputs of equal length.

        if (str1.length == str2.length){
            BigInteger big1 = new BigInteger(str1);
            BigInteger big2 = new BigInteger(str2);
            BigInteger out = big1.xor(big2);
            return out.toByteArray();
        }
        else {
            return null;
        }
    }


// See https://datatracker.ietf.org/doc/html/draft-irtf-cfrg-hash-to-curve-16#name-the-sgn0-function for details

}
