// Copyright 2019 The JEVM Project Developers
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package jevm.util;

import java.math.BigInteger;
import java.util.Arrays;

public class Word {

	protected int[] ints;

	public Word(int n) {
		this.ints = new int[n];
	}

	protected Word(int n, byte[] bytes) {
		if (bytes.length > (n * 4)) {
			throw new IllegalArgumentException("invalid int array (too big)");
		}
		this.ints = fromBigEndianBytes(n, bytes);
	}

	protected Word(int n, int[] ints) {
		if (ints.length > n) {
			throw new IllegalArgumentException("invalid int array (too big)");
		}
		this.ints = new int[n];
		System.arraycopy(ints, 0, this.ints, n - ints.length, ints.length);
	}

	protected Word(int n, byte v) {
		this.ints = new int[n];
		this.ints[n-1] = v;
		fixedwidth_twoscomplement_signextend(ints,n-1);
	}

	protected Word(int n, short v) {
		this.ints = new int[n];
		this.ints[n-1] = v;
		fixedwidth_twoscomplement_signextend(ints,n-1);
	}

	protected Word(int n, int v) {
		this.ints = new int[n];
		this.ints[n-1] = v;
		fixedwidth_twoscomplement_signextend(ints,n-1);
	}

	protected Word(int n, long v) {
		this.ints = new int[n];
		this.ints[n-1] = (int) v;
		this.ints[n-2] = (int) (v >> 32);
		fixedwidth_twoscomplement_signextend(ints,n-2);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Word) {
			Word w = (Word) o;
			return Arrays.equals(ints, w.ints);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(ints);
	}

	public boolean isInt() {
		return fixedwidth_twoscomplement_isint(ints);
	}

	public int toInt() {
		// Return least-significant int
		return ints[ints.length-1];
	}

	@Override
	public String toString() {
		String r = "";
		for (int i = 0; i != ints.length; ++i) {
			int b1 = (ints[i] >> 24) & 0xff;
			int b2 = (ints[i] >> 16) & 0xff;
			int b3 = (ints[i] >> 8) & 0xff;
			int b4 = ints[i] & 0xff;
			r = r + String.format("%02X", b1);
			r = r + String.format("%02X", b2);
			r = r + String.format("%02X", b3);
			r = r + String.format("%02X", b4);
		}
		return "0x" + r;
	}

	/**
	 * Return an array of bytes in big endian form
	 *
	 * @return
	 */
	public byte[] toByteArray() {
		return toBigEndianBytes(ints);
	}

	/**
	 * Get a (signed) biginteger representation of this word
	 *
	 * @return
	 */
	public BigInteger toBigInteger() {
		byte[] bytes = toBigEndianBytes(ints);
		return new BigInteger(bytes);
	}

	public final static class w32 extends Word {

		public w32() {
			super(1);
		}

		public w32(byte[] bytes) {
			super(1, bytes);
		}

		public w32(int[] bytes) {
			super(1, bytes);
		}

		public w32(byte v) {
			super(1,v);
		}

		public w32(short v) {
			super(1,v);
		}

		public w32(int v) {
			super(1,v);
		}

		/**
		 * Increment by one.
		 */
		public w32 increment() {
			int[] result = fixedwidth_twoscomplement_increment(ints);
			return new w32(result);
		}

		public w32 add(w32 rhs) {
			int[] result = fixedwidth_twoscomplement_addition(ints, rhs.ints);
			return new w32(result);
		}

		public w32 subtract(w32 rhs) {
			// FIXME: could be more efficient
			return this.add(rhs.negate());
		}

		public w32 multiply(w32 rhs) {
			int[] result = fixedwidth_twoscomplement_multiplication(ints, rhs.ints);
			return new w32(result);
		}

		public w32 negate() {
			int[] result = fixedwidth_twoscomplement_negation(ints);
			return new w32(result);
		}

		public boolean signedLessThan(w32 rhs) {
			return fixedwidth_twoscomplement_lessthan(ints,rhs.ints);
		}

		public boolean unsignedLessThan(w32 rhs) {
			return fixedwidth_unsigned_lessthan(ints,rhs.ints);
		}

		public w32 and(w32 rhs) {
			int[] result = fixedwidth_twoscomplement_bitwiseand(ints,rhs.ints);
			return new w32(result);
		}

		public w32 or(w32 rhs) {
			int[] result = fixedwidth_twoscomplement_bitwiseor(ints,rhs.ints);
			return new w32(result);
		}

