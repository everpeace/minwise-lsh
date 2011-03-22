package org.everpeace.search.lsh.galois;

import net.goui.util.MTRandom;

import org.everpeace.search.lsh.galois.GaloisField64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * test of Galois Field(GF(2^64).
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class GaloisField64Test {

	GaloisField64 testee = GaloisField64.DEFAULT_GALOIS_FIELD64;

	@Test
	public final void testMul() {
		// x^63+x^62+x
		long a = (0x1L << 63) | (0x1L << 62) | (0x1L << 1);
		// x^2 +x
		long b = (0x1L << 2) | (0x1L << 1);
		// x^63+x^5+x^4+x^2+x
		long r_ans = (0x1L << 63) | (0x1L << 5) | (0x1L << 4) | (0x1L << 3)
				| (0x1L << 1);
		System.out.print("(" + testee.polynomialRepresentationOf(a) + ")");
		System.out.println("(" + testee.polynomialRepresentationOf(b) + ")");
		long ans = testee.mul(a, b);
		System.out.println("=" + testee.polynomialRepresentationOf(ans));
		assertEquals(r_ans, ans);
	}

	@Test
	public final void testAdd() {
		Long ans = testee.add(1L, 1L);
		assertEquals(Long.valueOf(0L), ans);
	}

	@Test
	public final void testMul_root() {
		assertEquals(2L, testee.mul_root(1L));
	}

	@Test
	public final void testPerformance() {
		int num = 1000;
		long seed = 19791208;
		MTRandom mt = new MTRandom(seed);

		long[][] tab = new long[num][2];

		for (int i = 0; i < num; i++) {
			tab[i][0] = mt.nextInt();
			tab[i][1] = mt.nextInt();
		}

		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			System.out.print("(" + testee.polynomialRepresentationOf(tab[i][0])
					+ ")");
			System.out.println("("
					+ testee.polynomialRepresentationOf(tab[i][1]) + ")");
			// @SuppressWarnings("unused")
			long ans = testee.mul(tab[i][0], tab[i][1]);
			System.out.println(" = (" + testee.polynomialRepresentationOf(ans)
					+ ")");
		}
		long end = System.currentTimeMillis();

		System.out.println("total: " + (end - start) + "[ms] (" + num
				+ " times)");
		System.out.println("average: " + (end - start) / num + "[ms]");
	}
}
