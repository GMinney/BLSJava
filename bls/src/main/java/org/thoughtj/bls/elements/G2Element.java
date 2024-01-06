package org.thoughtj.bls.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.arith.*;
import org.thoughtj.bls.keys.PrivateKey;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class G2Element {

    // A G2Element is a signature that is 96 bytes in size or 768 bits in compressed form
    // A G2Element is a signature that is 192 bytes in size or 1536 bits in uncompressed form
    // Relic uses Minimum pubkey sizes and compression - check notes for details
    public final static int SIZE = 96;

    private static Logger log = LoggerFactory.getLogger(G2Element.class);
    private BigInteger g2_element;

    // Constructors
    public G2Element() {
        this.g2_element = new BigInteger(ByteBuffer.allocate(SIZE).array());
    }

    private G2Element(G2Element other) {
        this.g2_element = other.g2_element;
    }

    private G2Element(byte[] element) {
        this.g2_element = new BigInteger(element);
    }

    public static G2Element fromBytes(byte[] bytes, boolean fLegacy) {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        // TODO: fLegacy
        return new G2Element(bytes);
    }

    public static G2Element fromBytes(byte[] bytes) {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        return new G2Element(bytes);
    }

    public static G2Element fromBytesUnchecked(byte[] bytes, boolean fLegacy) {
        // TODO: fLegacy
        return new G2Element(bytes);
    }

    public static G2Element fromBytesUnchecked(byte[] bytes) {
        return new G2Element(bytes);
    }

    public static G2Element fromMessage(byte[] message, byte[] dst, int dst_len, boolean fLegacy) {
        // TODO: fLegacy
        return new G2Element(message);
    }

    public static G2Element fromMessage(byte[] message, byte[] dst, int dst_len) {
        return new G2Element(message);
    }


    // Functions and Methods
    public static G2Element generator() {
        // Get the generator element of the BLS12-381 Curve
        return new G2Element(Params.BLS_CONST_P.toByteArray());
    }

    public boolean isValid() {
        return CoreAPI.KeyValidate(this.serialize());
    }

    public void checkValid() {
        if (!isValid()){
            throw new RuntimeException("Not valid");
        }
    }

    public G2Element negate() {
        this.g2_element = Field.fieldNegation(this.g2_element, Params.BLS_CONST_P);
        return new G2Element(this.g2_element.toByteArray());
    }

    public GTElement pair(G1Element a) {
        return a.pair(this);
    }

    public byte[] serialize(boolean fLegacy) {
        Point curve_point = Conversion.octetToCurvePointG2(this.g2_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
        // TODO: fLegacy
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG2(this.g2_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
    }



}