		public w32 xor(w32 rhs) {
			int[] result = fixedwidth_twoscomplement_bitwisexor(ints,rhs.ints);
			return new w32(result);
		}

		public w32 signExtend(int offset) {
			boolean sign = (ints[offset] & 0x80000000) != 0;
			if (sign) {
				int[] nbytes = Arrays.copyOf(ints, ints.length);
				fixedwidth_twoscomplement_signextend(ints, offset);
				return new w32(nbytes);
			} else {
				return this;
			}
		}
	}


	public final static class w160 extends Word {

		public w160() {
			super(20);
		}

		public w160(byte[] bytes) {
			super(5, bytes);
		}

		public w160(int[] bytes) {
			super(5, bytes);
		}

		public w160(byte v) {
			super(5,v);
		}

		public w160(short v) {
			super(5,v);
		}

		public w160(int v) {
			super(5,v);
		}

		public w160(long v) {
			super(5,v);
		}

		/**
		 * Increment by one.
		 */
		public w160 increment() {
			int[] result = fixedwidth_twoscomplement_increment(ints);
			return new w160(result);
		}

		public w160 add(w160 rhs) {
			int[] result = fixedwidth_twoscomplement_addition(ints, rhs.ints);
			return new w160(result);
		}

		public w160 subtract(w160 rhs) {
			// FIXME: could be more efficient
			return this.add(rhs.negate());
		}

		public w160 multiply(w160 rhs) {
			int[] result = fixedwidth_twoscomplement_multiplication(ints, rhs.ints);
			return new w160(result);
		}

		public w160 negate() {
			int[] result = fixedwidth_twoscomplement_negation(ints);
			return new w160(result);
		}

		public w160 signExtend(int offset) {
			boolean sign = (ints[offset] & 0x80000000) != 0;
			if (sign) {
				int[] nbytes = Arrays.copyOf(ints, ints.length);
				fixedwidth_twoscomplement_signextend(ints, offset);
				return new w160(nbytes);
			} else {
				return this;
			}
		}
	}

	public final static class w256 extends Word {

		public static final w256 ZERO = new w256(0);
		public static final w256 ONE = new w256(1);

		public w256() {
			super(8);
		}

		public w256(int[] ints) {
			super(8, ints);
		}

		public w256(byte[] bytes) {
			super(8, bytes);
		}

		public w256(byte v) {
			super(8,v);
		}

		public w256(short v) {
			super(8,v);
		}

		public w256(int v) {
			super(8,v);
		}

		public w256(long v) {
			super(8,v);
		}

		/**
		 * Increment by one.
		 */
		public w256 increment() {
			int[] result = fixedwidth_twoscomplement_increment(ints);
			return new w256(result);
		}

		public w256 add(w256 rhs) {
			int[] result = fixedwidth_twoscomplement_addition(ints, rhs.ints);
			return new w256(result);
		}

		public w256 subtract(w256 rhs) {
			// FIXME: could be more efficient
			return this.add(rhs.negate());
		}

		public w256 multiply(w256 rhs) {
			int[] result = fixedwidth_twoscomplement_multiplication(ints, rhs.ints);
			return new w256(result);
		}

		public w256 negate() {
			int[] result = fixedwidth_twoscomplement_negation(ints);
			return new w256(result);
		}

		public boolean signedLessThan(w256 rhs) {
			return fixedwidth_twoscomplement_lessthan(ints,rhs.ints);
		}

		public boolean unsignedLessThan(w256 rhs) {
			return fixedwidth_unsigned_lessthan(ints,rhs.ints);
		}

		public w256 and(w256 rhs) {
			int[] result = fixedwidth_twoscomplement_bitwiseand(ints,rhs.ints);
			return new w256(result);
		}

		public w256 or(w256 rhs) {
			int[] result = fixedwidth_twoscomplement_bitwiseor(ints,rhs.ints);
			return new w256(result);
		}

		public w256 xor(w256 rhs) {
			int[] result = fixedwidth_twoscomplement_bitwisexor(ints,rhs.ints);
			return new w256(result);
		}

		public w256 signExtend(int offset) {
			boolean sign = (ints[offset] & 0x80000000) != 0;
			if (sign) {
				int[] nbytes = Arrays.copyOf(ints, ints.length);
				fixedwidth_twoscomplement_signextend(ints, offset);
				return new w256(nbytes);
			} else {
				return this;
			}
		}
	}

