package org.everpeace.search.lsh.distance;

import java.util.Arrays;

/**
 * JaroWinkler distance function.
 */
public class JaroWinklerDistance {

	private final double mWeightThreshold;
	private final int mNumChars;

	/**
	 * Construct a basic Jaro string distance without the Winkler modifications.
	 * See the class documentation above for more information on the exact
	 * algorithm and its parameters.
	 */
	public JaroWinklerDistance() {
		this(Double.POSITIVE_INFINITY, 0);
	}

	/**
	 * Construct a Winkler-modified Jaro string distance with the specified
	 * weight threshold for refinement and an initial number of characters over
	 * which to reweight. See the class documentation above for more information
	 * on the exact algorithm and its parameters.
	 */
	public JaroWinklerDistance(double weightThreshold, int numChars) {
		mNumChars = numChars;
		mWeightThreshold = weightThreshold;
	}

	/**
	 * Returns the Jaro-Winkler distance between the specified character
	 * sequences. Teh distance is symmetric and will fall in the range
	 * <code>0</code> (perfect match) to <code>1</code> (no overlap). See the
	 * class definition above for formal definitions.
	 *
	 * <p>
	 * This method is defined to be:
	 *
	 * <pre>
	 *   distance(cSeq1,cSeq2) = 1 - proximity(cSeq1,cSeq2)</code>
	 * </pre>
	 *
	 * @param cSeq1
	 *            First character sequence to compare.
	 * @param cSeq2
	 *            Second character sequence to compare.
	 * @return The Jaro-Winkler comparison value for the two character
	 *         sequences.
	 */
	public double distance(CharSequence cSeq1, CharSequence cSeq2) {
		return 1.0 - proximity(cSeq1, cSeq2);
	}

	/**
	 * Return the Jaro-Winkler comparison value between the specified character
	 * sequences. The comparison is symmetric and will fall in the range
	 * <code>0</code> (no match) to <code>1</code> (perfect match)inclusive. See
	 * the class definition above for an exact definition of Jaro-Winkler string
	 * comparison.
	 *
	 * <p>
	 * The method {@link #distance(CharSequence,CharSequence)} returns a
	 * distance measure that is one minus the comparison value.
	 *
	 * @param cSeq1
	 *            First character sequence to compare.
	 * @param cSeq2
	 *            Second character sequence to compare.
	 * @return The Jaro-Winkler comparison value for the two character
	 *         sequences.
	 */
	public double proximity(CharSequence cSeq1, CharSequence cSeq2) {
		int len1 = cSeq1.length();
		int len2 = cSeq2.length();
		if (len1 == 0)
			return len2 == 0 ? 1.0 : 0.0;

		int searchRange = Math.max(0, Math.max(len1, len2) / 2 - 1);

		boolean[] matched1 = new boolean[len1];
		Arrays.fill(matched1, false);
		boolean[] matched2 = new boolean[len2];
		Arrays.fill(matched2, false);

		int numCommon = 0;
		for (int i = 0; i < len1; ++i) {
			int start = Math.max(0, i - searchRange);
			int end = Math.min(i + searchRange + 1, len2);
			for (int j = start; j < end; ++j) {
				if (matched2[j])
					continue;
				if (cSeq1.charAt(i) != cSeq2.charAt(j))
					continue;
				matched1[i] = true;
				matched2[j] = true;
				++numCommon;
				break;
			}
		}
		if (numCommon == 0)
			return 0.0;

		int numHalfTransposed = 0;
		int j = 0;
		for (int i = 0; i < len1; ++i) {
			if (!matched1[i])
				continue;
			while (!matched2[j])
				++j;
			if (cSeq1.charAt(i) != cSeq2.charAt(j))
				++numHalfTransposed;
			++j;
		}
		// System.out.println("numHalfTransposed=" + numHalfTransposed);
		int numTransposed = numHalfTransposed / 2;

		// System.out.println("numCommon=" + numCommon
		// + " numTransposed=" + numTransposed);
		double numCommonD = numCommon;
		double weight = (numCommonD / len1 + numCommonD / len2 + (numCommon - numTransposed)
				/ numCommonD) / 3.0;

		if (weight <= mWeightThreshold)
			return weight;
		int max = Math.min(mNumChars, Math.min(cSeq1.length(), cSeq2.length()));
		int pos = 0;
		while (pos < max && cSeq1.charAt(pos) == cSeq2.charAt(pos))
			++pos;
		if (pos == 0)
			return weight;
		return weight + 0.1 * pos * (1.0 - weight);

	}

	/**
	 * A constant for the Jaro distance. The value is the same as would be
	 * returned by the nullary constructor <code>JaroWinklerDistance()</code>.
	 *
	 * <p>
	 * Instances are thread safe, so this single distance instance may be used
	 * for all comparisons within an application.
	 */
	public static final JaroWinklerDistance JARO_DISTANCE = new JaroWinklerDistance();

	/**
	 * A constant for the Jaro-Winkler distance with defaults set as in
	 * Winkler's papers. The value is the same as would be returned by the
	 * nullary constructor <code>JaroWinklerDistance(0.7,4)</code>.
	 *
	 * <p>
	 * Instances are thread safe, so this single distance instance may be used
	 * for all comparisons within an application.
	 */
	public static final JaroWinklerDistance JARO_WINKLER_DISTANCE = new JaroWinklerDistance(
			0.70, 4);

}