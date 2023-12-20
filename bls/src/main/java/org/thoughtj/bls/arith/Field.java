package org.thoughtj.bls.arith;

import java.math.BigInteger;

public class Field {

    /**
     * fieldElementInversion is a field arithmetic operation that finds the
     * modular multiplicative inverse using the Extended Euclidean Algorithm. <br>
     * As it turns out, BigInteger.ModInverse uses the Euclidean Extended Algorithm already, so we will use this instead
     * Algorithm - given an element 'a' in field 'F', find the multiplicative inverse 'a^-1' such that (a * a^-1 = 1 (mod p))
     * @param a a coprime value to m
     * @param m a coprime value to a
     * @return BigInteger - the multiplicative inverse
     */
    public static BigInteger fieldInversion(BigInteger a, BigInteger m){
        // Find coefficients x and y such that ax + my = 1
        //BigInteger[] result = extendedEuclidean(a, m);
        // The modular inverse is the x coefficient (handle negative values)
        //return result[0].compareTo(BigInteger.ZERO) < 0 ? result[0].add(m) : result[0];
        return a.modInverse(m);

    }

    /**
     * Field Negation that performs Modular Negation
     * @param a
     * @param p
     * @return
     */
    public static BigInteger fieldNegation(BigInteger a, BigInteger p) {
        // Calculate the additive inverse (-a) modulo p
        return p.subtract(a).mod(p);
    }

    /**
     * Field Addition that performs Modular Addition
     * @param a
     * @param b
     * @param p
     * @return
     */
    public static BigInteger fieldAddition(BigInteger a, BigInteger b, BigInteger p) {
        // Perform the addition and take the result modulo p
        return a.add(b).mod(p);
    }

    /**
     * Field Subtraction that performs Modular Subtraction
     * @param a
     * @param b
     * @param p
     * @return
     */
    public static BigInteger fieldSubtraction(BigInteger a, BigInteger b, BigInteger p) {
        // Perform the subtraction and take the result modulo p
        return a.subtract(b).add(p).mod(p);
    }

    /**
     * Field Multiplication that performs Modular Multiplication
     * @param a
     * @param b
     * @param p
     * @return
     */
    public static BigInteger fieldMultiplication(BigInteger a, BigInteger b, BigInteger p) {
        // Perform the multiplication and take the result modulo p
        return a.multiply(b).mod(p);
    }

    /**
     * Field Exponentiation that performs Modular Exponentiation
     * @param base
     * @param exponent
     * @param p
     * @return
     */
    public static BigInteger fieldExponentiation(BigInteger base, BigInteger exponent, BigInteger p) {
        // Perform the exponentiation and take the result modulo p
        return base.modPow(exponent, p);
    }

    /**
     * Convert a Weierstrass point (x, y) to a Montgomery point (u, v).
     * @param x
     * @param y
     * @param a
     * @param p
     * @return
     */
    public static BigInteger[] fromWeierstrassToMontgomery(BigInteger x, BigInteger y, BigInteger a, BigInteger p) {
        // Calculate Montgomery u and v coordinates
        BigInteger u = x.multiply(y.modInverse(p)).mod(p);
        BigInteger v = (y.subtract(a.multiply(x.pow(2))).mod(p)).multiply(BigInteger.valueOf(3).modInverse(p)).mod(p);

        return new BigInteger[]{u, v};
    }

    /**
     * Convert a Montgomery point (u, v) to a Weierstrass point (x, y).
     * @param u
     * @param v
     * @param a
     * @param p
     * @return
     */
    public static BigInteger[] fromMontgomeryToWeierstrass(BigInteger u, BigInteger v, BigInteger a, BigInteger p) {
        // Calculate Weierstrass x and y coordinates
        BigInteger x = u.multiply(v.modInverse(p)).mod(p);
        BigInteger y = (v.add(a.multiply(u)).mod(p)).multiply(BigInteger.valueOf(2).modInverse(p)).mod(p);

        return new BigInteger[]{x, y};
    }

    /**
     * extendedEuclidean computes the Extended Euclidean Algorithm. <br>
     * Unused method since BigInteger.ModInverse uses the Euclidean Extended Algorithm already, so we will use this instead
     * @param a
     * @param b
     * @return returns a BigInteger array containing the computed x and y coordinate, Could also be returned through the Point type
     */
    protected static BigInteger[] extendedEuclidean(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{BigInteger.ONE, BigInteger.ZERO};
        } else {
            BigInteger[] temp = extendedEuclidean(b, a.mod(b));
            BigInteger x = temp[1];
            BigInteger y = temp[0].subtract(a.divide(b).multiply(temp[1]));
            return new BigInteger[]{x, y};
        }
    }

    /**
     * Square root field arithmetic using the Tonelli–Shanks algorithm.
     * @param a
     * @param p
     * @return
     */
    private static BigInteger fieldSquareRoot (BigInteger a, BigInteger p) {
        // Check if 'a' is a quadratic residue (has a square root)
        if (!isQuadraticResidue(a, p)) {
            throw new IllegalArgumentException("No square root exists for " + a + " modulo " + p);
        }

        // Special case for p = 2
        if (p.equals(BigInteger.valueOf(2))) {
            return a.mod(p);
        }

        // Tonelli–Shanks algorithm
        if (p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            // Case p ≡ 3 (mod 4)
            return a.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
        } else {
            // Case p ≡ 1 (mod 4)
            BigInteger s = p.subtract(BigInteger.ONE);

            return getT(a, p, s);
        }
    }

    /**
     * Extension of the Tonelli–Shanks algorithm that is called from performing square root field arithmetic.
     * @param a
     * @param p
     * @param s
     * @return
     */
    private static BigInteger getT(BigInteger a, BigInteger p, BigInteger s) {
        BigInteger q = s;
        int r = 0;
        while (q.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            q = q.divide(BigInteger.valueOf(2));
            r += 1;
        }

        BigInteger z = BigInteger.valueOf(2);
        while (isQuadraticResidue(z, p)) {
            z = z.add(BigInteger.ONE);
        }

        BigInteger c = z.modPow(q, p);
        BigInteger t = a.modPow(q, p);
        BigInteger m = BigInteger.valueOf(r);

        while (t.compareTo(BigInteger.ONE) > 0) {
            int i = 0;
            BigInteger temp = t;
            while (!temp.equals(BigInteger.ONE)) {
                temp = temp.modPow(BigInteger.valueOf(2), p);
                i += 1;
            }

            BigInteger b = c.modPow(BigInteger.valueOf(2).pow(m.subtract(BigInteger.valueOf(i - 1)).intValue()), p);
            c = b.modPow(BigInteger.valueOf(2), p);
            t = t.multiply(c).mod(p);
            m = BigInteger.valueOf(i);
        }
        return t;
    }


    /**
     * isQuadraticResidue is a boolean that uses Euler's criterion for quadratic residues.
     * @param a
     * @param p
     * @return
     */
    private static boolean isQuadraticResidue(BigInteger a, BigInteger p) {
        // Using Euler's criterion for quadratic residues
        return a.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)), p).equals(BigInteger.ONE);
    }


}