	/**
	 * Perform a fixed-width two's complement increment (i.e. by one) with wrap
	 * around semantics.
	 *
	 * @param lhs
	 *            bytes for left-hand side in big endian form.
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_increment(int[] lhs) {
		int[] result = new int[lhs.length];
		// Set carry to one for increment
		long carry = 1;
		// Push carry all the way through whilst positive.
		for (int i = result.length - 1; i >= 0; --i) {
			long a = (lhs[i]) & 0xFFFFFFFFL;
			long b = a + carry;
			result[i] = (int) b;
			carry = (b & 0xF00000000L) == 0 ? 0L : 1L;
		}
		// Done
		return result;
	}

	/**
	 * Perform a fixed-width two's complement addition with wrap around semantics.
	 *
	 * @param lhs
	 *            bytes for left-hand side in big endian form.
	 * @param rhs
	 *            bytes for right-hand side in big endian form (must equal lhs
	 *            length)
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_addition(int[] lhs, int[] rhs) {
		int[] result = new int[lhs.length];
		long carry = 0;
		for (int i = result.length - 1; i >= 0; --i) {
			long a = (lhs[i]) & 0xFFFFFFFFL;
			long b = (rhs[i]) & 0xFFFFFFFFL;
			long c = a + b + carry;
			result[i] = (int) c;
			// Overflow detection
			carry = (c & 0xF00000000L) == 0 ? 0L : 1L;
		}
		return result;
	}

	/**
	 * Perform a fixed-width two's complement multiplication with wrap around semantics.
	 *
	 * @param lhs
	 *            bytes for left-hand side in big endian form.
	 * @param rhs
	 *            bytes for right-hand side in big endian form (must equal lhs
	 *            length)
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_multiplication(int[] lhs, int[] rhs) {
		// Determine sign of lhs
		boolean l_sign = lhs[0] < 0;
		// create space for result
		int[] result = new int[lhs.length];
		// process multiplication based on lhs
		if(l_sign) {
			// negate rhs
			rhs = fixedwidth_twoscomplement_negation(rhs);
			//
			while(!fixedwidth_bitwise_iszero(lhs)) {
				result = fixedwidth_twoscomplement_addition(result,rhs);
				lhs = fixedwidth_twoscomplement_increment(lhs);
			}
		} else {
			// negate lhs
			lhs = fixedwidth_twoscomplement_negation(lhs);
			//
			while(!fixedwidth_bitwise_iszero(lhs)) {
				result = fixedwidth_twoscomplement_addition(result,rhs);
				lhs = fixedwidth_twoscomplement_increment(lhs);
			}
		}
		// Done
		return result;
	}

	/**
	 * Perform a fixed-width twos complement negation. This is achieved by
	 * performing a bitwise inversion an increment simultaneously. Consider negating
	 * one:
	 *
	 * <pre>
	 * |     0     |     1     |     2     |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |00:00:00:00|00:00:00:00|00:00:00:01|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * </pre>
	 *
	 * We start by setting the carry to 1, and inverting the first byte:
	 *
	 * <pre>
	 * |     0     |     1     |     2     |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |00:00:00:00|00:00:00:00|00:00:00:FE|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * </pre>
	 *
	 * To this we add the carry which gives:
	 *
	 * <pre>
	 * |     0     |     1     |     2     |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |00:00:00:00|00:00:00:00|00:00:00:FF|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * </pre>
	 *
	 * At this point, the carry is now zero and we proceed by simply inverting all
	 * remaining bytes.
	 *
	 * @param lhs
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_negation(int[] lhs) {
		int[] result = new int[lhs.length];
		long carry = 1;
		for (int i = result.length - 1; i >= 0; --i) {
			// Make addition
			long a = (~lhs[i]) & 0xFFFFFFFFL;
			long b = a + carry;
			result[i] = (int) b;
			// Overflow detection
			carry = (b & 0xF00000000L) == 0 ? 0L : 1L;
		}
		return result;
	}

	/**
	 * Check whether one array is less than another. This is dictated primarily by
	 * the sign of the two operands.
	 *
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	private static boolean fixedwidth_twoscomplement_lessthan(int[] lhs, int[] rhs) {
		boolean l_sign = lhs[0] < 0;
		boolean r_sign = rhs[0] < 0;
		if (l_sign != r_sign) {
			return l_sign;
		} else {
			return fixedwidth_unsigned_lessthan(lhs, rhs);
		}
	}

	/**
	 * Check whether one int array is less than another. This is achieved by
	 * locating the most-significant integer which differs:
	 *
	 * <pre>
	 * |     0     |     1     |     2     |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |00:00:00:00|00:00:00:00|01:0b:00:10|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 *              !!
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |00:00:00:00|FF:00:00:00|01:0b:00:10|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * </pre>
	 *
	 * This integer determines whether or not one is less than the other.
	 *
	 * @param lhs
	 * @return
	 */
	private static boolean fixedwidth_unsigned_lessthan(int[] lhs, int[] rhs) {
		for (int i = 0; i != lhs.length; ++i) {
			int l = lhs[i];
			int r = rhs[i];
			if (l != r) {
				long ll = l & 0xFFFFFFFFL;
				long rr = r & 0xFFFFFFFFL;
				return ll < rr;
			}
		}
		// Not strictly less than
		return false;
	}

