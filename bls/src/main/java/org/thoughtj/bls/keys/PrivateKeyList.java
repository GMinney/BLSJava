package org.thoughtj.bls.keys;

import org.thoughtj.bls.keys.PrivateKey;
import org.thoughtj.bls.keys.PrivateKeyVector;

import java.util.Arrays;
import java.util.List;

public class PrivateKeyList extends PrivateKeyVector {

    public PrivateKeyList() {
        super();
    }
    public PrivateKeyList(List<PrivateKey> list) {
        super(list);
    }

    public PrivateKeyList(PrivateKey [] array) {
        super(Arrays.asList(array));
    }

    public PrivateKeyList(PrivateKey first, PrivateKey... elements) {
        add(first);
        addAll(Arrays.asList(elements));
    }
}