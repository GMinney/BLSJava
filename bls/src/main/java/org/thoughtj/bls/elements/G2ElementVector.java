package org.thoughtj.bls.elements;

import org.thoughtj.bls.DASHJBLSJNI;

public class G2ElementVector extends java.util.AbstractList<G2Element> implements java.util.RandomAccess {

    protected G2ElementVector(long cPtr, boolean cMemoryOwn) {

    }

    public G2ElementVector(G2Element[] initialElements) {
        this();
        reserve(initialElements.length);

        for (G2Element element : initialElements) {
            add(element);
        }
    }

    public G2ElementVector(Iterable<G2Element> initialElements) {
        this();
        for (G2Element element : initialElements) {
            add(element);
        }
    }

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

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

    public int size() {
        return doSize();
    }

    public G2ElementVector() {
        this(DASHJBLSJNI.new_G2ElementVector__SWIG_0(), true);
    }

    public G2ElementVector(G2ElementVector other) {

    }

    public int capacity() {

    }

    public void reserve(int n) {

    }

    public boolean isEmpty() {

    }

    public void clear() {

    }

    public G2ElementVector(int count, G2Element value) {

    }

    private int doSize() {

    }

    private void doAdd(G2Element x) {

    }

    private void doAdd(int index, G2Element x) {

    }

    private G2Element doRemove(int index) {

    }

    private G2Element doGet(int index) {

    }

    private G2Element doSet(int index, G2Element val) {

    }

    private void doRemoveRange(int fromIndex, int toIndex) {

    }

}