	/**
	 * Sign extend using a given index as the starting point. For example, consider
	 * this array (recall is stored in little endian):
	 *
	 * <pre>
	 * +--------+--------+--------+
	 * |00101100|10010101|00010000|
	 * +--------+--------+--------+
	 *     2        1        0
	 * </pre>
	 *
	 * If we sign extend from offset 0, then there is no extension. However, sign
	 * extending from offset 1 gives the following:
	 *
	 * <pre>
	 * +--------+--------+--------+
	 * |11111111|10010101|00010000|
	 * +--------+--------+--------+
	 *     2        1        0
	 * </pre>
	 *
	 * This occurs because the msb of the byte at offset 1 is set.
	 *
	 * @param ints
	 * @param msb
	 */
	private static void fixedwidth_twoscomplement_signextend(int[] ints, int offset) {
		// Check whether sign bit set or not
		boolean sign = (ints[offset] & 0x80000000) != 0;
		if (sign) {
			// Sign extension is required
			for (int i = 0; i < offset; ++i) {
				ints[i] = 0xFFFFFFFF;
			}
		}
	}

	/**
	 * Perform a fixed-width bitwise and.
	 *
	 * @param lhs
	 *            ints for left-hand side in big endian form.
	 * @param rhs
	 *            ints for right-hand side in big endian form (must equal lhs
	 *            length)
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_bitwiseand(int[] lhs, int[] rhs) {
		int[] result = new int[lhs.length];
		for (int i = 0 ; i < result.length; ++i) {
			result[i] = lhs[i] & rhs[i];
		}
		return result;
	}

	/**
	 * Perform a fixed-width bitwise or.
	 *
	 * @param lhs
	 *            ints for left-hand side in big endian form.
	 * @param rhs
	 *            ints for right-hand side in big endian form (must equal lhs
	 *            length)
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_bitwiseor(int[] lhs, int[] rhs) {
		int[] result = new int[lhs.length];
		for (int i = 0 ; i < result.length; ++i) {
			result[i] = lhs[i] | rhs[i];
		}
		return result;
	}

	/**
	 * Perform a fixed-width bitwise xor.
	 *
	 * @param lhs
	 *            bytes for left-hand side in big endian form.
	 * @param rhs
	 *            bytes for right-hand side in big endian form (must equal lhs
	 *            length)
	 * @return
	 */
	private static int[] fixedwidth_twoscomplement_bitwisexor(int[] lhs, int[] rhs) {
		int[] result = new int[lhs.length];
		for (int i = 0 ; i < result.length; ++i) {
			result[i] = lhs[i] & rhs[i];
		}
		return result;
	}

