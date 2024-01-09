package org.thoughtj.bls.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.arith.Field;
import org.thoughtj.bls.arith.Params;
import org.thoughtj.bls.keys.PrivateKey;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.thoughtj.bls.elements.G2Element.SIZE;

public class G2ElementVector extends java.util.AbstractList<G2Element> implements java.util.RandomAccess {

    // Members
    private static Logger log = LoggerFactory.getLogger(G2ElementVector.class);

    private G2Element[] g2_element_vector;

    // Constructions

    public G2ElementVector() {
        this.g2_element_vector = new G2Element[1]; //(ByteBuffer.allocate(G2Element.SIZE).array());
    }

    public G2ElementVector(byte[] element_vector) {
        // split byte array into G2Elements by size
        int modulus = element_vector.length % G2Element.SIZE;
        // check valid
        if (modulus != 0){
            throw new RuntimeException("Mod check failed for vector. Not containing G2Elements with valid size");
        }
        // get count
        int count = element_vector.length / G2Element.SIZE;
        // preallocate array
        this.g2_element_vector = new G2Element[count];
        // add each G2Element from vector to array
        for (int i = 0; i < count; i++) {
            int offset = i * SIZE;
            G2Element item = G2Element.fromBytes(ByteBuffer.wrap(element_vector, offset, SIZE).array());
            this.g2_element_vector[i] = item;
        }
    }
    public G2ElementVector(int count, G2Element value) {
        // Instantiate a vector of value
        this();
        reserve(count);
        for (int i = 0; i<count-1; i++ ) {
            add(value);
        }
    }

    public G2ElementVector(G2ElementVector other) {
        this.g2_element_vector = other.g2_element_vector;
    }

    public G2ElementVector(G2Element[] initialElements) {
        this();
        reserve(initialElements.length);

        this.addAll(Arrays.asList(initialElements));
    }

    public G2ElementVector(Iterable<G2Element> initialElements) {
        this();
        for (G2Element element : initialElements) {
            add(element);
        }
    }


    // Private Functions and Methods

    private int doSize() {
        // get the count of G2Elements from this g2_element_vector
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        return this.g2_element_vector.length;
    }

    private void doAdd(G2Element x) {
        // add a G2Element to the set of G2Elements
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        int count = this.g2_element_vector.length;
        List<G2Element> list = new ArrayList<>(Arrays.asList(g2_element_vector));
        list.add(count - 1, x);
        this.g2_element_vector = list.toArray(new G2Element[0]);
    }

    private void doAdd(int index, G2Element x) {
        // add a G2Element to the set of G2Elements beginning at index
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        List<G2Element> list = new ArrayList<>(Arrays.asList(g2_element_vector));
        list.add(index, x);
        this.g2_element_vector = list.toArray(new G2Element[0]);
    }

    private G2Element doRemove(int index) {
        // remove a G2Element from this g2_element_vector
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        List<G2Element> list = new ArrayList<>(List.of(this.g2_element_vector));
        G2Element removed = list.remove(index);
        this.g2_element_vector = list.toArray(new G2Element[0]);
        return removed;
    }

    private G2Element doGet(int index) {
        // get a G2Element from this g2_element_vector
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        return this.g2_element_vector[index];
    }

    private G2Element doSet(int index, G2Element val) {
        // set a G2Element from this g2_element_vector
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        this.g2_element_vector[index] = val;
        return this.g2_element_vector[index];
    }

    private void doRemoveRange(int fromIndex, int toIndex) {
        // remove a range of G2Element from this g2_element_vector
        // remove a G2Element from this g2_element_vector
        if (g2_element_vector == null) {
            throw new RuntimeException("G2ElementVector is null");
        }
        List<G2Element> list = new ArrayList<>(List.of(this.g2_element_vector));
        int count = toIndex - fromIndex;
        for (int i = 0; i < count; i++) {
            list.remove(fromIndex);
        }
        this.g2_element_vector = list.toArray(new G2Element[0]);
    }


    // Public Functions and Methods
    public G2Element get(int index) {
        return doGet(index);
    }

    public G2Element set(int index, G2Element e) {
        return doSet(index, e);
    }

    public boolean add(G2Element e) {
        modCount++;
        doAdd(e);
        return true;
    }

    public void add(int index, G2Element e) {
        modCount++;
        doAdd(index, e);
    }

    public G2Element remove(int index) {
        modCount++;
        return doRemove(index);
    }

    public int size() {
        // public accessor for doSize
        return doSize();
    }

    public int capacity() {
        // public accessor for doSize
        // gets the total room available including unused indices
        return (int) Arrays.stream(this.g2_element_vector).filter(Objects::isNull).count();
    }

    public void reserve(int n) {
        // public accessor for reserve the size of n G2Elements in an array
        this.g2_element_vector = new G2Element[n];
    }

    public boolean isEmpty() {
        // public accessor for doSize == 0
        return this.g2_element_vector.length == 0;
    }

    public void clear() {
        // public accessor for removing all G2Elements from this g2_element_vector
        this.g2_element_vector = new G2Element[0];
    }

    public void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

}
