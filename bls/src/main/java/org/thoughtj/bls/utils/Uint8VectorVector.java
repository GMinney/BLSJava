package org.thoughtj.bls.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Uint8VectorVector extends java.util.AbstractList<Uint8Vector> implements java.util.RandomAccess {

    // Members

    private Uint8Vector[] uint8_vector_vector;

    // Constructors

    public Uint8VectorVector() {
        this.uint8_vector_vector = new Uint8Vector[1]; //(ByteBuffer.allocate(Uint8Vector.SIZE).array());
    }

    public Uint8VectorVector(byte[] other_vector) {
        // split byte array into Uint8Vectors by size
        int modulus = other_vector.length % Uint8Vector.SIZE;
        // check valid
        if (modulus != 0){
            throw new RuntimeException("Mod check failed for vector. Not containing Uint8Vectors with valid size");
        }
        // get count
        int count = other_vector.length / Uint8Vector.SIZE;
        // preallocate array
        this.uint8_vector_vector = new Uint8Vector[count];
        // add each Uint8Vector from vector to array
        for (int i = 0; i < count; i++) {
            int offset = i * Uint8Vector.SIZE;
            Uint8Vector item = Uint8Vector.fromBytes(ByteBuffer.wrap(other_vector, offset, Uint8Vector.SIZE).array());
            this.uint8_vector_vector[i] = item;
        }
    }
    
    public Uint8VectorVector(Uint8VectorVector other) {
        this.uint8_vector_vector = other.uint8_vector_vector;
    }

    public Uint8VectorVector(int count, Uint8Vector value) {
        // Instantiate a vector of value
        this();
        reserve(count);
        for (int i = 0; i<count-1; i++ ) {
            add(value);
        }
    }

    public Uint8VectorVector(Uint8Vector[] initialElements) {
        this();
        reserve(initialElements.length);

        for (Uint8Vector element : initialElements) {
            add(element);
        }
    }

    public Uint8VectorVector(Iterable<Uint8Vector> initialElements) {
        this();
        for (Uint8Vector element : initialElements) {
            add(element);
        }
    }

    // Protected Methods and Functions

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

    // Private Methods and Functions

    private int doSize() {
        // get the count of Uint8Vectors from this uint8_vector
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        return this.uint8_vector_vector.length;
    }

    private void doAdd(Uint8Vector x) {
        // add a Uint8Vector to the set of Uint8Vectors
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        int count = this.uint8_vector_vector.length;
        List<Uint8Vector> list = new ArrayList<>(Arrays.asList(uint8_vector_vector));
        list.add(count - 1, x);
        this.uint8_vector_vector = list.toArray(new Uint8Vector[0]);
    }

    private void doAdd(int index, Uint8Vector x) {
        // add a Uint8Vector to the set of Uint8Vectors beginning at index
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        List<Uint8Vector> list = new ArrayList<>(Arrays.asList(uint8_vector_vector));
        list.add(index, x);
        this.uint8_vector_vector = list.toArray(new Uint8Vector[0]);
    }

    private Uint8Vector doRemove(int index) {
        // remove a Uint8Vector from this uint8_vector
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        List<Uint8Vector> list = new ArrayList<>(List.of(this.uint8_vector_vector));
        Uint8Vector removed = list.remove(index);
        this.uint8_vector_vector = list.toArray(new Uint8Vector[0]);
        return removed;
    }

    private Uint8Vector doGet(int index) {
        // get a Uint8Vector from this uint8_vector
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        return this.uint8_vector_vector[index];
    }

    private Uint8Vector doSet(int index, Uint8Vector val) {
        // set a Uint8Vector from this uint8_vector
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        this.uint8_vector_vector[index] = val;
        return this.uint8_vector_vector[index];
    }

    private void doRemoveRange(int fromIndex, int toIndex) {
        // remove a range of Uint8Vector from this uint8_vector
        // remove a Uint8Vector from this uint8_vector
        if (uint8_vector_vector == null) {
            throw new RuntimeException("Uint8VectorVector is null");
        }
        List<Uint8Vector> list = new ArrayList<>(List.of(this.uint8_vector_vector));
        int count = toIndex - fromIndex;
        for (int i = 0; i < count; i++) {
            list.remove(fromIndex);
        }
        this.uint8_vector_vector = list.toArray(new Uint8Vector[0]);
    }

    // Public Methods and Functions

    public Uint8Vector get(int index) {
        return doGet(index);
    }

    public Uint8Vector set(int index, Uint8Vector e) {
        return doSet(index, e);
    }

    public boolean add(Uint8Vector e) {
        modCount++;
        doAdd(e);
        return true;
    }

    public void add(int index, Uint8Vector e) {
        modCount++;
        doAdd(index, e);
    }

    public Uint8Vector remove(int index) {
        modCount++;
        return doRemove(index);
    }

    public int size() {
        return doSize();
    }



    public int capacity() {
        // public accessor for doSize
        // gets the total room available including unused indices
        return (int) Arrays.stream(this.uint8_vector_vector).filter(Objects::isNull).count();
    }

    public void reserve(int n) {
        // public accessor for reserve the size of n Uint8Vectors in an array
        this.uint8_vector_vector = new Uint8Vector[n];
    }

    public boolean isEmpty() {
        // public accessor for doSize == 0
        return this.uint8_vector_vector.length == 0;
    }

    public void clear() {
        // public accessor for removing all Uint8Vectors from this uint8_vector
        this.uint8_vector_vector = new Uint8Vector[0];
    }
    
}