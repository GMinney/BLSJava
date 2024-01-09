package org.thoughtj.bls.keys;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.thoughtj.bls.keys.PrivateKey.PRIVATE_KEY_SIZE;

public class PrivateKeyVector extends java.util.AbstractList<PrivateKey> implements java.util.RandomAccess {

    PrivateKey[] privatekey_vector;

    // Constructors

    public PrivateKeyVector() {
        this.privatekey_vector = new PrivateKey[1]; //(ByteBuffer.allocate(PrivateKey.SIZE).array());
    }

    public PrivateKeyVector(byte[] byte_vector) {
        // split byte array into PrivateKeys by size
        int modulus = byte_vector.length % PRIVATE_KEY_SIZE;
        // check valid
        if (modulus != 0){
            throw new RuntimeException("Mod check failed for vector. Not containing PrivateKeys with valid size");
        }
        // get count
        int count = byte_vector.length / PRIVATE_KEY_SIZE;
        // preallocate array
        this.privatekey_vector = new PrivateKey[count];
        // add each PrivateKey from vector to array
        for (int i = 0; i < count; i++) {
            int offset = i * PRIVATE_KEY_SIZE;
            PrivateKey item = PrivateKey.fromBytes(ByteBuffer.wrap(byte_vector, offset, PRIVATE_KEY_SIZE).array());
            this.privatekey_vector[i] = item;
        }
    }

    public PrivateKeyVector(int count, PrivateKey value) {
        // Instantiate a vector of value
        this();
        reserve(count);
        for (int i = 0; i<count-1; i++ ) {
            add(value);
        }
    }

    public PrivateKeyVector(PrivateKeyVector other) {
        this.privatekey_vector = other.privatekey_vector;
    }

    public PrivateKeyVector(PrivateKey[] initialElements) {
        this();
        reserve(initialElements.length);

        ArrayList<PrivateKey> privateKeyArrayList = new ArrayList<>(Arrays.asList(initialElements));
    }

    public PrivateKeyVector(Iterable<PrivateKey> initialElements) {
        this();
        for (PrivateKey element : initialElements) {
            add(element);
        }
    }


    // Private Functions and Methods

    private int doSize() {
        // get the count of PrivateKeys from this privatekey_vector
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        return this.privatekey_vector.length;
    }

    private void doAdd(PrivateKey x) {
        // add a PrivateKey to the set of PrivateKeys
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        int count = this.privatekey_vector.length;
        List<PrivateKey> list = new ArrayList<>(Arrays.asList(privatekey_vector));
        list.add(count - 1, x);
        this.privatekey_vector = list.toArray(new PrivateKey[0]);
    }

    private void doAdd(int index, PrivateKey x) {
        // add a PrivateKey to the set of PrivateKeys beginning at index
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        List<PrivateKey> list = new ArrayList<>(Arrays.asList(privatekey_vector));
        list.add(index, x);
        this.privatekey_vector = list.toArray(new PrivateKey[0]);
    }

    private PrivateKey doRemove(int index) {
        // remove a PrivateKey from this privatekey_vector
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        List<PrivateKey> list = new ArrayList<>(List.of(this.privatekey_vector));
        PrivateKey removed = list.remove(index);
        this.privatekey_vector = list.toArray(new PrivateKey[0]);
        return removed;
    }

    private PrivateKey doGet(int index) {
        // get a PrivateKey from this privatekey_vector
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        return this.privatekey_vector[index];
    }

    private PrivateKey doSet(int index, PrivateKey val) {
        // set a PrivateKey from this privatekey_vector
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        this.privatekey_vector[index] = val;
        return this.privatekey_vector[index];
    }

    private void doRemoveRange(int fromIndex, int toIndex) {
        // remove a range of PrivateKey from this privatekey_vector
        // remove a PrivateKey from this privatekey_vector
        if (privatekey_vector == null) {
            throw new RuntimeException("PrivateKeyVector is null");
        }
        List<PrivateKey> list = new ArrayList<>(List.of(this.privatekey_vector));
        int count = toIndex - fromIndex;
        for (int i = 0; i < count; i++) {
            list.remove(fromIndex);
        }
        this.privatekey_vector = list.toArray(new PrivateKey[0]);
    }


    // Public Functions and Methods

    public PrivateKey get(int index) {
        return doGet(index);
    }

    public PrivateKey set(int index, PrivateKey e) {
        return doSet(index, e);
    }

    public boolean add(PrivateKey e) {
        modCount++;
        doAdd(e);
        return true;
    }

    public void add(int index, PrivateKey e) {
        modCount++;
        doAdd(index, e);
    }

    public PrivateKey remove(int index) {
        modCount++;
        return doRemove(index);
    }

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

    public int size() {
        return doSize();
    }

    public int capacity() {
        // public accessor for doSize
        // gets the total room available including unused indices
        return (int) Arrays.stream(this.privatekey_vector).filter(Objects::isNull).count();
    }

    public void reserve(int n) {
        // public accessor for reserve the size of n PrivateKeys in an array
        this.privatekey_vector = new PrivateKey[n];
    }

    public boolean isEmpty() {
        // public accessor for doSize == 0
        return this.privatekey_vector.length == 0;
    }

    public void clear() {
        // public accessor for removing all PrivateKeys from this privatekey_vector
        this.privatekey_vector = new PrivateKey[0];
    }

}