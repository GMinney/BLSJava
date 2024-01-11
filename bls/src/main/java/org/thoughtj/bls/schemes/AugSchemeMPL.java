package org.thoughtj.bls.schemes;

import org.thoughtj.bls.arith.CoreAPI;
import org.thoughtj.bls.arith.Curve;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.utils.Uint8VectorVector;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class AugSchemeMPL extends CoreMPL {
    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params
    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    // protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_AUG_";

    // Members
    protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_AUG_";


    // Constructor

    public AugSchemeMPL() {
        super(CIPHERSUITE_G2);
    }


    // Methods and Functions

    public G2Element sign(PrivateKey seckey, byte[] message) {
        BigInteger seckey_element= new BigInteger(seckey.serialize());
        BigInteger pubkey = Curve.calcPubKey(seckey_element);
        byte[] pubkey_bytes = pubkey.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[pubkey_bytes.length + message.length]);
        buffer.put(pubkey_bytes);
        buffer.put(message);
        byte[] signature = CoreAPI.CoreSign(seckey_element, buffer.array());
        assert signature != null;
        return G2Element.fromBytes(signature);

    }

    public G2Element sign(PrivateKey seckey, byte[] message, G1Element prepend_pk) {
        BigInteger seckey_element= new BigInteger(seckey.serialize());
        BigInteger pubkey = Curve.calcPubKey(seckey_element);
        byte[] pubkey_bytes = pubkey.toByteArray();
        byte[] pre_pk_bytes = prepend_pk.serialize();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[pre_pk_bytes.length + pubkey_bytes.length + message.length]);
        buffer.put(pre_pk_bytes);
        buffer.put(pubkey_bytes);
        buffer.put(message);
        byte[] signature = CoreAPI.CoreSign(seckey_element, buffer.array());
        assert signature != null;
        return G2Element.fromBytes(signature);
    }

    public boolean verify(byte[] pubkey, byte[] message, byte[] signature) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[pubkey.length + message.length]);
        buffer.put(pubkey);
        buffer.put(message);
        byte[] concat = buffer.array();
        return CoreAPI.CoreVerify(pubkey, concat, signature);
    }

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {
        if (pubkeys.size() != messages.size()){
            throw new RuntimeException("pubkey vector size doesn't match message vector size");
        }
        if (pubkeys.size() <= 1){
            throw new RuntimeException("vector size <= 1");
        }
        int vector_entries = messages.size();
        byte[][] messages_out = new byte[vector_entries][];
        for (int i = 0; i < pubkeys.size(); i++) {
            int concat_size = pubkeys.get(i).size() + messages.get(i).size();
            ByteBuffer buffer = ByteBuffer.wrap(new byte[concat_size]);
            buffer.put(pubkeys.get(i).toByteArray());
            buffer.put(messages.get(i).toByteArray());
            messages_out[i] = buffer.array();
        }
        return CoreAPI.CoreAggregateVerify(pubkeys.getBytes(), messages_out, signature);
    }

    public boolean aggregateVerify(G1ElementVector pubkeys, Uint8VectorVector messages, G2Element signature) {
        if (pubkeys.size() != messages.size()){
            throw new RuntimeException("pubkey vector size doesn't match message vector size");
        }
        if (pubkeys.size() <= 1){
            throw new RuntimeException("vector size <= 1");
        }
        int vector_entries = messages.size();
        byte[][] messages_out = new byte[vector_entries][];
        for (int i = 0; i < pubkeys.size(); i++) {
            int concat_size = pubkeys.get(i).serialize().length + messages.get(i).size();
            ByteBuffer buffer = ByteBuffer.wrap(new byte[concat_size]);
            buffer.put(pubkeys.get(i).serialize());
            buffer.put(messages.get(i).toByteArray());
            messages_out[i] = buffer.array();
        }
        return CoreAPI.CoreAggregateVerify(pubkeys.getBytes(), messages_out, signature.serialize());
    }


}
