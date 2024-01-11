package org.thoughtj.bls.schemes;

import org.thoughtj.bls.arith.Conversion;
import org.thoughtj.bls.arith.CoreAPI;
import org.thoughtj.bls.arith.Point;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.elements.G2ElementVector;
import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.utils.Uint8VectorVector;

import java.math.BigInteger;

public class CoreMPL {

    public CoreMPL(String strId) {

    }

    public PrivateKey keyGen(byte[] seed) {
        return new PrivateKey(CoreAPI.KeyGen(seed));
    }

    public byte[] skToPk(PrivateKey seckey) {
        return CoreAPI.SkToPk(seckey.serialize());
    }

    public G1Element skToG1(PrivateKey seckey) {
        BigInteger curve_point = Conversion.octetToFieldElement(seckey.serialize());
        return G1Element.fromBytes(curve_point.toByteArray());
    }

    public G2Element sign(PrivateKey seckey, byte[] message) {
        byte[] signature = CoreAPI.CoreSign(new BigInteger(seckey.serialize()), message);
        return G2Element.fromBytes(signature);
    }

    // verify signature in g2
    public boolean verify(byte[] pubkey, byte[] message, byte[] signature) {
        return CoreAPI.CoreVerify(pubkey, message, signature);
    }

    // verify signature in g2
    public boolean verify(G1Element pubkey, byte[] message, G2Element signature) {
        return CoreAPI.CoreVerify(pubkey.serialize(), message, signature.serialize());
    }

    public byte[] aggregate(Uint8VectorVector signatures) {
        return CoreAPI.CoreAggregate(signatures.getBytes());
    }

    public G2Element aggregate(G2ElementVector signatures) {
        byte[] aggregate = CoreAPI.CoreAggregate(signatures.getBytes());
        assert aggregate != null;
        return G2Element.fromBytes(aggregate);
    }

    public G1Element aggregate(G1ElementVector publicKeys) {
        byte[] aggregate = CoreAPI.CoreAggregate(publicKeys.getBytes());
        assert aggregate != null;
        return G1Element.fromBytes(aggregate);
    }

    public G2Element aggregateSecure(G1ElementVector vecPublicKeys, G2ElementVector vecSignatures, byte[] message) {

    }

    public G2Element aggregateSecure(G1ElementVector vecPublicKeys, G2ElementVector vecSignatures, byte[] message, boolean fLegacy) {
        // TODO: fLegacy
    }

    public boolean verifySecure(G1ElementVector vecPublicKeys, G2Element signature, byte[] message) {

    }

    public boolean verifySecure(G1ElementVector vecPublicKeys, G2Element signature, byte[] message, boolean fLegacy) {
        // TODO: fLegacy
    }

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {
        return CoreAPI.CoreAggregateVerify(pubkeys, messages, signature);
    }

    public boolean aggregateVerify(G1ElementVector pubkeys, Uint8VectorVector messages, G2Element signature) {
        return CoreAPI.CoreAggregateVerify(pubkeys, messages, signature.serialize());
    }

    public PrivateKey deriveChildSk(PrivateKey sk, long index) {

    }

    public PrivateKey deriveChildSkUnhardened(PrivateKey sk, long index) {

    }

    public G1Element deriveChildPkUnhardened(G1Element sk, long index) {

    }

}