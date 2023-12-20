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
    


    /**
     * Conversion function from a field element (as a BigInteger) to an octet string (sequence of octets, groups of 8 bits)(byte[])
     * @param element Field element as a BigInteger
     * @return An octet string / byte array (sequence of octets, groups of 8 bits)
     */
    public static byte[] fieldElementToOctet (BigInteger element) {
        return element.toByteArray();
    }

    /**
     * Conversion function from an octet string (sequence of octets, groups of 8 bits)(as a byte[]) to a field element (BigInteger)
     * @param octets (sequence of octets, groups of 8 bits)
     * @return Field element as a BigInteger
     */
    public static BigInteger octetToFieldElement (byte[] octets) {
        return new BigInteger(octets);
    }

    /**
     * Conversion function from a curve point (as a Point) to an octet string (sequence of octets, groups of 8 bits)(byte[])
     * This operation is known <strong>Deserialization</strong>
     * @see Point
     * @param point Curve point as Point type, containing an x and y coordinate as BigIntegers
     * @return the canonical representation of the point P as an octet string. An octet string / byte array (sequence of octets, groups of 8 bits)
     */
    public static byte[] curvePointToOctetG1 (Point point) {
        //Check if compressed form
        
        // Perform conversion
        byte[] point_bytes = new byte[point.x.toByteArray().length + point.y.toByteArray().length];
        System.arraycopy(point.x.toByteArray(),0,point_bytes,0, point.x.toByteArray().length);
        System.arraycopy(point.y.toByteArray(),0,point_bytes, point.x.toByteArray().length, point.y.toByteArray().length);
        return point_bytes;
    }

    /**
     * Conversion function from an octet string (sequence of octets, groups of 8 bits)(as a byte[]) to a curve point (Point)
     * This operation is known <strong>Serialization</strong>
     * @param octets (sequence of octets, groups of 8 bits)
     * @param compressed Whether the resulting point should be in compressed form
     * @return Curve point as Point type, containing an x and y coordinate as BigIntegers
     */
    public static Point octetToCurvePointG1 (byte[] octets, boolean compressed) {
        //Check for identity Point
        if (octets[0] == 0x00){
            return Params.POINT_AT_INFINITY;
        }
        //Check if compressed form
        // TODO: Needs work, check https://www.secg.org/sec1-v2.pdf
        if (compressed) {
            BigInteger compressed_y = BigInteger.valueOf(octets[0]);
            BigInteger x = new BigInteger(Arrays.copyOfRange(octets, 1, octets.length));
        }
        else {

        }
    }

    /**
     * Conversion function from a curve point (as a Point) to an octet string (sequence of octets, groups of 8 bits)(byte[])
     * This operation is known <strong>Deserialization</strong>
     * @see Point
     * @param point Curve point as Point type, containing an x and y coordinate as BigIntegers
     * @return the canonical representation of the point P as an octet string. An octet string / byte array (sequence of octets, groups of 8 bits)
     */
    public static byte[] curvePointToOctetG2 (Point point) {
        //Check if compressed form

        // Perform conversion
        byte[] point_bytes = new byte[point.x.toByteArray().length + point.y.toByteArray().length];
        System.arraycopy(point.x.toByteArray(),0,point_bytes,0, point.x.toByteArray().length);
        System.arraycopy(point.y.toByteArray(),0,point_bytes, point.x.toByteArray().length, point.y.toByteArray().length);
        return point_bytes;
    }

    /**
     * Conversion function from an octet string (sequence of octets, groups of 8 bits)(as a byte[]) to a curve point (Point)
     * This operation is known <strong>Serialization</strong>
     * @param octets (sequence of octets, groups of 8 bits)
     * @param compressed Whether the resulting point should be in compressed form
     * @return Curve point as Point type, containing an x and y coordinate as BigIntegers
     */
    public static Point octetToCurvePointG2 (byte[] octets, boolean compressed) {
        //Check for identity Point
        if (octets[0] == 0x00){
            return Params.POINT_AT_INFINITY;
        }
        //Check if compressed form
        // TODO: Needs work, check https://www.secg.org/sec1-v2.pdf
        if (compressed) {
            BigInteger compressed_y = BigInteger.valueOf(octets[0]);
            BigInteger x = new BigInteger(Arrays.copyOfRange(octets, 1, octets.length));
        }
        else {

        }
    }

    /**
     * Conversion function from a bit string (sequence of bits, zero or more bits)(as a String) to an octet string (sequence of octets, groups of 8 bits) (byte[])
     * @param bitString (sequence of bits, zero or more bits)(as a hex encoded String)
     * @return An octet string / byte array (sequence of octets, groups of 8 bits)
     */
    public static byte[] bitStringToOctet (String bitString) {
        return new BigInteger(bitString, 16).toByteArray();
    }

    /**
     * Conversion function from an octet string (sequence of octets, groups of 8 bits)(as a byte[]) to a bit string (String)
     * @param octets (sequence of octets, groups of 8 bits)
     * @return a String
     */
    public static String octetToBitString (byte[] octets) {
        return new BigInteger(octets).toString(16);
    }

    /**
     * I2OSP<br>
     * Conversion function from a nonnegative integer (as an int) to an octet (byte)<br>
     * Follows the convention of I2OSP (Integer to Octet String Primitive)<br>
     * @see <a href="https://www.rfc-editor.org/rfc/pdfrfc/rfc8017.txt.pdf">RFC8017</a>
     * @param x nonnegative integer to be converted
     * @param size intended length of the resulting octet string
     * @return corresponding octet string of length xLen
     */
    public static byte integerToOctet (int x, int size) {
        return (byte)x;
        /*
            1. If x >= 256^xLen, output "integer too large" and stop.

            2. Write the integer x in its unique xLen-digit representation in base 256:

                    x = x_(xLen-1) 256^(xLen-1) + x_(xLen-2) 256^(xLen-2) + ...
                    + x_1 256 + x_0,

                where 0 <= x_i < 256 (note that one or more leading digits
                will be zero if x is less than 256^(xLen-1)).

             3. Let the octet X_i have the integer value x_(xLen-i) for 1 <= i
                <= xLen. Output the octet string

                    X = X_1 X_2 ... X_xLen.

         */

    }

    /**
     * OS2IP <br>
     * Conversion function from an octet (as a byte) to a nonnegative integer (int)<br>
     * Follows the convention of OS2IP (Octet String to Integer Primitive)<br>
     * @see <a href="https://www.rfc-editor.org/rfc/pdfrfc/rfc8017.txt.pdf">RFC8017</a>
     * @param octetString X octet string to be converted, (sequence of octets, groups of 8 bits)
     * @return x corresponding nonnegative integer
     */
    public static int octetToInteger (byte[] octetString) {
        return octetString;

        /*
        Steps:
             1. Let X_1 X_2 ... X_xLen be the octets of X from first to last,
             and let x_(xLen-i) be the integer value of the octet X_i for 1 <= i <= xLen.

             2. Let x = x_(xLen-1) 256^(xLen-1) + x_(xLen-2) 256^(xLen-2) + ... + x_1 256 + x_0.

             3. Output x.

         */

    }

    public static boolean subgroup_check_E1 (Point point) {
        // checking that a point P exists within G1
        // using the LLL algorithm as in Fuentes et al.[5] and Budroni and Pintore[3].  https://eprint.iacr.org/2017/419
        // This can be computed using a single scalar multiplication
        // by z which is roughly a fourth of the size of q, and some other trivial group operations.
    }

    public static boolean subgroup_check_E2 (Point point) {

    }

}