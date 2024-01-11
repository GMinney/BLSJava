package org.thoughtj.bls.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.utils.Uint8Vector;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.thoughtj.bls.elements.G2Element.SIZE;

public class G1ElementVector extends java.util.AbstractList<G1Element> implements java.util.RandomAccess {

    // Members

    private static Logger log = LoggerFactory.getLogger(G1ElementVector.class);

    private G1Element[] g1_element_vector;

    // Constructors

    // G1ElementVector is a vector (list) of G1Elements
    public G1ElementVector() {
        this.g1_element_vector = new G1Element[1]; //(ByteBuffer.allocate(G2Element.SIZE).array());
    }

    public G1ElementVector(byte[] element_vector) {
        // split byte array into G1Elements by size
        int modulus = element_vector.length % G1Element.SIZE;
        // check valid
        if (modulus != 0){
            throw new RuntimeException("Mod check failed for vector. Not containing G1Elements with valid size");
        }
        // get count
        int count = element_vector.length / G1Element.SIZE;
        // preallocate array
        this.g1_element_vector = new G1Element[count];
        // add each G1Element from vector to array
        for (int i = 0; i < count; i++) {
            int offset = i * SIZE;
            G1Element item = G1Element.fromBytes(ByteBuffer.wrap(element_vector, offset, SIZE).array());
            this.g1_element_vector[i] = item;
        }
    }

    public G1ElementVector(int count, G1Element value) {
        // Instantiate a vector of value
        this();
        reserve(count);
        for (int i = 0; i<count-1; i++ ) {
            add(value);
        }
    }

    public G1ElementVector(G1ElementVector other) {
        this.g1_element_vector = other.g1_element_vector;
    }

    public G1ElementVector(G1Element[] initialElements) {
        this();
        reserve(initialElements.length);

        for (G1Element element : initialElements) {
            add(element);
        }
    }

    public G1ElementVector(Iterable<G1Element> initialElements) {
        this();
        for (G1Element element : initialElements) {
            add(element);
        }
    }


    // Private Functions and Methods

    private int doSize() {
        // get the count of G1Elements from this g1_element_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        return this.g1_element_vector.length;
    }

    private void doAdd(G1Element x) {
        // add a G1Element to this g1_element_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        int count = this.g1_element_vector.length;
        List<G1Element> list = new ArrayList<>(Arrays.asList(g1_element_vector));
        list.add(count - 1, x);
        this.g1_element_vector = list.toArray(new G1Element[0]);
    }

    private void doAdd(int index, G1Element x) {
        // add a G1Element to this g1_element_vector beginning at index
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        List<G1Element> list = new ArrayList<>(Arrays.asList(g1_element_vector));
        list.add(index, x);
        this.g1_element_vector = list.toArray(new G1Element[0]);
    }

    private G1Element doRemove(int index) {
        // remove a G2Element from this g2_element_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        List<G1Element> list = new ArrayList<>(List.of(this.g1_element_vector));
        G1Element removed = list.remove(index);
        this.g1_element_vector = list.toArray(new G1Element[0]);
        return removed;
    }

    private G1Element doGet(int index) {
        // get a G1Element from this g1_element_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        return this.g1_element_vector[index];
    }

    private byte[][] doGetBytes() {
        // get a 2d-byte array from this uint8_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        int length = g1_element_vector.length;
        G1Element[] g1vector = this.g1_element_vector;
        byte[][] out = new byte[length][];

        for (int i = 0; i < length; i++) {
            out[i] = g1vector[i].serialize();
        }
        return out;
    }

    private G1Element doSet(int index, G1Element val) {
        // set a G1Element from this g1_element_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        this.g1_element_vector[index] = val;
        return this.g1_element_vector[index];
    }

    private void doRemoveRange(int fromIndex, int toIndex) {
        // remove a range of G1Element from this g1_element_vector
        // remove a G1Element from this g1_element_vector
        if (g1_element_vector == null) {
            throw new RuntimeException("G1ElementVector is null");
        }
        List<G1Element> list = new ArrayList<>(List.of(this.g1_element_vector));
        int count = toIndex - fromIndex;
        for (int i = 0; i < count; i++) {
            list.remove(fromIndex);
        }
        this.g1_element_vector = list.toArray(new G1Element[0]);
    }


    // Public Functions and Methods

    public G1Element get(int index) {
        return doGet(index);
    }

    public byte[][] getBytes() {
        return doGetBytes();
    }

    public G1Element set(int index, G1Element e) {
        return doSet(index, e);
    }

    public boolean add(G1Element e) {
        modCount++;
        doAdd(e);
        return true;
    }

    public void add(int index, G1Element e) {
        modCount++;
        doAdd(index, e);
    }

    public G1Element remove(int index) {
        modCount++;
        return doRemove(index);
    }

    public int size() {
        return doSize();
    }

    public int capacity() {
        // public accessor for doSize
        // gets the total room available including unused indices
        return (int) Arrays.stream(this.g1_element_vector).filter(Objects::isNull).count();
    }

    public void reserve(int n) {
        // public accessor for reserve the size of n G1Elements in an array
        this.g1_element_vector = new G1Element[n];
    }

    public boolean isEmpty() {
        // public accessor for doSize == 0
        return this.g1_element_vector.length == 0;
    }

    public void clear() {
        // public accessor for removing all G1Elements from this g1_element_vector
        this.g1_element_vector = new G1Element[0];
    }

    public void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }


}