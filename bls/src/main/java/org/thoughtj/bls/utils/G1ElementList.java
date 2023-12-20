package org.thoughtj.bls.utils;

import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;

import java.util.Arrays;
import java.util.List;

public class G1ElementList extends G1ElementVector {

    public G1ElementList() {
        super();
    }
    public G1ElementList(List<G1Element> list) {
        super(list);
    }

    public G1ElementList(G1Element [] array) {
        super(Arrays.asList(array));
    }

    public G1ElementList(G1Element first, G1Element... elements) {
        add(first);
        addAll(Arrays.asList(elements));
    }
}