package org.thoughtj.bls;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.thoughtj.bls.arith.Params;

public class HKDF256 {

    // HKDF256 is the class responsible for KeyGen

    public static void extract(byte[] prk_output, byte[] salt, long saltLen, byte[] ikm, long ikm_len) {
        // prk_output - pseudo-random key output
        // salt - optional salt value
        // saltLen - length of salt
        // ikm - input keying material
        // ikm_len - input keying material length

        byte[] prk = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, ikm).hmac(salt);
        //TODO: FIX THIS
    }

    public static void expand(byte[] okm, long L, byte[] prk, byte[] info, long infoLen) {
        // prk - pseudo-random key output, usually output from extract
        // info - optional context and application specific information
        // infoLen - length of info
        // okm - output keying material
        // L - output keying material length
        //BigDecimal n = new BigDecimal(L).divide(BigDecimal.valueOf(HASH_LEN), RoundingMode.CEILING);
        int l = Params.BLS_L_VALUE.intValueExact();
        ByteArrayOutputStream hash_storage = new ByteArrayOutputStream( );

        byte[] stream = null;
        for (int i = 0; i < l; i++) {
            if (stream != null){
                hash_storage.writeBytes(stream);
            }
            hash_storage.writeBytes(info);
            hash_storage.write(i);
            stream = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, prk).hmac(hash_storage.toByteArray());
        }

        assert stream != null;
        okm = Arrays.copyOfRange(stream, 0, (int) L);
        //TODO: FIX THIS

    }

    public static void extractExpand(byte[] output, long outputLen, byte[] key, long keyLen, byte[] salt, long saltLen, byte[] info, long infoLen) {
        //TODO: FIX THIS
    }

    public static byte[] hash(byte[] message){
        String salt = DigestUtils.sha256Hex("BLS-SIG-KEYGEN-SALT-");;
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, salt).hmac(message);
    }
}