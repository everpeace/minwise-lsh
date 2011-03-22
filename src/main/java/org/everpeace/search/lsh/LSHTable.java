package org.everpeace.search.lsh;

import java.util.TreeMap;
import java.util.TreeSet;


/**
 * locality sensitive hashing table for a minwise hash function.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class LSHTable {
    private long[] hashSeed;
    MinWisePermutationFunction mwf = new MinWisePermutationFunction();
    TreeMap<Long, TreeSet<String>> table = new TreeMap<Long, TreeSet<String>>();

    /**
     * @param hashSeed
     */
    public LSHTable(long[] hashSeed) {
        super();
        this.hashSeed = hashSeed;
    }

    public void putWord(String s) {
        TreeSet<String> grams = NGramGen.generate(s);
        Long key = mwf.returnHashValue(GramSigner.sign(grams), hashSeed);
        if (table.get(key) == null) {
            TreeSet<String> entry = new TreeSet<String>();
            table.put(key, entry);
        }
        TreeSet<String> entry = table.get(key);
        entry.add(s);
    }

    public TreeSet<String> getCandidates(String query) {
        TreeSet<String> ans = new TreeSet<String>();
        TreeSet<String> grams = NGramGen.generate(query);
        Long key = mwf.returnHashValue(GramSigner.sign(grams), hashSeed);
        if (table.get(key) != null) {
            ans = table.get(key);
        }
        return ans;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hash Function: " + hashSeed[0] + "x + " + hashSeed[1] + "\n");
        for (Long l : table.keySet()) {
            sb.append(l + ": ");
            for (String s : table.get(l)) {
                sb.append(s + ",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
