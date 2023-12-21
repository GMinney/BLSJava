package org.thoughtj.bls.elements;

public class GTElement {

    // A GTElement is a public key that is 48 bytes in size or 384 bits
    // GT: a subgroup, of prime order r, of the multiplicative group of a field extension.
    // e : G1 x G2 -> GT: a non-degenerate bilinear map. e: the optimal Ate pairing
    // Operations in GT are written in multiplicative notation, i.e., a * b is field multiplication.
    // G1, G2: subgroups of E1 and E2 (respectively) having prime order r
    // E1, E2: elliptic curve groups defined over finite fields. This document assumes that E1 has a more compact representation than E2, i.e., because E1 is defined over a smaller field than E2.

    public final static int SIZE = 48;

    public static GTElement fromBytes(byte[] bytes) {
        return null;
    }

    public static GTElement fromBytesUnchecked(byte[] bytes) {
        return null;
    }

    public static GTElement unity() {
        return null;
    }

    public void serialize(byte[] buffer) {

    }

    public byte[] serialize() {
        return null;
    }

}
