package org.thoughtj.bls;

public class Uint8VectorVector extends java.util.AbstractList<Uint8Vector> implements java.util.RandomAccess {

    protected Uint8VectorVector(long cPtr, boolean cMemoryOwn) {

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

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

    public int size() {
        return doSize();
    }

    public Uint8VectorVector() {

    }

    public Uint8VectorVector(Uint8VectorVector other) {

    }

    public int capacity() {

    }

    public void reserve(int n) {

    }

    public boolean isEmpty() {

    }

    public void clear() {

    }

    public Uint8VectorVector(int count, Uint8Vector value) {

    }

    private int doSize() {

    }

    private void doAdd(Uint8Vector x) {

    }

    private void doAdd(int index, Uint8Vector x) {

    }

    private Uint8Vector doRemove(int index) {

    }

    private Uint8Vector doGet(int index) {

    }

    private Uint8Vector doSet(int index, Uint8Vector val) {

    }

    private void doRemoveRange(int fromIndex, int toIndex) {

    }

}