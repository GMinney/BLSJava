package org.thoughtj.bls.utils;

public class Uint8Vector extends java.util.AbstractList<Short> implements java.util.RandomAccess {


    protected Uint8Vector(long cPtr, boolean cMemoryOwn) {

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

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

    public int size() {
        return doSize();
    }

    public Uint8Vector() {

    }

    public Uint8Vector(Uint8Vector other) {

    }

    public int capacity() {

    }

    public void reserve(int n) {

    }

    public boolean isEmpty() {

    }

    public void clear() {

    }

    public Uint8Vector(int count, short value) {

    }

    private int doSize() {

    }

    private void doAdd(short x) {

    }

    private void doAdd(int index, short x) {

    }

    private short doRemove(int index) {

    }

    private short doGet(int index) {

    }

    private short doSet(int index, short val) {

    }

    private void doRemoveRange(int fromIndex, int toIndex) {

    }

}