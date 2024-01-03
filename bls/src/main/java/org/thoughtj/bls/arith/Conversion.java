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
    public static BigInteger octetToFieldElement (byte octet) {
        return new BigInteger(String.valueOf(octet));
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
     * This operation is known <strong>Serialization</strong>
     * @param octets (sequence of octets, groups of 8 bits)
     * @param compressed Whether the resulting point should be in compressed form
     * @return Curve point as Point type, containing an x and y coordinate as BigIntegers
     */
    public static Point octetToCurvePointG1 (byte[] octets, boolean compressed) {
        // TODO: Needs work, check https://www.secg.org/sec1-v2.pdf

        //Check for identity Point
        if (octets[0] == 0x00) {
            return Params.POINT_AT_INFINITY;
        }
        // Length check
        if (octets.length != Params.SIGNATURE_LENGTH + 1) {
            return null;
        }
        // Instantiate vars
        byte y_octet = octets[0];
        byte[] x_octets = Arrays.copyOfRange(octets, 1, Params.SIGNATURE_LENGTH + 1);
        BigInteger x_element = octetToFieldElement(x_octets); // output invalid if invalid and stop
        BigInteger a = Params.G1_CONST_A;
        BigInteger b = Params.G1_CONST_B;
        BigInteger q = BigInteger.valueOf(octets.length * 8); // q is the bit depth of the octets

        if (y_octet == 0x02 || y_octet == 0x03) {

            BigInteger y_element = octetToFieldElement(BigInteger.valueOf(0).byteValueExact()); // set field element y_element to identity element to start
            BigInteger y_out = null;

            // if y_octet is 0x03, set y_element to value of 1
            if (y_octet == 0x03) {
                y_element = octetToFieldElement(BigInteger.valueOf(1).byteValueExact());
            }

            // check if odd prime
            if (q.mod(BigInteger.valueOf(2)) == BigInteger.valueOf(1)) {
                // compute square root
                BigInteger root = Field.fieldSquareRoot(y_element, Params.EMBEDDING_DEGREE);
                // if root null, Invalid
                if (root == null) {
                    return null;
                }
                // if root even, y_out is root
                if (root.mod(BigInteger.valueOf(2)).equals(root)) {
                    y_out = root;
                } else { // otherwise compute additive inverse for root with characteristic p
                    y_out = Field.fieldSubtraction(Params.BLS_CONST_P, root, BigInteger.valueOf(2));
                }
            }
            // q == 2^m
            boolean b1 = q.intValueExact() == (2 ^ Params.BLS_M_VALUE.intValueExact());

            // if b1 and x_element == 0, output b^2^m-1
            if (b1 && (x_element.equals(BigInteger.valueOf(0)))) {
                y_out = Field.fieldExponentiation(b, BigInteger.valueOf(2 ^ (Params.BLS_M_VALUE.intValueExact() - 1)), Params.EMBEDDING_DEGREE);
            }
            // if b1 and x_element != 0
            if (b1 && (!x_element.equals(BigInteger.valueOf(0)))) {
                // compute beta
                BigInteger beta = Field.fieldAddition(
                        Field.fieldAddition(
                                x_element, Params.G1_CONST_A, Params.EMBEDDING_DEGREE),
                        Field.fieldMultiplication(
                                Params.G1_CONST_B,
                                Field.fieldSquareRoot(x_element, Params.EMBEDDING_DEGREE), // not square root, reciprical
                                Params.EMBEDDING_DEGREE),
                        Params.EMBEDDING_DEGREE);
                // compute z
                BigInteger z = Field.fieldDivision(BigInteger.valueOf(1), Field.fieldMultiplication(x_element, y_element, Params.EMBEDDING_DEGREE));
                // if no z exists such that z^2 + z = beta, return invalid
                if (!Field.fieldAddition(Field.fieldExponentiation(z, BigInteger.valueOf(2), Params.EMBEDDING_DEGREE), z, Params.EMBEDDING_DEGREE).equals(beta)){
                    return null;
                }
                // if z[0] == y_element
                if (x_octets[0] == y_element.byteValueExact()) {
                    y_out = Field.fieldMultiplication(x_element, z, Params.EMBEDDING_DEGREE);
                } else {
                    // otherwise
                    y_out = Field.fieldMultiplication(x_element , Field.fieldAddition(z, BigInteger.valueOf(1), Params.EMBEDDING_DEGREE), Params.EMBEDDING_DEGREE);
                }
            }
            // null check
            if (y_out == null) {
                return null;
            }
            // Finally
            return new Point(x_element, y_out);

        }
        return null; // Invalid
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

        if (octets.length == (2*Params.SIGNATURE_LENGTH+1)) {
            if (octets[0] == 0x04){
                // convert x to field element
                BigInteger x_value = octetToFieldElement(Arrays.copyOfRange(octets,1,Params.SIGNATURE_LENGTH+1)); // output invalid if invalid and stop
                // convert y to field element
                BigInteger y_value = octetToFieldElement(Arrays.copyOfRange(octets,Params.SIGNATURE_LENGTH+1,octets.length)); // output invalid if invalid and stop
                // check if point satisfys curve equation
                // output point
                Point curve_point = new Point(x_value, y_value);
            }
            else {
                return null; // Output invalid and stop
            }
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
