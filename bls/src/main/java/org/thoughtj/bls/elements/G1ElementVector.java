package org.thoughtj.bls.elements;

public class G1ElementVector extends java.util.AbstractList<G1Element> implements java.util.RandomAccess {

    protected G1ElementVector(long cPtr, boolean cMemoryOwn) {

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

    public G1Element get(int index) {
        return doGet(index);
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

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        doRemoveRange(fromIndex, toIndex);
    }

    public int size() {
        return doSize();
    }

    public G1ElementVector() {

    }

    public G1ElementVector(G1ElementVector other) {

    }

    public int capacity() {
;
    }

    public void reserve(int n) {

    }

    public boolean isEmpty() {

    }

    public void clear() {

    }

    public G1ElementVector(int count, G1Element value) {

    }

    private int doSize() {

    }

    private void doAdd(G1Element x) {

    }

    private void doAdd(int index, G1Element x) {

    }

    private G1Element doRemove(int index) {

    }

    private G1Element doGet(int index) {

    }

    private G1Element doSet(int index, G1Element val) {

    }

    private void doRemoveRange(int fromIndex, int toIndex) {

    }

}