package org.thoughtj.bls.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Uint8Vector extends java.util.AbstractList<Short> implements java.util.RandomAccess {

    // Members

    public static final int SIZE = 2;
    private Short[] uint8_vector;


    // Constructors

    public Uint8Vector() {
        this.uint8_vector = new Short[1]; //(ByteBuffer.allocate(Uint8Vector.SIZE).array());
    }

    public Uint8Vector(int allocation_size) {
        this.uint8_vector = new Short[allocation_size]; //(ByteBuffer.allocate(Uint8Vector.SIZE).array());
    }
    
    public Uint8Vector(byte[] bytes) {
        // split byte array into Uint8Vectors by size
        int modulus = bytes.length % Uint8Vector.SIZE;
        // check valid - check for if the provided byte array is not even
        if (modulus != 0){
            throw new RuntimeException("Mod check failed for vector. Not containing Uint8Vectors with valid size");
        }
        // get count
        int count = bytes.length / Uint8Vector.SIZE;
        // preallocate array
        this.uint8_vector = new Short[count];
        // add each Uint8Vector from vector to array
        for (int i = 0; i < count; i+=2) {
            this.uint8_vector[i/2] = toShort(bytes[i], bytes[i+1]);
        }
    }

    public Uint8Vector(Uint8Vector other) {
        this.uint8_vector = other.uint8_vector;
    }

    public Uint8Vector(int count, short value) {
        // Instantiate a vector of value
        this();
        reserve(count);
        for (int i = 0; i<count-1; i++ ) {
            add(value);
        }
    }

    public Uint8Vector(short[] initialElements) {
        this();
        reserve(initialElements.length);

        for (short element : initialElements) {
            add(element);
        }
    }

    public Uint8Vector(Iterable<Short> initialElements) {
        this();
        for (short element : initialElements) {
            add(element);
        }
    }


    // Protected Methods and Functions

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }


    // Private Methods and Functions

    private short toShort(byte[] byte_array) {
        if (byte_array.length != 2) {
            throw new RuntimeException("byte array to short failed: byte array not length 2");
        }
        byte b1 = byte_array[0];
        byte b2 = byte_array[1];
        return (short) (b1<<8 | b2 & 0xFF);
    }

    private short toShort(byte byte1, byte byte2) {
        return (short) (byte1<<8 | byte2 & 0xFF);
    }

    private int doSize() {
        // get the count of Uint8Vectors from this uint8_vector
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        return this.uint8_vector.length;
    }

    private void doAdd(short x) {
        // add a Uint8Vector to the set of Uint8Vectors
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        int count = this.uint8_vector.length;
        List<Short> list = new ArrayList<>(Arrays.asList(uint8_vector));
        list.add(count - 1, x);
        this.uint8_vector = list.toArray(new Short[0]);
    }

    private void doAdd(int index, short x) {
        // add a Uint8Vector to the set of Uint8Vectors beginning at index
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        List<Short> list = new ArrayList<>(Arrays.asList(uint8_vector));
        list.add(index, x);
        this.uint8_vector = list.toArray(new Short[0]);
    }

    private short doRemove(int index) {
        // remove a Uint8Vector from this uint8_vector
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        List<Short> list = new ArrayList<>(List.of(this.uint8_vector));
        Short removed = list.remove(index);
        this.uint8_vector = list.toArray(new Short[0]);
        return removed;
    }

    private short doGet(int index) {
        // get a Uint8Vector from this uint8_vector
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        return this.uint8_vector[index];
    }

    private short doSet(int index, short val) {
        // set a Uint8Vector from this uint8_vector
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        this.uint8_vector[index] = val;
        return this.uint8_vector[index];
    }

    private void doRemoveRange(int fromIndex, int toIndex) {
        // remove a range of Uint8Vector from this uint8_vector
        // remove a Uint8Vector from this uint8_vector
        if (uint8_vector == null) {
            throw new RuntimeException("Uint8Vector is null");
        }
        List<Short> list = new ArrayList<>(List.of(this.uint8_vector));
        int count = toIndex - fromIndex;
        for (int i = 0; i < count; i++) {
            list.remove(fromIndex);
        }
        this.uint8_vector = list.toArray(new Short[0]);
    }


    // Public Methods and Functions

    public Short get(int index) {
        return doGet(index);
    }

    public Short set(int index, Short e) {
        return doSet(index, e);
    }

    public boolean add(Short e) {
        modCount++;
        doAdd(e);
        return true;
    }

    public void add(int index, Short e) {
        modCount++;
        doAdd(index, e);
    }

    public Short remove(int index) {
        modCount++;
        return doRemove(index);
    }

    public int size() {
        return doSize();
    }


    public int capacity() {
        // public accessor for doSize
        // gets the total room available including unused indices
        return (int) Arrays.stream(this.uint8_vector).count();
    }

    public void reserve(int n) {
        // public accessor for reserve the size of n Uint8Vectors in an array
        this.uint8_vector = new Short[n];
    }

    public boolean isEmpty() {
        // public accessor for doSize == 0
        return this.uint8_vector.length == 0;
    }

    public void clear() {
        // public accessor for removing all Uint8Vectors from this uint8_vector
        this.uint8_vector = new Short[0];
    }

    // 3 times faster than bytebuffers, 9 times slower than jni
    // from https://stackoverflow.com/questions/10804852/how-to-convert-short-array-to-byte-array
    public byte[] toByteArray()
    {
        Short[] input = this.uint8_vector;
        int short_index, byte_index;
        int iterations = input.length;

        byte [] buffer = new byte[input.length * 2];

        short_index = byte_index = 0;

        for(/*NOP*/; short_index != iterations; /*NOP*/)
        {
            buffer[byte_index]     = (byte) (input[short_index] & 0x00FF);
            buffer[byte_index + 1] = (byte) ((input[short_index] & 0xFF00) >> 8);

            ++short_index; byte_index += 2;
        }

        return buffer;
    }
    
}