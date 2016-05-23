package BloomFilter;

public class HashClass {

	private String url = null;
	private final int len;
	private int[] seedArray; // must be a 8 elements array
	private final int[] apHashSeed = { 3, 5, 7, 9, 11, 13, 17, 19, 23, 29, 31/* , 37, 41, 43, 47, 53 */ };

	private int[] resultArray;

	public HashClass(String url, int[] seeds) {
		this.url = url;
		this.seedArray = seeds;
		len = url.length();
		resultArray = new int[32];
	}

	private void runHashFunctions() {
		for (int i = 0; i < 4; i++) {
			resultArray[i * 8] = BKDRHash(i);
			resultArray[i * 8 + 1] = APHash(i);
			resultArray[i * 8 + 2] = DJBHash(i);
			resultArray[i * 8 + 3] = JSHash(i);
			resultArray[i * 8 + 4] = RSHash(i);
			resultArray[i * 8 + 5] = SDBHash(i);
			resultArray[i * 8 + 6] = PJWHash(i);
			resultArray[i * 8 + 7] = ELFHash(i);
		}
	}

	private int BKDRHash(int seedIndex) {
		int seed = seedArray[seedIndex];
		int hash = 0;
		for (int i = 0; i < len; i++) {
			hash = seed * hash + url.charAt(i);
		}
		return (hash & 0x7FFFFFFF);
	}

	private int APHash(int seedIndex) {
		int hash = 0;
		for (int i = 0; i < len; i++) {
			if ((i & 1) == 0) {
				hash ^= ((hash << apHashSeed[seedIndex]) ^ url.charAt(i) ^ (hash >> apHashSeed[seedIndex + 1]));
			} else {
				hash ^= (~((hash << apHashSeed[seedIndex + 2]) ^ url.charAt(i) ^ (hash >> apHashSeed[seedIndex + 3])));
			}
		}
		return (hash & 0x7FFFFFFF);
	}

	private int DJBHash(int seedIndex) {
		int hash = seedArray[seedIndex];
		for (int i = 0; i < len; i++) {
			hash += (hash << 5) + url.charAt(i);
		}
		return (hash & 0x7FFFFFFF);
	}

	private int JSHash(int seedIndex) {
		int hash = seedArray[seedIndex];
		for (int i = 0; i < len; i++) {
			hash ^= ((hash << 5) + url.charAt(i) + (hash >> 2));
		}
		return (hash & 0x7FFFFFFF);
	}

	private int RSHash(int seedIndex) {
		int b = apHashSeed[seedIndex];
		int a = apHashSeed[seedIndex + 1];
		int hash = 0;
		for (int i = 0; i < len; i++) {
			hash = hash * a + url.charAt(i);
			a = a * b;
		}
		return (hash & 0x7FFFFFFF);
	}

	private int SDBHash(int seedIndex) {
		int hash = 0;
		for (int i = 0; i < len; i++) {
			hash = (hash << apHashSeed[seedIndex]) + (hash << apHashSeed[seedIndex + 1]) - hash + url.charAt(i);
		}
		return (hash & 0x7FFFFFFF);
	}

	// no seed hash, it would be better to be replaced.
	private int PJWHash(int seedIndex) {
		int bitsInUnsighnedInt = 32;
		int threeQuarters = 24;
		int oneEight = 4;
		int highBits = (0xFFFFFFFF) << (bitsInUnsighnedInt - oneEight);
		int hash = 0;
		int test = 0;
		for (int i = 0; i < len; i++) {
			hash = (hash << oneEight) + url.charAt(i);
			if ((test = hash & highBits) != 0) {
				hash = ((hash ^ (test >> threeQuarters)) & (~highBits));
			}
		}
		return (hash & 0x7FFFFFFF);
	}

	private int ELFHash(int seedIndex) {
		int hash = 0;
		for (int i = 0; i < len; i++) {
			hash = (hash << apHashSeed[seedIndex]) + url.charAt(i);
			long g = hash & 0xF0000000L;
			if (g != 0) {
				hash ^= (g >> 24);
				hash &= (~g);
			}
		}
		return (hash & 0x7FFFFFFF);
	}

	/**
	 * get the 4 bytes finger print of URL
	 * 
	 * @return int[]
	 */
	public int[] getResult() {
		runHashFunctions();
		return resultArray;
	}
}
