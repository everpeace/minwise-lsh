package org.everpeace.search.lsh;

import java.util.TreeSet;

/**
 * convert String to Long (get a sign of string).
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class GramSigner {

	public static Long sign(String gram) {
		return hash(gram.getBytes());
	}

	public static TreeSet<Long> sign(TreeSet<String> grams) {
		TreeSet<Long> ret = new TreeSet<Long>();
		for (String gram : grams) {
			ret.add(hash(gram.getBytes()));
		}
		return ret;
	}

	// hash function from
	// http://www.javamex.com/tutorials/collections/strong_hash_code_implementation.shtml
	private static final long[] byteTable = createLookupTable();
	private static final long HSTART = 0xBB40E64DA205B064L;
	private static final long HMULT = 7664345821815920749L;

	public static long hash(byte[] data) {
		long h = HSTART;
		final long hmult = HMULT;
		final long[] ht = byteTable;
		for (int len = data.length, i = 0; i < len; i++) {
			h = (h * hmult) ^ ht[data[i] & 0xff];
		}
		return h;
	}

	private static final long[] createLookupTable() {
		long[] byteTable = new long[256];
		long h = 0x544B2FBACAAF1684L;
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 31; j++) {
				h = (h >>> 7) ^ h;
				h = (h << 11) ^ h;
				h = (h >>> 10) ^ h;
			}
			byteTable[i] = h;
		}
		return byteTable;
	}
}
