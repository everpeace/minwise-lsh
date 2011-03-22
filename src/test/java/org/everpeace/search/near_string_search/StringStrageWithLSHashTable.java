package org.everpeace.search.near_string_search;

import java.io.UnsupportedEncodingException;
import java.util.TreeSet;

import org.everpeace.search.lsh.LSHTable;
import org.everpeace.search.lsh.distance.JaroWinklerDistance;
import org.everpeace.search.lsh.distance.LevenshteinDistance;

/**
 * localith sensitive hash table using minwise hash function. (boosting with multiple tables)
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class StringStrageWithLSHashTable {
	LSHTable[] tables;

	public StringStrageWithLSHashTable(long[][] hashSeeds) {
		tables = new LSHTable[hashSeeds.length];
		for (int i = 0; i < hashSeeds.length; i++) {
			tables[i] = new LSHTable(hashSeeds[i]);
		}
	}

	public void register(String s) {
		for (LSHTable table : tables) {
			table.putWord(s);
		}
	}

	public TreeSet<String> getNearStringFromByLevenStein(String query, int th)
			throws UnsupportedEncodingException {
		TreeSet<String> ret = new TreeSet<String>();
		TreeSet<String> cands = getCandidatesFromLSHTables(query);
		System.out.println("scanned words:");
		for (String cand : cands) {
			// System.out.print(cand + " ");
			int d = LevenshteinDistance.calc(query, cand);
			System.out.print(cand + "<->" + query + ":" + d);
			if (d <= th) {
				ret.add(cand);
				System.out.println(" <<<FOUND!!");
			}
			System.out.println();
		}
		System.out.println("\n" + cands.size() + " words scanned.");
		return ret;
	}

	public TreeSet<String> getNearStringFromByJaroWinkler(String query,
			double th) throws UnsupportedEncodingException {
		TreeSet<String> ret = new TreeSet<String>();
		TreeSet<String> cands = getCandidatesFromLSHTables(query);
		System.out.println("scanned words:");
		for (String cand : cands) {
			// System.out.print(cand + " ");
			double d = JaroWinklerDistance.JARO_WINKLER_DISTANCE.distance(
					query, cand);
			System.out.print(cand + "<->" + query + ":" + d);
			if (d <= th) {
				ret.add(cand);
				System.out.println(" <<<FOUND!!");
			}
			System.out.println();
		}
		System.out.println("\n" + cands.size() + " words scanned.");
		return ret;
	}

	private TreeSet<String> getCandidatesFromLSHTables(String query) {
		TreeSet<String> cands = new TreeSet<String>();

		for (LSHTable table : tables) {
			cands.addAll(table.getCandidates(query));
		}

		return cands;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (LSHTable t : tables) {
			sb.append("###   HASH TABLE #" + i + "  ###\n");
			sb.append(t.toString());
			sb.append("\n");
			i++;
		}
		return sb.toString();
	}
}
