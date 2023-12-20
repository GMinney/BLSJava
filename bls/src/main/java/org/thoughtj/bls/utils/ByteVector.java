package org.thoughtj.bls.utils;

import org.thoughtj.bls.Uint8Vector;
public class ByteVector extends Uint8Vector {

    public ByteVector() {
        super();
    }

    public ByteVector(ByteVector byteVector) {
        super(byteVector);
    }

    public ByteVector(Iterable<Short> initialElements) {
        super(initialElements);
    }
    public ByteVector(byte[] byteArray) {
        for (byte b : byteArray) {
            add((short)b);
        }
    }

    public ByteVector(short[] shortArray) {
        super(shortArray);
    }

    public ByteVector(int count, short value) {
        super(count, value);
    }
}