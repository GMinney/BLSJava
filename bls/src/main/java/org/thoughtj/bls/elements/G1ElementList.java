package org.thoughtj.bls.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thoughtj.bls.elements.G1Element;
import org.thoughtj.bls.elements.G1ElementVector;
import org.thoughtj.bls.keys.PrivateKey;

import java.util.Arrays;
import java.util.List;

public class G1ElementList extends G1ElementVector {

    private static Logger log = LoggerFactory.getLogger(G1ElementList.class);

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