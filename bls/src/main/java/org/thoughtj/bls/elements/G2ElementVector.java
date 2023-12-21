package org.thoughtj.bls.elements;

public class G2ElementVector extends java.util.AbstractList<G2Element> implements java.util.RandomAccess {


    // Constructions
    public G2ElementVector(int count, G2Element value) {

    }

    public G2ElementVector(G2ElementVector other) {

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



    // Functions and Methods
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

    public int capacity() {

    }

    public void reserve(int n) {

    }

    public boolean isEmpty() {

    }

    public void clear() {

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
