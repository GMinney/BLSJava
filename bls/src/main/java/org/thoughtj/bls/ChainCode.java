package org.thoughtj.bls;

import org.thoughtj.bls.arith.Conversion;
import org.thoughtj.bls.arith.Point;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.thoughtj.bls.arith.Conversion.curvePointToOctetG1;

public class ChainCode {

    // ChainCode Size is 32 bytes
    public final static int SIZE = 32;

    private BigInteger chain_code;

    // Constructors
    public ChainCode() {
        this.chain_code = new BigInteger(ByteBuffer.allocate(((int) SIZE)).array());
    }

    public ChainCode(byte[] bytes) {
        this.chain_code = new BigInteger(bytes);
    }

    public static ChainCode fromBytes(byte[] bytes) {
        // check the the bytes
        if (bytes.length != SIZE) {
            throw new RuntimeException("ChainCode failed check");
        }
        return new ChainCode(bytes);
    }

    public void serialize(byte[] buffer) {
        // modify members
        Point curve_point = Conversion.octetToCurvePointG1(this.chain_code.toByteArray(), true);
        // Members might need to be byte[] and not BigInt
        this.chain_code = new BigInteger(curvePointToOctetG1(curve_point));
    }

    public byte[] serialize() {
        Point curve_point = Conversion.octetToCurvePointG1(this.chain_code.toByteArray(), true);
        return curvePointToOctetG1(curve_point);
    }

}