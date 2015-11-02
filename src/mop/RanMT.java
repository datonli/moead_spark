package mop;

import org.apache.commons.math.random.AbstractRandomGenerator;

public class RanMT extends AbstractRandomGenerator {

	private static final int N = 624;
	private static final int M = 397;
	private static final int MATRIX_A = 0x9908b0df; // private static final *
	// constant vector a
	private static final int UPPER_MASK = 0x80000000; // most significant w-r
	// bits
	private static final int LOWER_MASK = 0x7fffffff; // least significant r
	// bits

	// Tempering parameters
	private static final int TEMPERING_MASK_B = 0x9d2c5680;
	private static final int TEMPERING_MASK_C = 0xefc60000;

	private int mt[]; // the array for the state vector
	private int mti; // mti==N+1 means mt[N] is not initialized
	private int mag01[];

	public RanMT(long seedCount) {
		this.setSeed(System.currentTimeMillis()+seedCount);
	}
	
	@Override
	public double nextDouble() {
		int y;
		int z;

		if (mti >= N) { // generate N words at one time

			int kk;

			for (kk = 0; kk < N - M; kk++) {
				y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
			}
			for (; kk < N - 1; kk++) {
				y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
			}
			y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
			mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

			mti = 0;
		}

		y = mt[mti++];
		y ^= y >>> 11; // TEMPERING_SHIFT_U(y)
		y ^= (y << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(y)
		y ^= (y << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(y)
		y ^= (y >>> 18); // TEMPERING_SHIFT_L(y)

		if (mti >= N) { // generate N words at one time
			int kk;

			for (kk = 0; kk < N - M; kk++) {
				z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				mt[kk] = mt[kk + M] ^ (z >>> 1) ^ mag01[z & 0x1];
			}
			for (; kk < N - 1; kk++) {
				z = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				mt[kk] = mt[kk + (M - N)] ^ (z >>> 1) ^ mag01[z & 0x1];
			}
			z = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
			mt[N - 1] = mt[M - 1] ^ (z >>> 1) ^ mag01[z & 0x1];

			mti = 0;
		}

		z = mt[mti++];
		z ^= z >>> 11; // TEMPERING_SHIFT_U(z)
		z ^= (z << 7) & TEMPERING_MASK_B; // TEMPERING_SHIFT_S(z)
		z ^= (z << 15) & TEMPERING_MASK_C; // TEMPERING_SHIFT_T(z)
		z ^= (z >>> 18); // TEMPERING_SHIFT_L(z)

		/* derived from nextDouble documentation in jdk 1.2 docs, see top */
		return ((((long) (y >>> 6)) << 27) + (z >>> 5)) / (double) (1L << 53);
	}

	@Override
	public void setSeed(long seed) {
		mt = new int[N];

		mag01 = new int[2];
		mag01[0] = 0x0;
		mag01[1] = MATRIX_A;

		mt[0] = (int) (seed & 0xfffffff);
		for (mti = 1; mti < N; mti++) {
			mt[mti] = (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
			/* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
			/* In the previous versions, MSBs of the seed affect */
			/* only MSBs of the array mt[]. */
			mt[mti] &= 0xffffffff;
			/* for >32 bit machines */
		}

	}

}
