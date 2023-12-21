package org.thoughtj.bls.arith;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Field {

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

    /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     */
    public static BigDecimal ln(BigDecimal x, int scale)
    {
        // Check that x > 0.
        if (x.signum() <= 0) {
            throw new IllegalArgumentException("x <= 0");
        }

        // The number of digits to the left of the decimal point.
        int magnitude = x.toString().length() - x.scale() - 1;

        if (magnitude < 3) {
            return lnNewton(x, scale);
        }

        // Compute magnitude*ln(x^(1/magnitude)).
        else {
            // x^(1/magnitude)
            BigDecimal root = intRoot(x, magnitude, scale);

            // ln(x^(1/magnitude))
            BigDecimal lnRoot = lnNewton(root, scale);

            // magnitude*ln(x^(1/magnitude))
            return BigDecimal.valueOf(magnitude).multiply(lnRoot).setScale(scale, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     * Use Newton's algorithm.
     */
    private static BigDecimal lnNewton(BigDecimal x, int scale)
    {
        int        sp1 = scale + 1;
        BigDecimal n   = x;
        BigDecimal term;

        // Convergence tolerance = 5*(10^-(scale+1))
        BigDecimal tolerance = BigDecimal.valueOf(5).movePointLeft(sp1);

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {
            // e^x
            BigDecimal eToX = exp(x, sp1);

            // (e^x - n)/e^x
            term = eToX.subtract(n).divide(eToX, sp1, RoundingMode.DOWN);

            // x - (e^x - n)/e^x
            x = x.subtract(term);

            Thread.yield();
        } while (term.compareTo(tolerance) > 0);

        return x.setScale(scale, RoundingMode.HALF_EVEN);
    }

    /**
     * Compute the integral root of x to a given scale, x >= 0.
     * Use Newton's algorithm.
     * @param x the value of x
     * @param index the integral root value
     * @param scale the desired scale of the result
     * @return the result value
     */
    public static BigDecimal intRoot(BigDecimal x, long index, int scale)
    {
        // Check that x >= 0.
        if (x.signum() < 0) {
            throw new IllegalArgumentException("x < 0");
        }

        int        sp1 = scale + 1;
        BigDecimal n   = x;
        BigDecimal i   = BigDecimal.valueOf(index);
        BigDecimal im1 = BigDecimal.valueOf(index-1);
        BigDecimal tolerance = BigDecimal.valueOf(5).movePointLeft(sp1);
        BigDecimal xPrev;

        // The initial approximation is x/index.
        x = x.divide(i, scale, RoundingMode.HALF_EVEN);

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            // x^(index-1)
            BigDecimal xToIm1 = intPower(x, index-1, sp1);


            // x^index
            BigDecimal xToI = x.multiply(xToIm1).setScale(sp1, RoundingMode.HALF_EVEN);

            // n + (index-1)*(x^index)
            BigDecimal numerator = n.add(im1.multiply(xToI)).setScale(sp1, RoundingMode.HALF_EVEN);

            // (index*(x^(index-1))
            BigDecimal denominator = i.multiply(xToIm1).setScale(sp1, RoundingMode.HALF_EVEN);

            // x = (n + (index-1)*(x^index)) / (index*(x^(index-1)))
            xPrev = x;
            x = numerator.divide(denominator, sp1, RoundingMode.DOWN);

            Thread.yield();
        } while (x.subtract(xPrev).abs().compareTo(tolerance) > 0);

        return x;
    }

    /**
     * Compute e^x to a given scale.
     * Break x into its whole and fraction parts and
     * compute (e^(1 + fraction/whole))^whole using Taylor's formula.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    public static BigDecimal exp(BigDecimal x, int scale)
    {
        // e^0 = 1
        if (x.signum() == 0) {
            return BigDecimal.valueOf(1);
        }

        // If x is negative, return 1/(e^-x).
        else if (x.signum() == -1) {
            return BigDecimal.valueOf(1).divide(exp(x.negate(), scale), scale, RoundingMode.HALF_EVEN);
        }

        // Compute the whole part of x.
        BigDecimal xWhole = x.setScale(0, RoundingMode.DOWN);

        // If there isn't a whole part, compute and return e^x.
        if (xWhole.signum() == 0) return expTaylor(x, scale);

        // Compute the fraction part of x.
        BigDecimal xFraction = x.subtract(xWhole);

        // z = 1 + fraction/whole
        BigDecimal z = BigDecimal.valueOf(1).add(xFraction.divide(xWhole, scale, RoundingMode.HALF_EVEN));

        // t = e^z
        BigDecimal t = expTaylor(z, scale);

        BigDecimal maxLong = BigDecimal.valueOf(Long.MAX_VALUE);
        BigDecimal result  = BigDecimal.valueOf(1);

        // Compute and return t^whole using intPower().
        // If whole > Long.MAX_VALUE, then first compute products
        // of e^Long.MAX_VALUE.
        while (xWhole.compareTo(maxLong) >= 0) {
            result = result.multiply(intPower(t, Long.MAX_VALUE, scale)).setScale(scale, RoundingMode.HALF_EVEN);
            xWhole = xWhole.subtract(maxLong);

            Thread.yield();
        }
        return result.multiply(intPower(t, xWhole.longValue(), scale)).setScale(scale, RoundingMode.HALF_EVEN);
    }

    /**
     * Compute x^exponent to a given scale. Uses the same algorithm as class
     * numbercruncher.mathutils.IntPower.
     *
     * @param x the value x
     * @param exponent the exponent value
     * @param scale the desired scale of the result
     * @return the result value
     */
    public static BigDecimal intPower(BigDecimal x, long exponent, int scale) {
        // If the exponent is negative, compute 1/(x^-exponent).
        if (exponent < 0) {
            return BigDecimal.valueOf(1).divide(intPower(x, -exponent, scale), scale, RoundingMode.HALF_EVEN);
        }

        BigDecimal power = BigDecimal.valueOf(1);

        // Loop to compute value^exponent.
        while (exponent > 0) {

            // Is the rightmost bit a 1?
            if ((exponent & 1) == 1) {
                power = power.multiply(x).setScale(scale, RoundingMode.HALF_EVEN);
            }

            // Square x and shift exponent 1 bit to the right.
            x = x.multiply(x).setScale(scale, RoundingMode.HALF_EVEN);
            exponent >>= 1;

            Thread.yield();
        }

        return power;
    }

    /**
     * Compute e^x to a given scale by the Taylor series.
     *
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    private static BigDecimal expTaylor(BigDecimal x, int scale) {
        BigDecimal factorial = BigDecimal.valueOf(1);
        BigDecimal xPower = x;
        BigDecimal sumPrev;

        // 1 + x
        BigDecimal sum = x.add(BigDecimal.valueOf(1));

        // Loop until the sums converge
        // (two successive sums are equal after rounding).
        int i = 2;
        do {
            // x^i
            xPower = xPower.multiply(x).setScale(scale, RoundingMode.HALF_EVEN);

            // i!
            factorial = factorial.multiply(BigDecimal.valueOf(i));

            // x^i/i!
            BigDecimal term = xPower.divide(factorial, scale, RoundingMode.HALF_EVEN);

            // sum = sum + x^i/i!
            sumPrev = sum;
            sum = sum.add(term);

            ++i;
            Thread.yield();
        } while (sum.compareTo(sumPrev) != 0);

        return sum;
    }

}
