package org.thoughtj.bls.utils;

import org.thoughtj.bls.elements.G2Element;
import org.thoughtj.bls.elements.G2ElementVector;

import java.util.Arrays;
import java.util.List;

public class G2ElementList extends G2ElementVector {

    public G2ElementList() {
        super();
    }
    public G2ElementList(List<G2Element> list) {
        super(list);
    }

    public G2ElementList(G2Element [] array) {
        super(Arrays.asList(array));
    }

    public G2ElementList(G2Element first, G2Element... elements) {
        add(first);
        addAll(Arrays.asList(elements));
    }
}