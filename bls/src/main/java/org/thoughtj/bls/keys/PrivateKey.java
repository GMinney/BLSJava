package org.thoughtj.bls.keys;

import org.thoughtj.bls.HKDF256;
import org.thoughtj.bls.arith.Mapping;
import org.thoughtj.bls.arith.Params;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.utils.ByteVector;
import org.thoughtj.bls.utils.HexUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.arith.Curve;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class PrivateKey {

    // Members

    // When this class is instantiated, allocate a 32byte byte array with 0's
    private BigInteger keydata = new BigInteger(ByteBuffer.allocate(PRIVATE_KEY_SIZE).array());
    public final static int PRIVATE_KEY_SIZE = 32;
    private static Logger log = LoggerFactory.getLogger(PrivateKey.class);

    // Constructors

    public PrivateKey(){
        allocateKeyData();
    }

    public PrivateKey(PrivateKey pk){
        pk.checkKeyData();
        allocateKeyData();
        bn_copy(keydata, privateKey.keydata);
    }

    // Public Functions and Methods

    public static PrivateKey fromSeedBIP32(byte[] seed) {
        // Generate a random private key from a provided seed
        // hmacKey - "BLS private key seed" in ascii
        byte[] hmacKey = {66, 76, 83, 32, 112, 114, 105, 118, 97, 116, 101, 32, 107, 101, 121, 32, 115, 101, 101, 100};
        byte[] hash = new byte[PRIVATE_KEY_SIZE];
        // Hash the seed into sk
        HKDF256.extract(hash, 0, seed.length, hmacKey, hmacKey.length);
        byte[] pkhash = HKDF256.prk;

        // Check that pk is less than curve order via mod arithmetic
        BigInteger order = Params.BLS_CURVE_ORDER_R;


        return new PrivateKey(pkhash);
    }

    public static PrivateKey randomPrivateKey() {
        // Generate a random private key from a randomly generated seed
        BigInteger r = BigInteger.ONE;
        bn_rand(r, RLC_POS, 256);

        PrivateKey k;
        bn_copy(k.keydata, r);

        BigInteger order = Params.BLS_CURVE_ORDER_R;
        bn_mod_basic(k.keydata, k.keydata, order);
        return k;
    }

    public static PrivateKey fromBytes(byte[] bytes, boolean modOrder) {
        if (bytes.length != PRIVATE_KEY_SIZE) {
            throw new RuntimeException("PrivateKey::FromBytes: Invalid size");
        }

        PrivateKey k = null;
        bn_read_bin(k.keydata, 0, PrivateKey.PRIVATE_KEY_SIZE);

        BigInteger order = g1_get_ord(ord);
        if (modOrder) {
            bn_mod_basic(k.keydata, k.keydata, order);
        }
        else {
            if (bn_cmp(k.keydata, order) > 0) {
                throw new RuntimeException("PrivateKey byte data must be less than the group order");
            }
        }
        return k;
    }

    public static PrivateKey fromByteVector(ByteVector bytes, boolean modOrder) {
        return PrivateKey.fromBytes(Bytes(bytes), modOrder);
    }

    public static PrivateKey fromBytes(byte[] bytes) {
        return fromBytes(bytes, true);
    }

    public static PrivateKey aggregate(PrivateKeyVector privateKeys) {
        // Aggregate a list of private keys into one. A core operation of BLS
        if (privateKeys.isEmpty()) {
            throw new RuntimeException("Number of private keys must be at least 1");
        }
        BigInteger order = g1_get_ord(order);

        PrivateKey ret = null;
        assert(ret.isZero());
        for (PrivateKey privateKey : privateKeys) {
            privateKey.checkKeyData();
            bn_add(ret.keydata, ret.keydata, privateKey.keydata);
            bn_mod_basic(ret.keydata, ret.keydata, order);
        }
        return ret;
    }


    // getG1Element returns a Public Key of 48bytes corresponding the Private Key of 32bytes
    public G1Element getG1Element() {
        if (!fG1CacheValid) {
            CheckKeyData();
            G1Element p = new G1Element(); // alloc 1
            g1_mul_gen(p, keydata);

            g1Cache = G1Element.fromNative(p);
            fG1CacheValid = true;
        }
        return g1Cache;
    }

    // getG2Element returns a signature of 96bytes corresponding the Private Key of 32bytes
    public G2Element getG2Element() {
        if (!fG2CacheValid) {
            checkKeyData();
            G2Element q = new G2Element(); // alloc 1
            g2_mul_gen(q, keydata);

            g2Cache = G2Element.fromNative(q);
            fG2CacheValid = true;
        }
        return g2Cache;
    }

    public G2Element getG2Power(G2Element element) {
        checkKeyData();
        G2Element q = new G2Element(); // alloc 1
        element.toNative(q);
        g2_mul(q, q, keydata);

        return G2Element.fromNative(q);
    }

    // Serialize a PrivateKey into a byte array with fLegacy
    public byte[] serialize(boolean fLegacy) {
        //TODO: fLegacy
        try {
            if (!hasKeyData()){
                throw new RuntimeException();
            }
            return this.keydata.toByteArray();
        }
        catch (RuntimeException e){
            log.error("Exception: PrivateKey: Does not contain any keydata", e);
        }

        return null;
    }

    // Serialize a PrivateKey into a byte array
    public byte[] serialize() {
        try {
            if (!hasKeyData()){
                throw new RuntimeException();
            }
            return this.keydata.toByteArray();
        }
        catch (RuntimeException e){
            log.error("Exception: PrivateKey: Does not contain any keydata", e);
        }

        return null;
    }


    // Sign a message with a private key and return a G2Element and choose the Legacy scheme is boolean is true
    public G2Element signG2(byte[] msg, long len, byte[] dst, long dst_len, boolean fLegacy) {
        checkKeyData();

        G2Element pt = new G2Element();

        if (fLegacy) {
            ep2_map_legacy(pt, msg, Params.BLS_MESSAGE_HASH_LEN);

        } else {
            ep2_map_dst(pt, msg, len, dst, dst_len);
        }

        g2_mul(pt, pt, keydata);
        G2Element ret = G2Element.fromNative(pt);
        return ret;
    }

    // Sign a message with a private key and return a G2Element
    public G2Element signG2(byte[] msg, long len, byte[] dst, long dst_len) {

    }

    // Null check for keydata
    public boolean hasKeyData() {
        return this.keydata != null;
    }

}