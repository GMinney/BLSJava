package org.thoughtj.bls.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.arith.Conversion;
import org.thoughtj.bls.arith.Point;
import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.utils.ByteVector;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;

public class GTElement {

    // A GTElement is a public key that is 48 bytes in size or 384 bits
    // GT: a subgroup, of prime order r, of the multiplicative group of a field extension.
    // e : G1 x G2 -> GT: a non-degenerate bilinear map. e: the optimal Ate pairing
    // Operations in GT are written in multiplicative notation, i.e., a * b is field multiplication.
    // G1, G2: subgroups of E1 and E2 (respectively) having prime order r
    // E1, E2: elliptic curve groups defined over finite fields. This document assumes that E1 has a more compact representation than E2, i.e., because E1 is defined over a smaller field than E2.

    // Members

    public final static int SIZE = 384;
    private static Logger log = LoggerFactory.getLogger(GTElement.class);
    private BigInteger gt_element;
    private BigInteger r_value;


    // Constructors

    private GTElement() {

    }

    private GTElement(byte[] element) {
        this.gt_element = new BigInteger(element);
    }


    // Public Methods and Functions

    public static GTElement fromBytes(byte[] bytes) {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        return new GTElement(bytes);
    }

    public static GTElement fromBytesUnchecked(byte[] bytes) {
        return new GTElement(bytes);
    }

    public static GTElement fromNative(GTElement element) {
        GTElement new_element = new GTElement();
        new_element.r_value = element.r_value;
        return new_element;
    }

    private static GTElement fromByteVector(ByteVector byte_vector) {
        List<Short> new_list = byte_vector.stream().toList();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[new_list.size()*2]);
        for (int i = 0; i < new_list.size(); i++) {
            buffer.putShort(new_list.get(i));
        }
        return GTElement.fromBytes(buffer.array());
    }

    public GTElement unity() {
        // Set GTElement to the identity element
        this.gt_element = BigInteger.ZERO;
        return this;
    }

    public void serialize(byte[] buffer) {
        Point curve_point = Conversion.octetToCurvePointG2(buffer, true);
        byte[] serialized = Conversion.curvePointToOctetG2(curve_point);
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG1(this.gt_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
    }

}
