package org.thoughtj.bls.keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.ChainCode;
import org.thoughtj.bls.HKDF256;
import org.thoughtj.bls.arith.Conversion;
import org.thoughtj.bls.arith.Point;
import org.thoughtj.bls.elements.G1Element;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.thoughtj.bls.arith.Conversion.curvePointToOctetG1;

public class ExtendedPublicKey {

    // Members

    // Version number and Size bytes pulled from bls-signatures
    public final static long VERSION = 1;
    public final static long SIZE = 93;
    private static Logger log = LoggerFactory.getLogger(PrivateKey.class);

    private long version;
    private short depth;
    private long parentFingerprint;
    private long childNumber;
    private ChainCode chainCode;
    private PrivateKey sk;

    private BigInteger extended_public_key;


    // Constructors
    
    private ExtendedPublicKey() {
        this.extended_public_key = new BigInteger(ByteBuffer.allocate(((int) SIZE)).array());
    }

    private ExtendedPublicKey(byte[] ext_pubkey) {
        this.extended_public_key = new BigInteger(ext_pubkey);
    }

    public static ExtendedPublicKey fromBytes(byte[] bytes, boolean fLegacy) {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        // TODO: fLegacy
        return new ExtendedPublicKey(bytes);
    }

    public static ExtendedPublicKey fromBytes(byte[] bytes) {
        // check the the bytes
        if (bytes.length != SIZE) {
            log.error("input bytes wrong size");
            throw new RuntimeException("G1Element failed check");
        }
        return new ExtendedPublicKey(bytes);
    }

    public ExtendedPublicKey publicChild(long i, boolean fLegacy) {
        // Hardened children have i >= 2^31. Non-hardened have i < 2^31
        long cmp = (1L << 31);
        if (i >= cmp) {
            throw new RuntimeException("Cannot derive hardened children from public key");
        }
        if (depth >= 255) {
            throw new RuntimeException("Cannot go further than 255 levels");
        }
        byte[] ILeft = new byte[PrivateKey.PRIVATE_KEY_SIZE];
        byte[] IRight = new byte[ChainCode.SIZE];

        // Chain code is used as hmac key
        byte[] hmacKey = new byte[ChainCode.SIZE];
        chainCode.serialize(hmacKey);

        // Public key serialization, i serialization, and one 0 or 1 byte
        long inputLen = G1Element.SIZE + 4 + 1;

        // Hmac input includes sk or pk, int i, and byte with 0 or 1
        byte[] hmacInput = new byte[G1Element.SIZE + 4 + 1];

        // Fill the input with the required data
        byte[] vecG1 = pk.serialize(fLegacy);
        memcpy(hmacInput, vecG1, vecG1.length);

        hmacInput[(int) (inputLen - 1)] = 0;
        Util.IntToFourBytes(hmacInput + G1Element.SIZE, i);

        HKDF256.extract(ILeft, hmacInput, inputLen, hmacKey, ChainCode.SIZE);
        ILeft = HKDF256.prk;

        // Change 1 byte to generate a different sequence for chaincode
        hmacInput[(int) (inputLen - 1)] = 1;

        HKDF256.extract(IRight, hmacInput, inputLen, hmacKey, ChainCode.SIZE);
        IRight = HKDF256.prk;

        PrivateKey leftSk = PrivateKey.fromBytes(Bytes(ILeft, PrivateKey.PRIVATE_KEY_SIZE), true);
        G1Element newPk = pk + leftSk.getG1Element();

        return new ExtendedPublicKey(version, depth + 1, getPublicKey().getFingerprint(), i, ChainCode.fromBytes(Bytes(IRight, ChainCode.SIZE)), newPk);
    }

    public ExtendedPublicKey publicChild(long i) {
        // Hardened children have i >= 2^31. Non-hardened have i < 2^31
        long cmp = (1L << 31);
        if (i >= cmp) {
            throw new RuntimeException("Cannot derive hardened children from public key");
        }
        if (depth >= 255) {
            throw new RuntimeException("Cannot go further than 255 levels");
        }
        byte[] ILeft = new byte[PrivateKey.PRIVATE_KEY_SIZE];
        byte[] IRight = new byte[ChainCode.SIZE];

        // Chain code is used as hmac key
        byte[] hmacKey = new byte[ChainCode.SIZE];
        chainCode.serialize(hmacKey);

        // Public key serialization, i serialization, and one 0 or 1 byte
        long inputLen = G1Element.SIZE + 4 + 1;

        // Hmac input includes sk or pk, int i, and byte with 0 or 1
        byte[] hmacInput = new byte[G1Element.SIZE + 4 + 1];

        // Fill the input with the required data
        byte[] vecG1 = pk.serialize(false);
        memcpy(hmacInput, vecG1, vecG1.length);

        hmacInput[(int) (inputLen - 1)] = 0;
        Util.IntToFourBytes(hmacInput + G1Element.SIZE, i);

        HKDF256.extract(ILeft, hmacInput, inputLen, hmacKey, ChainCode.SIZE);
        ILeft = HKDF256.prk;

        // Change 1 byte to generate a different sequence for chaincode
        hmacInput[(int) (inputLen - 1)] = 1;

        HKDF256.extract(IRight, hmacInput, inputLen, hmacKey, ChainCode.SIZE);
        IRight = HKDF256.prk;

        PrivateKey leftSk = PrivateKey.fromBytes(Bytes(ILeft, PrivateKey.PRIVATE_KEY_SIZE), true);
        G1Element newPk = pk + leftSk.getG1Element();

        return new ExtendedPublicKey(version, depth + 1, getPublicKey().getFingerprint(), i, ChainCode.fromBytes(Bytes(IRight, ChainCode.SIZE)), newPk);
    }


    // Public Functions and Methods

    public long getVersion() {
        return ExtendedPublicKey.VERSION;
    }

    public short getDepth() {
        return depth;
    }

    public long getParentFingerprint() {
        return parentFingerprint;
    }

    public long getChildNumber() {
        return childNumber;
    }

    public ChainCode getChainCode() {
        return chainCode;
    }

    public G1Element getPublicKey() {
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_public_key.toByteArray(), true);
        return G1Element.fromBytes(curvePointToOctetG1(curve_point));
    }

    public void serialize(byte[] buffer, boolean fLegacy) {
        // modify members
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_public_key.toByteArray(), true);
        // Members might need to be byte[] and not BigInt
        this.extended_public_key = new BigInteger(curvePointToOctetG1(curve_point));
        //TODO: fLegacy
    }

    public void serialize(byte[] buffer) {
        // modify members
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_public_key.toByteArray(), true);
        // Members might need to be byte[] and not BigInt
        this.extended_public_key = new BigInteger(curvePointToOctetG1(curve_point));
    }

    public byte[] serialize(boolean fLegacy) {
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_public_key.toByteArray(), true);
        return curvePointToOctetG1(curve_point);
        //TODO: fLegacy
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_public_key.toByteArray(), true);
        return curvePointToOctetG1(curve_point);
    }

}