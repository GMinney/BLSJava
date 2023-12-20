package org.thoughtj.bls.utils;

import com.google.common.io.BaseEncoding;

public class HexUtils {
    public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    static public byte [] hexToBytes(String hex) {
        return HEX.decode(hex);
    }

    public static byte[] reverse(byte[] data) {
        for (int i = 0, j = data.length - 1; i < data.length / 2; i++, j--) {
            data[i] ^= data[j];
            data[j] ^= data[i];
            data[i] ^= data[j];
        }
        return data;
    }

    // hasOnlyZeroes checks a byte array "bytes" for any byte that is non-zero and returns a boolean
    public static boolean hasOnlyZeroes(byte[] bytes) {
        for (byte b : bytes) {
            if (b != (byte) 0b0000000){
                return false;
            }
        }
        return true;
    }

    public static String hexStr(byte [] bytes) {
        return HEX.encode(bytes);
    }
}
