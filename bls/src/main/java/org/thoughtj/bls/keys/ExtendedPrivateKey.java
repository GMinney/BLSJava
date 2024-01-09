package org.thoughtj.bls.keys;

import org.thoughtj.bls.ChainCode;
import org.thoughtj.bls.HKDF256;
import org.thoughtj.bls.arith.Conversion;
import org.thoughtj.bls.arith.Point;
import org.thoughtj.bls.elements.G1Element;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.thoughtj.bls.arith.Conversion.curvePointToOctetG1;

/*
Defines a BIP-32 style node, which is composed of a private key and a
chain code. This follows the spec from BIP-0032, with a few changes:
  * The master secret key is generated mod n from the master seed,
    since not all 32 byte sequences are valid BLS private keys
  * Instead of SHA512(input), do SHA256(input || 00000000) ||
    SHA256(input || 00000001)
  * Mod n for the output of key derivation.
  * ID of a key is SHA256(pk) instead of HASH160(pk)
  * Serialization of extended public key is 93 bytes
*/
public class ExtendedPrivateKey {

    // Members

    // version(4) depth(1) parent fingerprint(4) child#(4) cc(32) sk(32)

    public final static long SIZE = 77;

    // uint32_t is an 32bit int in java, however java uses signed integers, unless using unsigned arithmetic
    // uint32_t is an 8bit byte in java, however java uses signed bytes, unless using unsigned arithmetic, and im not sure how to do this directly with bytes

    private long version;
    private short depth;
    private long parentFingerprint;
    private long childNumber;
    private ChainCode chainCode;
    private PrivateKey sk;


    private BigInteger extended_private_key;

    // Constructors

    public ExtendedPrivateKey() {
        this.extended_private_key = new BigInteger(ByteBuffer.allocate(((int) SIZE)).array());
    }

    public ExtendedPrivateKey(long version, short depth, long parentFingerprint, long childNumber, ChainCode cc, PrivateKey sk) {
        this.version = version;
        this.depth = depth;
        this.parentFingerprint = parentFingerprint;
        this.childNumber = childNumber;
        this.chainCode = cc;
        this.sk = sk;
    }

    public static ExtendedPrivateKey fromSeed(byte[] bytes) {
        // prefix - "BLS HD seed" in ascii
        byte[] prefix = {66, 76, 83, 32, 72, 68, 32, 115, 101, 101, 100};
        byte[] hashInput = new byte[bytes.length + 1];  // allocate space
        // allocate space for chaincode and privatekey
        byte[] pk = new byte[PrivateKey.PRIVATE_KEY_SIZE];
        byte[] cc = new byte[ChainCode.SIZE];
        System.arraycopy(bytes, 0, hashInput, 0, bytes.length);
        System.arraycopy(bytes, 0, pk, 0, bytes.length);
        System.arraycopy(bytes, 32, cc, 0, bytes.length);

        // Hash the seed into 64 bytes, half will be sk, half will be cc
        HKDF256.extract(pk, prefix, prefix.length, hashInput, bytes.length + 1);
        pk = HKDF256.prk;
        HKDF256.extract(cc, prefix, prefix.length, hashInput, bytes.length + 1);
        cc = HKDF256.prk;

        // Make sure private key is less than the curve order
        BigInteger order = g1_get_curve_order();

        bn_new(*skBn);
        bn_read_bin(*skBn, ILeft, PrivateKey::PRIVATE_KEY_SIZE);
        bn_mod_basic(*skBn, *skBn, order);
        bn_write_bin(ILeft, PrivateKey::PRIVATE_KEY_SIZE, *skBn);


        // hash seed, left 32 bytes is sk, right is cc
        byte[] hash = HKDF256.hash(bytes);
        System.arraycopy(hash, 0, pk, 0, pk.length);
        System.arraycopy(hash, 32, cc, 0, cc.length);
        // Make sure private key is less than the curve order
        return new ExtendedPrivateKey(ExtendedPublicKey.VERSION, 0, 0, 0, cc, pk);
    }

    public static ExtendedPrivateKey fromBytes(byte[] bytes) {
        BigInteger version = BigInteger.valueOf(bytes[0]);
        BigInteger depth;
        BigInteger parentfingerprint;
        BigInteger childnumber;
        return new ExtendedPrivateKey(version, 0, 0, 0, cc, pk);
    }

