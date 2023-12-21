package org.thoughtj.bls.utils;

import org.thoughtj.bls.keys.Uint8VectorVector;

import java.util.List;

public class ByteVectorList extends Uint8VectorVector {

    public ByteVectorList() {
        super();
    }
    public ByteVectorList(List<byte[]> list) {
        for (byte [] array : list) {
            add(new ByteVector(array));
        }
    }

    public ByteVectorList(ByteVectorList list) {
        super(list);
    }

    public ByteVectorList(byte[][] arrays) {
        for (byte [] array : arrays) {
            add(new ByteVector(array));
        }
    }

    public ByteVectorList(byte [] firstByteArray, byte []... byteArrays) {
        add(new ByteVector(firstByteArray));
        for (byte [] byteArray: byteArrays) {
            add(byteArray);
        }
    }

    public void add(byte [] byteArray) {
        add(new ByteVector(byteArray));
    }
}