package org.thoughtj.bls.schemes;

import org.thoughtj.bls.*;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.elements.G2ElementVector;
import org.thoughtj.bls.keys.PrivateKey;

public class CoreMPL {

    protected CoreMPL(long cPtr, boolean cMemoryOwn) {

    }


    public CoreMPL(String strId) {

    }

    public PrivateKey keyGen(byte[] seed) {

    }

    public byte[] skToPk(PrivateKey seckey) {

    }

    public G1Element skToG1(PrivateKey seckey) {

    }

    public G2Element sign(PrivateKey seckey, byte[] message) {

    }

    public boolean verify(byte[] pubkey, byte[] message, byte[] signature) {

    }

    public boolean verify(G1Element pubkey, byte[] message, G2Element signature) {

    }

    public byte[] aggregate(Uint8VectorVector signatures) {

    }

    public G2Element aggregate(G2ElementVector signatures) {

    }

    public G1Element aggregate(G1ElementVector publicKeys) {

    }

    public G2Element aggregateSecure(G1ElementVector vecPublicKeys, G2ElementVector vecSignatures, byte[] message) {

    }

    public boolean verifySecure(G1ElementVector vecPublicKeys, G2Element signature, byte[] message) {

    }

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {

    }

    public boolean aggregateVerify(G1ElementVector pubkeys, Uint8VectorVector messages, G2Element signature) {

    }

    public PrivateKey deriveChildSk(PrivateKey sk, long index) {

    }

    public PrivateKey deriveChildSkUnhardened(PrivateKey sk, long index) {

    }

    public G1Element deriveChildPkUnhardened(G1Element sk, long index) {

    }

}