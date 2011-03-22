package org.everpeace.search.lsh;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.everpeace.search.lsh.galois.GaloisField64;

/**
 * MinWise Permutaiton Function.
 * Minwise permutations are represented by polynomials on GF(2^64).
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class MinWisePermutationFunction {
    public static final GaloisField64 gf = GaloisField64.DEFAULT_GALOIS_FIELD64;
    private int depth = 2;

    public Long returnHashValue(final TreeSet<Integer> featureVector, final long[] seed) {
        Set<Long> conv = new HashSet<Long>();
        for (Integer i : featureVector) {
            conv.add((long) Integer.valueOf(i));
        }
        return returnHashValue(conv, seed);
    }

    public Long returnHashValue(final Set<Long> featureVector, final long[] seed) {
        TreeMap<Long, Long> hashedFeatureVector = this.createHashedVector(featureVector, seed);
        long clusterId = 0;
        for (int i = 0; i < this.depth; i++) {
            if (hashedFeatureVector.size() <= 0) {
                return clusterId;
            }
            Long minimum = hashedFeatureVector.firstKey();
            clusterId += (minimum + (i * 13));
            hashedFeatureVector.remove(minimum);
        }
        return clusterId;
    }

    private TreeMap<Long, Long> createHashedVector(final Set<Long> featureVector,
            final long[] seed) {
        TreeMap<Long, Long> hashedFeatureVector = new TreeMap<Long, Long>();

        for (Long key : featureVector) {
            hashedFeatureVector.put(calcHash(key, seed), key);
        }
        return hashedFeatureVector;
    }

    /**
     * \sum_{i} a[i]x<sup>i</sup> on GF(2^64)
     *
     * @param x
     * @param a
     * @return
     */
    private Long calcHash(final long x, final long[] a) {
        long ret = 0;
        for (int i = 0; i < a.length; i++) {
            ret = gf.add(ret, gf.mul(a[i], gf.pow(x, i)));
        }
        return ret;
    }
}