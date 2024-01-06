package org.thoughtj.bls.elements;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.HKDF256;
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

    // Force use of static constructors
    private G1Element() {
        this.g1_element = new BigInteger(ByteBuffer.allocate(SIZE).array());
    }

    private G1Element(G1Element element) {
        this.g1_element = element.g1_element;
    }
    private G1Element(byte[] element) {
        this.g1_element = new BigInteger(element);
    }

    public static G1Element fromBytes(byte[] bytes, boolean fLegacy) throws RuntimeException {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        // TODO: fLegacy
        return new G1Element(bytes);
    }

    public static G1Element fromBytes(byte[] bytes) throws RuntimeException {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        return new G1Element(bytes);
    }

    public static G1Element fromBytesUnchecked(byte[] bytes, boolean fLegacy) {
        // TODO: fLegacy
        return new G1Element(bytes);
    }

    public static G1Element fromBytesUnchecked(byte[] bytes) {
        // return G1Element object
        return new G1Element(bytes);
    }

    public static G1Element fromMessage(byte[] message, byte[] dst, int dst_len) {
        // TODO: fLegacy
        return new G1Element(message);
    }

    public static G1Element generator() {
        // Get the generator element of the BLS12-381 Curve
        return new G1Element(Params.BLS_CONST_P.toByteArray());
    }

    public boolean isValid() {
        return CoreAPI.KeyValidate(this.serialize());
    }

    public void checkValid() {
        if (!isValid()){
            throw new RuntimeException("Not valid");
        }
    }

    public G1Element negate() {
        this.g1_element = Field.fieldNegation(g1_element, Params.BLS_CONST_P);
        return this;
    }

    public GTElement pair(G2Element b) {
        return b.pair(this);
    }

    public long getFingerprint(boolean fLegacy) {
        // SHA256 the G1Element and return the first four bytes as long
        // TODO: fLegacy
        byte[] hash = HKDF256.hash(this.g1_element.toByteArray());
        return ByteBuffer.wrap(hash).getLong();
    }

    public long getFingerprint() {
        // SHA256 the G1Element and return the first four bytes as long
        byte[] hash = HKDF256.hash(this.g1_element.toByteArray());
        return ByteBuffer.wrap(hash).getLong();

    }

    public byte[] serialize(boolean fLegacy) {
        Point curve_point = Conversion.octetToCurvePointG1(this.g1_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
        // TODO: fLegacy
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG1(this.g1_element.toByteArray(), true);
        return Conversion.curvePointToOctetG1(curve_point);
    }



}
