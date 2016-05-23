/**
 * 32 bits bloom filter
 * @author vito
 * time	2016-5-9
 */
package BloomFilter;

import java.util.BitSet;

public class BloomFilter {

	private static BitSet bitSet = null;
	private static int bitsetLen = 0;

	private static int hash_bits = 32;
	private static int[] seed = { 2147483629, 2147483587, 2147483579, 2147483563, 
			2147483549, 2147483543, 2147483497, 2147483489 };

	/**
	 * Constructor initialize BitSet that bloom filter needed bitsetLen should
	 * be set by user, an appropriate length of BitSet will lower the conflict
	 * of bloom filter.
	 * 
	 * @param length
	 *            length of BitSet
	 * @param url
	 *            the URL to be checked
	 *
	 *            public BloomFilter(int length, String url){ if(bitsetLen == 0
	 *            ){ BloomFilter.bitsetLen = length; BloomFilter.bitSet = new
	 *            BitSet(bitsetLen); } hash = new int[hash_bits]; this.url =
	 *            url; }
	 */

	/**
	 * Constructor only initialize the static BitSet and BitSetLength it should
	 * be called before any other constructors
	 * 
	 * @param length
	 *            the length of BitSet
	 */
	public BloomFilter(int length) {
		BloomFilter.bitsetLen = length;
		BloomFilter.bitSet = new BitSet(BloomFilter.bitsetLen);
	}

	/**
	 * get the 32 finger prints of URL
	 */
	public synchronized static int[] calFingerPrint(String url) {
		try {
			HashClass h = new HashClass(url, seed);
			int[] hash = h.getResult();
			FNVHash fnvH = new FNVHash(hash);
			fnvH.FNV1();
			hash = fnvH.getResult();
			return hash;
		} catch (Exception e) {
			System.out.println("URL " + url + " calculate finger print failed.");
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * add this.url in to BitSet
	 * 
	 */
	public synchronized static void add(int[] hash ) {
		for (int i = 0; i < hash_bits; i++) {
			bitSet.set(hash[i]);
		}
		return;
	}

	/**
	 * check if this.url is exists in BitSet.
	 * 
	 * @return
	 */
	public synchronized static boolean check( int[] hash ) {
		for (int i = 0; i < hash_bits; i++) {
			if ( !bitSet.get(hash[i])) {
				return false;
			}
		}
		return true;
	}
}