	/**
	 * Check whether a given array of integers is equivalent to zero or not.
	 *
	 * @param lhs
	 * @return
	 */
	private static boolean fixedwidth_bitwise_iszero(int[] lhs) {
		for(int i=0;i!=lhs.length;++i) {
			if(lhs[i] != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check whether a given array of integers corresponds to a single integer.
	 * There are two cases. The positive case looks like this:
	 *
	 * <pre>
	 * |     0     |     1     |     2     |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |00:00:00:00|00:00:00:00|01:0b:00:10|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * </pre>
	 *
	 * This corresponds to int 17498128. The key is that, since the most-significant
	 * bit of the least-significant int is zero, the remaining ints must all be
	 * zero. This contrasts with the negative case:
	 *
	 * <pre>
	 * |     0     |     1     |     2     |
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * |FF:FF:FF:FF|FF:FF:FF:FF|FF:0b:00:10|
	 * +--+--+--+--+--+--+--+--+--+--+--+--+
	 * </pre>
	 *
	 * This corresponds to int -16056304. Again, since the most-significant bit is
	 * one all the bits of the remaining ints must be one.
	 *
	 * @param ints
	 * @return
	 */
	private static boolean fixedwidth_twoscomplement_isint(int[] ints) {
		final int n = ints.length;
		// Determine whether result would be negative or not
		int msb = ints[n - 1] & 0x80000000;
		// Check remainder follows suit.
		if (msb == 0) {
			// Sanity check we're positive
			for (int i = 0; i < (n - 1); ++i) {
				if (ints[i] != 0) {
					return false;
				}
			}
		} else {
			// Sanity check we're negative
			for (int i = 0; i < (n - 1); ++i) {
				if (ints[i] != 0xFFFFFFFF) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Convert an array of ints into an array of bytes in big endian format.The
	 * alignment looks like this:
	 *
	 * <pre>
	 *     0       1       2       3
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * | : : : | : : : | : : : | : : : |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * :       :       :       :       :
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * | | | | | | | | | | | | | | | | |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 *  0 1 2 3 4 5 6 8 9 0 1 2 3 4 5 6
	 * </pre>
	 *
	 * The resulting array always has length a multiple of four.
	 *
	 * @param n
	 * @param bytes
	 * @return
	 */
	public static byte[] toBigEndianBytes(int[] ints) {
		byte[] bytes = new byte[ints.length * 4];
		for (int i = 0, j = 0; i != ints.length; ++i, j = j + 4) {
			int v = ints[i];
			bytes[j] = (byte) ((v >> 24) & 0xFF);
			bytes[j + 1] = (byte) ((v >> 16) & 0xFF);
			bytes[j + 2] = (byte) ((v >> 8) & 0xFF);
			bytes[j + 3] = (byte) (v & 0xFF);
		}
		return bytes;
	}

	/**
	 * Convert an array of bytes into an array of ints. The key challenge here is
	 * that the array may not be a multiple of four. Furthermore, the number of ints
	 * described maybe less than required (n). The alignment looks like this:
	 *
	 * <pre>
	 *     0       1       2       3
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * | : : : | : : : | : : : | : : : |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 *         :       :       :       :
	 *           +-+-+-+-+-+-+-+-+-+-+-+
	 *           | | | | | | | | | | | |
	 *           +-+-+-+-+-+-+-+-+-+-+-+
	 *            0 1 2 3 4 5 6 8 9 0 1
	 * </pre>
	 *
	 * We can imagine the byte array as being effectively padded with zeros to the
	 * left.
	 *
	 * @param n
	 * @param bytes
	 * @return
	 */
	public static int[] fromBigEndianBytes(int n, byte[] bytes) {
		int length = bytes.length / 4;
		int rem = bytes.length % 4;
		if(rem != 0) { length++; }
		int[] ints = new int[n];
		// First, do incomplete bytes
		int boff = 0;
		int ioff = n - length;
		switch (rem) {
		case 1:
			ints[ioff++] = bytes[boff++] & 0xFF;
			break;
		case 2: {
			int b1 = bytes[boff++] & 0xFF;
			int b2 = bytes[boff++] & 0xFF;
			ints[ioff++] = (b1 << 8) | b2;
			break;
		}
		case 3: {
			int b1 = bytes[boff++] & 0xFF;
			int b2 = bytes[boff++] & 0xFF;
			int b3 = bytes[boff++] & 0xFF;
			ints[ioff++] = (b1 << 16) | (b2 << 8) | b3;
			break;
		}
		}
		// Second, do all complete bytes
		for (int i = ioff; i < ints.length; ++i) {
			int b1 = bytes[boff++] & 0xFF;
			int b2 = bytes[boff++] & 0xFF;
			int b3 = bytes[boff++] & 0xFF;
			int b4 = bytes[boff++] & 0xFF;
			ints[i] = (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
		}
		// Done
		return ints;
	}

	public static long MIN = Integer.MIN_VALUE;
	public static long MAX = Integer.MAX_VALUE;
	public static long INC = (Integer.MAX_VALUE >> 20) - 1;

	public static void testIncrement() {
		System.out.println("*** TESTING INCREMENT");
		for (long i = MIN; i < MAX; i=i+INC) {
			w256 w = new w256(i).increment();
			w256 nw = new w256(i+1);
			if(!w.equals(nw)) {
				System.out.println("*** ERROR: " + w.toBigInteger() + " vs " + nw.toBigInteger());
			}
		}
	}

	public static void testNegation() {
		System.out.println("*** TESTING NEGATION");
		int count = 0;
		for (long i = MIN; i < MAX; i = i + INC) {
			w256 w = new w256(i).negate();
			w256 nw = new w256(-i);
			if(!w.equals(nw)) {
				System.out.println("*** ERROR: " + w.toBigInteger() + " vs " + nw.toBigInteger());
			}
			count = count + 1;
		}
		System.out.println("*** TESTED NEGATION (" + count + ")");
	}

	public static void testAddition() {
		System.out.println("*** TESTING ADDITION");
		for (long i = MIN; i < MAX; i = i + INC) {
			for (long j = MIN; j < MAX; j = j + INC) {
				w256 l = new w256(i);
				w256 r = new w256(j);
				w256 t = new w256(i+j);
				if(!l.add(r).equals(t)) {
					System.out.println("*** ERROR: " + l.toBigInteger() + " + " + r.toBigInteger() + " = " + l.add(r));
				}
			}
		}
	}


	public static void testMultiplication() {
		System.out.println("*** TESTING MULTIPLICATION");
		for (long i = MIN; i < MAX; i = i + INC) {
			for (long j = MIN; j < MAX; j = j + INC) {
				w256 l = new w256(i);
				w256 r = new w256(j);
				w256 t = new w256(i*j);
				if(!l.multiply(r).equals(t)) {
					System.out.println("*** ERROR: " + l.toBigInteger() + " * " + r.toBigInteger() + " = " + l.multiply(r).toBigInteger());
				}
			}
		}
	}

	public static void testUnsignedLessThan() {
		System.out.println("*** TESTING UNSIGNED LESS THAN");
		for (long i = 0; i < MAX; i = i + INC) {
			for (long j = 0; j < MAX; j = j + INC) {
				w256 l = new w256(i);
				w256 r = new w256(j);
				boolean b1 = i < j;
				boolean b2 = l.unsignedLessThan(r);
				if(b1 != b2) {
					System.out.println("*** ERROR: " + l.toBigInteger() + " < " + r.toBigInteger() + " = " + b2);
				}
			}
		}
	}

	public static void testSignedLessThan() {
		System.out.println("*** TESTING SIGNED LESS THAN");
		for (long i = MIN; i < MAX; i = i + INC) {
			for (long j = MIN; j < MAX; j = j + INC) {
				w256 l = new w256(i);
				w256 r = new w256(j);
				boolean b1 = i < j;
				boolean b2 = l.signedLessThan(r);
				if(b1 != b2) {
					System.out.println("*** ERROR: " + l.toBigInteger() + " < " + r.toBigInteger() + " = " + b2);
				}
			}
		}
	}

	public static void testToInt() {
		System.out.println("*** TESTING TOINT");
		for (long i = MIN; i < MAX; ++i) {
			w256 w = new w256(i);
			if (!w.isInt()) {
				System.out.println("*** ERROR: " + i + " (not int)");
			} else if (w.toInt() != i) {
				System.out.println("*** ERROR: " + i + " => " + w.toInt());
			}
		}
	}

	public static void main(String[] args) {
//		w32 w = new w32(128);
//		System.out.println("GOT: " + w.toBigInteger());
//		System.out.println("GOT: " + w.negate().toBigInteger());
//		System.out.println("GOT: " + w.add(w).toBigInteger());
//		testIncrement();
		//testAddition();
//		testMultiplication();
//		testNegation();
		testUnsignedLessThan();
		testSignedLessThan();
//		testToInt();
//		w32 w1 = new w32(123);
//		w32 w2 = new w32(Integer.MIN_VALUE);
//		System.out.println("GOT: " + w1.toBigInteger() + " < " + w2.toBigInteger() + " : " + w1.unsignedLessThan(w2));
//		System.out.println("DONE");
		//System.out.println(Long.toHexString(-4294967296L));
		//w256 w = new w256(-4294967296L);
		w256 w = new w256(0);
		w256 v = new w256(5);
		System.out.println(w + " < " + v + " = " + w.signedLessThan(v));
		System.out.println(v + " < " + w + " = " + v.signedLessThan(w));
	}
}
