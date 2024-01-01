package org.thoughtj.bls.arith;

import java.math.BigInteger;
import java.util.Arrays;

public abstract class Conversion {

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

        if (octets.length == 97) {
            byte y_value = octets[0];
            BigInteger y_octet;
            if (y_value == 0x02){
                y_octet = BigInteger.valueOf(0); // Maybe a problem with the point at infinity
            }
            else if (y_value == 0x03) {
                y_octet = BigInteger.valueOf(1);
            }
            else {
                return null;
            }
            BigInteger x_octets = octetToFieldElement(Arrays.copyOfRange(octets,1,97)); // output invalid if invalid and stop
            // Derive point

        }

        // TODO: Needs work, check https://www.secg.org/sec1-v2.pdf
        return null;
    }

    /**
     * Conversion function from a curve point (as a Point) to an octet string (sequence of octets, groups of 8 bits)(byte[])
     * This operation is known as <strong>Deserialization</strong>
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
     * This operation is known as <strong>Serialization</strong>
     * @param octets (sequence of octets, groups of 8 bits)
     * @param compressed Whether the resulting point should be in compressed form
     * @return Curve point as Point type, containing an x and y coordinate as BigIntegers
     */
    public static Point octetToCurvePointG2 (byte[] octets, boolean compressed) {
        //Check for identity Point
        if (octets[0] == 0x00){
            return Params.POINT_AT_INFINITY;
        }

        if (octets.length == 97) {
            byte y_value = octets[0];
            BigInteger y_octet;
            if (y_value == 0x02){
                y_octet = BigInteger.valueOf(0); // Maybe a problem with the point at infinity
            }
            else if (y_value == 0x03) {
                y_octet = BigInteger.valueOf(1);
            }
            else {
                return null;
            }
            BigInteger x_octets = octetToFieldElement(Arrays.copyOfRange(octets,1,97)); // output invalid if invalid and stop
            // Derive point

        }

        // TODO: Needs work, check https://www.secg.org/sec1-v2.pdf
        return null;
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
     * Conversion function from a nonnegative integer (as a BigInt) to an octet (byte)<br>
     * Follows the convention of I2OSP (Integer to Octet String Primitive)<br>
     * Code from BouncyCastle
     * @see <a href="https://www.rfc-editor.org/rfc/pdfrfc/rfc8017.txt.pdf">RFC8017</a>
     * @param val nonnegative integer to be converted
     * @return corresponding octet string of length xLen
     */

    public static byte[] integerToOctets(int val, int xLen)
    {
        byte[] valBytes = new byte[]{((byte) val)};
        System.arraycopy(valBytes, 0, valBytes, 0, xLen);
        return valBytes;
    }
    /**
     * OS2IP <br>
     * Conversion function from an octet (as a byte) to a nonnegative integer (int)<br>
     * Follows the convention of OS2IP (Octet String to Integer Primitive)<br>
     * Code from BouncyCastle
     * @see <a href="https://www.rfc-editor.org/rfc/pdfrfc/rfc8017.txt.pdf">RFC8017</a>
     * @param data X octet string to be converted, (sequence of octets, groups of 8 bits)
     * @param offset offset to octet string
     * @param length length of octet string
     * @return x corresponding nonnegative integer
     */
    public static BigInteger octetsToInteger(byte[] data, int offset,
                                             int length)
    {
        byte[] val = new byte[length + 1];

        val[0] = 0;
        System.arraycopy(data, offset, val, 1, length);
        return new BigInteger(val);
    }
    /**
     * OS2IP <br>
     * Conversion function from an octet (as a byte) to a nonnegative integer (int)<br>
     * Follows the convention of OS2IP (Octet String to Integer Primitive)<br>
     * Code from BouncyCastle
     * @see <a href="https://www.rfc-editor.org/rfc/pdfrfc/rfc8017.txt.pdf">RFC8017</a>
     * @param data X octet string to be converted, (sequence of octets, groups of 8 bits)
     * @return x corresponding nonnegative integer
     */
    public static BigInteger octetsToInteger(byte[] data)
    {
        return octetsToInteger(data, 0, data.length);
    }

}
