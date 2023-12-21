package org.thoughtj.bls.arith;

import java.math.BigInteger;
import java.util.Arrays;

// Point on the BLS12-381 curve

public class Point {
    BigInteger x;
    BigInteger y;

    /**
     * {@link Point} - Constructor for the Point type containing an x and y coordinate using the BigInteger type. <br>
     * Technically, this Point constructor is actually a representation for an Affine Point (a Point that is representative of its coordinates)<br>
     * <br>
     * A field element is a BigInteger<br>
     * An octet is an 8-bit byte, often many as a byte[] (sequence of octets, groups of 8 bits)<br>
     * A bitstring(sequence of bits, zero or more bits)(as a hex encoded String) <br>
     * A curve point is a Point with x and y as BigIntegers<br>
     * @see Point
     * @see <a href="https://www.secg.org/sec1-v2.pdf">Documentation</a>
     * @param x
     * @param y
     */
    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
    






}