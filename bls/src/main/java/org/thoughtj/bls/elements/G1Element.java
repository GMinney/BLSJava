package org.thoughtj.bls.elements;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.arith.*;
import org.thoughtj.bls.keys.PrivateKey;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class G1Element {

    // A G1Element is a public key that is 48 bytes in size or 384 bits in compressed form (odd or even byte + x coord )
    // A G1Element is a public key that is 96 bytes in size or 768 bits in uncompressed form
    public final static int SIZE = 48;

    private static Logger log = LoggerFactory.getLogger(PrivateKey.class);
    private BigInteger g1_element;


    public G1Element() {
        this.g1_element = new BigInteger(ByteBuffer.allocate(SIZE).array());
    }

    public G1Element(G1Element other) {
        this.g1_element = other.g1_element;
    }

    public G1Element fromBytes(byte[] bytes, boolean fLegacy) throws RuntimeException {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        // set the g1_element and return this object
        this.g1_element = new BigInteger(bytes);
        // if fLegacy, do something
        return this;
    }

    public G1Element fromBytes(byte[] bytes) throws RuntimeException {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        // set the g1_element and return this object
        this.g1_element = new BigInteger(bytes);
        return this;
    }

    public G1Element fromBytesUnchecked(byte[] bytes, boolean fLegacy) {
        // set the g1_element and return this object
        this.g1_element = new BigInteger(bytes);
        // if fLegacy, do something
        return this;
    }

    public G1Element fromBytesUnchecked(byte[] bytes) {
        // set the g1_element and return this object
        this.g1_element = new BigInteger(bytes);
        return this;
    }

    public G1Element fromMessage(byte[] message, byte[] dst, int dst_len) {
        // set the g1_element and return this object
        this.g1_element = new BigInteger(message);
        // if fLegacy, do something
        return this;
    }

    public G1Element generator() {
        return null;
    }

    public boolean isValid() {
        return false;
    }

    public void checkValid() {

    }

    public G1Element negate() {
        this.g1_element = Field.fieldNegation(g1_element, Params.BLS_CONST_P);
        return this;
    }

    public GTElement pair(G2Element b) {
        return b.pair(this);
    }

    public long getFingerprint(boolean fLegacy) {

    }

    public long getFingerprint() {

    }

    public byte[] serialize(boolean fLegacy) {
        Point curve_point = Conversion.octetToCurvePointG1(this.g1_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG1(this.g1_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
    }



}
