package org.thoughtj.bls.keys;

import org.thoughtj.bls.HKDF256;
import org.thoughtj.bls.arith.Params;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G2Element;

public class HDKeys {

    // Members

    // Length of the hash
    public final static short HASH_LEN = 32;

    // Public Methods and Functions

    public static PrivateKey keyGen(byte[] seed) {
        // KeyGen
        // 1. PRK = HKDF-Extract("BLS-SIG-KEYGEN-SALT-", IKM || I2OSP(0, 1))
        // 2. OKM = HKDF-Expand(PRK, keyInfo || I2OSP(L, 2), L)
        // 3. SK = OS2IP(OKM) mod r
        // 4. return SK

        byte[] info = new byte[1];
        long infoLen = 0;

        // Required by the ietf spec to be at least 32 bytes
        if (seed.length < 32) {
            throw new RuntimeException("Seed size must be at least 32 bytes");
        }

        // "BLS-SIG-KEYGEN-SALT-" in ascii
        byte[] saltHkdf = {66, 76, 83, 45, 83, 73, 71, 45, 75, 69, 89, 71, 69, 78, 45, 83, 65, 76, 84, 45};

        uint8_t *prk = Util.SecAlloc<uint8_t>(32);
        uint8_t *ikmHkdf = Util.SecAlloc<uint8_t>(seed.length + 1);
        memcpy(ikmHkdf, 0, seed.length);
        ikmHkdf[seed.length] = 0;

        final uint8_t L = Params.PUBKEY_LENGTH;  // `ceil((3 * ceil(log2(r))) / 16)`, where `r` is the
        // order of the BLS 12-381 curve

        HKDF256.okm = Util.SecAlloc<uint8_t>(L);

        uint8_t keyInfoHkdf[infoLen + 2];
        memcpy(keyInfoHkdf, info, infoLen);
        keyInfoHkdf[infoLen] = 0;  // Two bytes for L, 0 and 48
        keyInfoHkdf[infoLen + 1] = L;

        HKDF256.extractExpand(HKDF256.okm, L, ikmHkdf, seed.length + 1, saltHkdf, 20, keyInfoHkdf, infoLen + 2);

        bn_t order;
        bn_new(order);
        g1_get_ord(order);

        // Make sure private key is less than the curve order
        bn_t *skBn = Util.SecAlloc<bn_t>(1);
        bn_new(*skBn);
        bn_read_bin(*skBn, HKDF256.okm, L);
        bn_mod_basic(*skBn, *skBn, order);

        uint8_t *skBytes = Util.SecAlloc<uint8_t>(32);
        bn_write_bin(skBytes, 32, *skBn);

        return PrivateKey.fromBytes(Bytes(skBytes, 32));
    }

    public static void iKMToLamportSk(byte[] outputLamportSk, byte[] ikm, long ikmLen, byte[] salt, long saltLen) {
        // Expands the ikm to 255*HASH_LEN bytes for the lamport sk
        const uint8_t info[1] = {0};
        HKDF256.extractExpand(outputLamportSk, HASH_LEN * 255, ikm, ikmLen, salt, saltLen, info, 0);
    }

    public static void parentSkToLamportPK(byte[] outputLamportPk, PrivateKey parentSk, long index) {
        uint8_t* salt = Util.SecAlloc<uint8_t>(4);
        uint8_t* ikm = Util.SecAlloc<uint8_t>(HASH_LEN);
        uint8_t* notIkm = Util.SecAlloc<uint8_t>(HASH_LEN);
        uint8_t* lamport0 = Util.SecAlloc<uint8_t>(HASH_LEN * 255);
        uint8_t* lamport1 = Util.SecAlloc<uint8_t>(HASH_LEN * 255);

        Util.IntToFourBytes(salt, index);
        parentSk.serialize(ikm);

        for (size_t i = 0; i < HASH_LEN; i++) {  // Flips the bits
            notIkm[i] = ikm[i] ^ 0xff;
        }

        HDKeys.IKMToLamportSk(lamport0, ikm, HASH_LEN, salt, 4);
        HDKeys.IKMToLamportSk(lamport1, notIkm, HASH_LEN, salt, 4);

        uint8_t* lamportPk = Util.SecAlloc<uint8_t>(HASH_LEN * 255 * 2);

        for (size_t i = 0; i < 255; i++) {
            Util.Hash256(lamportPk + i * HASH_LEN, lamport0 + i * HASH_LEN, HASH_LEN);
        }

        for (size_t i=0; i < 255; i++) {
            Util.Hash256(lamportPk + 255 * HASH_LEN + i * HASH_LEN, lamport1 + i * HASH_LEN, HASH_LEN);
        }
        Util.Hash256(outputLamportPk, lamportPk, HASH_LEN * 255 * 2);

    }

    public static PrivateKey deriveChildSk(PrivateKey parentSk, long index) {
        uint8_t* lamportPk = Util.SecAlloc<uint8_t>(HASH_LEN);
        HDKeys.ParentSkToLamportPK(lamportPk, parentSk, index);
        std.vector<uint8_t> lamportPkVector(lamportPk, lamportPk + HASH_LEN);
        PrivateKey child = HDKeys.KeyGen(lamportPkVector);

        return child;
    }

    public static PrivateKey deriveChildSkUnhardened(PrivateKey parentSk, long index) {
        uint8_t* buf = Util.SecAlloc<uint8_t>(G1Element.SIZE + 4);
        uint8_t* digest = Util.SecAlloc<uint8_t>(HASH_LEN);
        memcpy(buf, parentSk.GetG1Element().Serialize().data(), G1Element.SIZE);
        Util.IntToFourBytes(buf + G1Element.SIZE, index);
        Util.Hash256(digest, buf, G1Element.SIZE + 4);

        PrivateKey ret = PrivateKey.Aggregate({parentSk, PrivateKey.FromBytes(Bytes(digest, HASH_LEN), true)});

        return ret;
    }

    public static G1Element deriveChildG1Unhardened(G1Element pk, long index) {
        uint8_t* buf = Util.SecAlloc<uint8_t>(G1Element.SIZE + 4);
        uint8_t* digest = Util.SecAlloc<uint8_t>(HASH_LEN);
        
        memcpy(buf, pk.serialize(), G1Element.SIZE);

        Util.IntToFourBytes(buf + G1Element.SIZE, index);
        Util.Hash256(digest, buf, G1Element.SIZE + 4);

        bn_t nonce, ord;
        bn_new(nonce);
        bn_zero(nonce);
        bn_read_bin(nonce, digest, HASH_LEN);
        bn_new(ord);
        g1_get_ord(ord);
        bn_mod_basic(nonce, nonce, ord);
        
        G1Element gen = G1Element.generator();
        return pk + gen * nonce;
    }

    public static G2Element deriveChildG2Unhardened(G2Element pk, long index) {
        uint8_t* buf = Util.SecAlloc<uint8_t>(G2Element.SIZE + 4);
        uint8_t* digest = Util.SecAlloc<uint8_t>(HASH_LEN);
        memcpy(buf, pk.serialize().data(), G2Element.SIZE);
        Util.IntToFourBytes(buf + G2Element.SIZE, index);
        Util.Hash256(digest, buf, G2Element.SIZE + 4);

        bn_t nonce, ord;
        bn_new(nonce);
        bn_zero(nonce);
        bn_read_bin(nonce, digest, HASH_LEN);
        bn_new(ord);
        g1_get_ord(ord);
        bn_mod_basic(nonce, nonce, ord);

        G2Element gen = G2Element.generator();
        return pk + gen * nonce;
    }

}