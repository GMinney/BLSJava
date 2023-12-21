package org.thoughtj.bls.arith;

public abstract class Verify {

    public static boolean subgroup_check_E1 (Point point) {
        // checking that a point P exists within G1
        // using the LLL algorithm as in Fuentes et al.[5] and Budroni and Pintore[3].  https://eprint.iacr.org/2017/419
        // This can be computed using a single scalar multiplication
        // by z which is roughly a fourth of the size of q, and some other trivial group operations.
    }

    public static boolean subgroup_check_E2 (Point point) {

    }

}