    public ExtendedPrivateKey privateChild(long i, boolean fLegacy) {
        if (depth >= 255) {
            throw new RuntimeException("Cannot go further than 255 levels");
        }
        // Hardened keys have i >= 2^31. Non-hardened have i < 2^31
        long cmp = 1L << 31;
        boolean hardened = (i >= cmp);
        // allocate space for chaincode and privatekey
        byte[] pk = new byte[PrivateKey.PRIVATE_KEY_SIZE];
        byte[] cc = new byte[ChainCode.SIZE];
        // Chain code is used as hmac key
        chainCode.serialize(cc);
        int inputLen = hardened ? PrivateKey.PRIVATE_KEY_SIZE + 4 + 1 : G1Element.SIZE + 4 + 1;
        // Hmac input includes sk or pk, int i, and byte with 0 or 1
        byte[] hmacInput = new byte[inputLen];
        // Fill the input with the required data
        if (hardened) {
            hmacInput = sk.serialize();
            byte[] newbytes = (hmacInput + PrivateKey.PRIVATE_KEY_SIZE, i); // uses utils int to four bytes
        } else {
            System.arraycopy(sk.getG1Element().serialize(fLegacy), 0, hmacInput, 0, G1Element.SIZE);
            byte[] newbytes = (hmacInput + G1Element.SIZE, i);
        }
        hmacInput[inputLen - 1] = 0;
        HKDF256.extract(pk, hmacInput, inputLen, cc, ChainCode.SIZE);
        // Change 1 byte to generate a different sequence for chaincode
        hmacInput[inputLen - 1] = 1;
        PrivateKey newSk = PrivateKey.fromBytes(pk, true);
        newSk = PrivateKey.aggregate(sk, newSk).serialize();

        ExtendedPrivateKey esk = new ExtendedPrivateKey(version, depth + 1, sk.getG1Element().getFingerprint(), i, ChainCode.fromBytes(cc, newSk));
    }

    public ExtendedPrivateKey privateChild(long i) {
        if (depth >= 255) {
            throw new RuntimeException("Cannot go further than 255 levels");
        }
        // Hardened keys have i >= 2^31. Non-hardened have i < 2^31
        long cmp = 1L << 31;
        boolean hardened = (i >= cmp);
        // allocate space for chaincode and privatekey
        byte[] pk = new byte[PrivateKey.PRIVATE_KEY_SIZE];
        byte[] cc = new byte[ChainCode.SIZE];
        // Chain code is used as hmac key
        chainCode.serialize(cc);
        int inputLen = hardened ? PrivateKey.PRIVATE_KEY_SIZE + 4 + 1 : G1Element.SIZE + 4 + 1;
        // Hmac input includes sk or pk, int i, and byte with 0 or 1
        byte[] hmacInput = new byte[inputLen];
        // Fill the input with the required data
        if (hardened) {
            hmacInput = sk.serialize();
            byte[] newbytes = (hmacInput + PrivateKey.PRIVATE_KEY_SIZE, i); // uses utils int to four bytes
        } else {
            System.arraycopy(sk.getG1Element().serialize(false), 0, hmacInput, 0, G1Element.SIZE);
            byte[] newbytes = (hmacInput + G1Element.SIZE, i);
        }
        hmacInput[inputLen - 1] = 0;
        HKDF256.extract(pk, hmacInput, inputLen, cc, ChainCode.SIZE);
        // Change 1 byte to generate a different sequence for chaincode
        hmacInput[inputLen - 1] = 1;
        PrivateKey newSk = PrivateKey.fromBytes(pk, true);
        newSk = PrivateKey.aggregate(sk, newSk).serialize();

        ExtendedPrivateKey esk = new ExtendedPrivateKey(version, depth + 1, sk.getG1Element().getFingerprint(), i, ChainCode.fromBytes(cc, newSk));
    }

    public ExtendedPublicKey publicChild(long i) {
        return privateChild(i).getExtendedPublicKey();
    }

    public ExtendedPublicKey getExtendedPublicKey(boolean fLegacy) {
        byte[] cc = new byte[(int) ExtendedPublicKey.SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(cc);
        buffer.put(version);
        buffer.put(depth);
        buffer.put(parentFingerprint);
        buffer.put(childNumber);
        byte[] chain_code = chainCode.serialize();
        buffer.put(chain_code);
        byte[] vecG1 = sk.getG1Element().serialize(fLegacy);
        buffer.put(vecG1);
        return new ExtendedPublicKey(buffer.array());
    }

    public ExtendedPublicKey getExtendedPublicKey() {
        byte[] cc = new byte[(int) ExtendedPublicKey.SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(cc);
        buffer.put(version);
        buffer.put(depth);
        buffer.put(parentFingerprint);
        buffer.put(childNumber);
        byte[] chain_code = chainCode.serialize();
        buffer.put(chain_code);
        byte[] vecG1 = sk.getG1Element().serialize(false);
        buffer.put(vecG1);
        return new ExtendedPublicKey(buffer.array());
    }


    // Public Functions and Methods

    // There does exist an operator overload for == and != in the cpp code

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

    public PrivateKey getPrivateKey() {
        return sk;
    }

    public G1Element getPublicKey() {
        return sk.getG1Element();
    }

    public void serialize(byte[] buffer) {
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_private_key.toByteArray(), true);
        // Members might need to be byte[] and not BigInt
        this.extended_private_key = new BigInteger(curvePointToOctetG1(curve_point));
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG1(this.extended_private_key.toByteArray(), true);
        return curvePointToOctetG1(curve_point);
    }

}
