package org.thoughtj.bls.keys;

import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.utils.HexUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.arith.Curve;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class PrivateKey {

    // Private members

    // Get the logger attached to the BLS class
    // private static final Logger log = LoggerFactory.getLogger(BLS.class);

    // When this class is instantiated, allocate a 32byte byte array with 0's
    private BigInteger keydata = new BigInteger(ByteBuffer.allocate(PRIVATE_KEY_SIZE).array());


    // Public members
    public final static int PRIVATE_KEY_SIZE = 32;
    private static Logger log = LoggerFactory.getLogger(PrivateKey.class);



    // Methods
    public static PrivateKey fromSeedBIP32(byte[] seed) {
        // Generate a random private key from a provided seed
        return null;
    }

    public static PrivateKey randomPrivateKey() {
        // Generate a random private key from a randomly generated seed
        return null;
    }

    public static PrivateKey fromBytes(byte[] bytes, boolean modOrder) {
        try {
            if (bytes.length != PRIVATE_KEY_SIZE){

                throw new RuntimeException();
            }

            PrivateKey k = new PrivateKey();

            // In traditional modular arithmetic without braces, modulo is parsed as "left mod right", where each left and right side encompass the entire side of the operation.
            // An example of this would look like "(left) mod (right)", however in computer programming languages such as C++ and Java,
            // the mod operator is parsed with the same precedence as multiplication and division ("*,/") and is rather determined by associativity.
            if (modOrder) {
                // this allows any bytes to be input and does proper mod order
                //blst_scalar_from_be_bytes(k.keydata, bytes, bytes.length);

                k.keydata = new BigInteger(bytes);

            }
            else {
                // this should only be the output of deserialization
                //blst_scalar_from_bendian(k.keydata, bytes);
                k.keydata = new BigInteger(bytes);
            }

            // Check byte array for zeroes
            if (HexUtils.hasOnlyZeroes(bytes)) {
                return k; // don't check anything else, we allow zero private key
            }

            if (!blst_sk_check(k.keydata)) {
                log.error("PrivateKey: PrivateKey byte data must be less than the group order");
            }

            return k;
        }
        catch (RuntimeException e){
            log.error("Exception: {} | PrivateKey: Invalid Size: PrivateKey fromBytes | bytes.length: {} | PRIVATE_KEY_SIZE: {}", e, bytes.length, PRIVATE_KEY_SIZE);
        }

        return null;

    }

    public static PrivateKey fromBytes(byte[] bytes) {
        return fromBytes(bytes, true);
    }

    public static PrivateKey aggregate(PrivateKeyVector privateKeys) {
        // Aggregate a list of private keys into one. A core operation of BLS
        return null;
    }


    // getG1Element returns a Public Key of 48bytes corresponding the Private Key of 32bytes
    public G1Element getG1Element() {

        BigInteger pubKey = Curve.calcPubKey(this.keydata);



        G1Element pubkey;
        return pubkey;

    }
    // getG2Element returns a signature of 96bytes corresponding the Private Key of 32bytes
    public G2Element getG2Element() {
        return null;
    }

    public G2Element getG2Power(G2Element element) {
        return null;
    }

    // Serialize a PrivateKey into a byte array with fLegacy
    public byte[] serialize(boolean fLegacy) {
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
        return null;
    }

    // Sign a message with a private key and return a G2Element
    public G2Element signG2(byte[] msg, long len, byte[] dst, long dst_len) {
        return null;
    }

    // Null check for keydata
    public boolean hasKeyData() {
        return this.keydata != null;
    }

}