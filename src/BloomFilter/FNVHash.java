package BloomFilter;

public class FNVHash {
	private final int FNV_PRIME = 16777619; // 32 bits prime
	private final long OFFSET_BASIS = 2166136261L; // 32 bits offset
	private final int hash_bits = 32;
	private final int[] originPosition;
	private int[] result;

	/**
	 * Constructor calculate 32 hash values by FNV hash method
	 * 
	 * @param position
	 *            original 32 hash values
	 */
	public FNVHash(int[] position) {
		this.originPosition = position;
		result = new int[hash_bits];
	}

	/**
	 * FNV1 hash method process all print values to get result, call getResult()
	 */
	public void FNV1() {
		for (int index = 0; index < hash_bits; index++) {
			long hash = OFFSET_BASIS;
			int value = originPosition[index];
			for (int i = 0; i < hash_bits / 8; i++) {
				byte byteValue = getByteFromInteger(i, value);
				hash = hash * (long) FNV_PRIME % (1L << 32);
				hash ^= byteValue;
			}
			result[index] = (int) hash;
		}
	}

	/**
	 * FNV_1 hash function
	 * 
	 * @param index
	 * @return
	 *
	 * 		public int FNV1(int index){ long hash = OFFSET_BASIS; int value =
	 *         originPosition[index];
	 * 
	 *         for(int i = 0; i < hash_bits / 8; i++){ byte byteValue =
	 *         getByteFromInteger(i, value); hash = hash * (long)FNV_PRIME % (1l
	 *         << 32); hash ^= byteValue; }
	 * 
	 *         return (int)hash; }
	 */

	/**
	 * FNV1a hash method process all print values to get result, call
	 * getResult()
	 */
	public void FNV1a() {
		for (int index = 0; index < hash_bits; index++) {
			long hash = OFFSET_BASIS;
			int value = originPosition[index];
			for (int i = 0; i < hash_bits / 8; i++) {
				byte bytevalue = getByteFromInteger(i, value);
				hash ^= bytevalue;
				hash = hash * (long) FNV_PRIME % (1L << 32);
			}
			result[index] = (int) hash;
		}
	}

	/**
	 * FNV_1a hash function
	 * 
	 * @param index
	 * @return
	 *
	 * 		public int FNV1a(int index){ long hash = OFFSET_BASIS; int value
	 *         = originPosition[index];
	 * 
	 *         for(int i = 0; i < hash_bits / 8; i++){ byte byteValue =
	 *         getByteFromInteger(i, value); hash ^= byteValue; hash = hash *
	 *         (long)FNV_PRIME % (1L<<32); } return (int)hash; }
	 */

	/**
	 * a Integer has 4 bytes or 32 bits this function aim to get the byte in
	 * position index
	 * 
	 * @param index
	 *            indicates to get which byte of the 4 bytes it count from left
	 *            to right.
	 * @return the target byte in position index
	 */
	public byte getByteFromInteger(int index, int value) {
		int cutLeft = (value << (index * 8));
		int moveToRight = (cutLeft >> ((hash_bits / 8 - 1 - index) * 8));
		byte targetByte = (byte) moveToRight;
		return targetByte;
	}// 需要测试

	/**
	 * get FNV result
	 * 
	 * @return
	 */
	public int[] getResult() {
		if (result == null)
			return null;
		return result;
	}
}