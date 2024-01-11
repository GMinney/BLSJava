package org.thoughtj.bls.schemes;

import org.thoughtj.bls.arith.CoreAPI;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.utils.Uint8VectorVector;

import java.nio.ByteBuffer;

public class BasicSchemeMPL extends CoreMPL {
    // ID: the ciphersuite ID, an ASCII string. The REQUIRED format for this string is
    // "BLS_SIG_" || H2C_SUITE_ID || SC_TAG || "_"
    // https://www.ietf.org/archive/id/draft-irtf-cfrg-bls-signature-05.html#name-ciphersuite-format
    // || refers to a concatenation of params
    // CIPHERSUITE_G1 is for minimal size pubkeys denoted by the "G1"
    // CIPHERSUITE_G2 is for minimal size pubkeys denoted by the "G2"
    // protected static final String CIPHERSUITE_G1 = "BLS_SIG_BLS12381G1_XMD:SHA-256_SSWU_RO_NUL_";

    // Members
    protected static final String CIPHERSUITE_G2 = "BLS_SIG_BLS12381G2_XMD:SHA-256_SSWU_RO_NUL_";


    // Constructor

    public BasicSchemeMPL() {
        super(CIPHERSUITE_G2);
    }


    // Methods and Functions

    public boolean aggregateVerify(Uint8VectorVector pubkeys, Uint8VectorVector messages, byte[] signature) {

        if (pubkeys.size() != messages.size()){
            throw new RuntimeException("pubkey vector size doesn't match message vector size");
        }
        if (pubkeys.size() <= 1){
            throw new RuntimeException("vector size less than or equal to 1");
        }

        for (int i = 0; i < messages.size(); i++) {
            for (int j = 0; j < messages.size(); j++) {
                if (i != j) {
                    if (messages.get(i) == messages.get(j)) {
                        throw new RuntimeException("Two messages match");
                    }
                }
            }
        }

        int vector_entries = messages.size();
        byte[][] messages_out = new byte[vector_entries][];

        for (int i = 0; i < vector_entries; i++) {
            int concat_size = pubkeys.get(i).toByteArray().length + messages.get(i).toByteArray().length;
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
            throw new RuntimeException("vector size less than or equal to 1");
        }

        for (int i = 0; i < messages.size(); i++) {
            for (int j = 0; j < messages.size(); j++) {
                if (i != j) {
                    if (messages.get(i) == messages.get(j)) {
                        throw new RuntimeException("Two messages match");
                    }
                }
            }
        }

        int vector_entries = messages.size();
        byte[][] messages_out = new byte[vector_entries][];

        for (int i = 0; i < vector_entries; i++) {
            int concat_size = pubkeys.get(i).serialize().length + messages.get(i).toByteArray().length;
            ByteBuffer buffer = ByteBuffer.wrap(new byte[concat_size]);
            buffer.put(pubkeys.get(i).serialize());
            buffer.put(messages.get(i).toByteArray());
            messages_out[i] = buffer.array();
        }
        return CoreAPI.CoreAggregateVerify(pubkeys.getBytes(), messages_out, signature.serialize());
    }

}