package org.thoughtj.bls.keys;

import java.util.ArrayList;
import java.util.Arrays;

public class PrivateKeyVector extends java.util.AbstractList<PrivateKey> implements java.util.RandomAccess {

    // Constructors

    public PrivateKeyVector() {

    }

    public PrivateKeyVector(PrivateKeyVector other) {

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

    }

    private void doAdd(PrivateKey x) {

    }

    private void doAdd(int index, PrivateKey x) {

    }

    private PrivateKey doRemove(int index) {

    }

    private PrivateKey doGet(int index) {

    }

    private PrivateKey doSet(int index, PrivateKey val) {

    }

    private void doRemoveRange(int fromIndex, int toIndex) {

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

    }

    public void reserve(int n) {

    }

    public boolean isEmpty() {

    }

    public void clear() {

    }

    public PrivateKeyVector(int count, PrivateKey value) {

    }



}