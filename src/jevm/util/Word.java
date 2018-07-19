package jevm.util;

import java.math.BigInteger;
import java.util.Arrays;

public class Word {

	protected byte[] bytes;

	public Word(int n) {
		this.bytes = new byte[n];
	}

	protected Word(int n, byte[] bytes) {
		if (bytes.length > n) {
			throw new IllegalArgumentException("invalid byte array (too big)");
		}
		this.bytes = new byte[n];
		System.arraycopy(bytes, 0, this.bytes, n - bytes.length, bytes.length);
	}

	protected Word(int n, byte v) {
		this.bytes = new byte[n];
		this.bytes[n-1] = (byte) (v & 0xFF);
		fixedwidth_twoscomplement_signextend(bytes,n-1);
	}

	protected Word(int n, short v) {
		this.bytes = new byte[n];
		this.bytes[n-1] = (byte) (v & 0xFF);
		this.bytes[n-2] = (byte) ((v >> 8) & 0xFF);
		fixedwidth_twoscomplement_signextend(bytes,n-2);
	}

	protected Word(int n, int v) {
		this.bytes = new byte[n];
		this.bytes[n-1] = (byte) (v & 0xFF);
		this.bytes[n-2] = (byte) ((v >> 8) & 0xFF);
		this.bytes[n-3] = (byte) ((v >> 16) & 0xFF);
		this.bytes[n-4] = (byte) ((v >> 24) & 0xFF);
		fixedwidth_twoscomplement_signextend(bytes,n-4);
	}

	protected Word(int n, long v) {
		this.bytes = new byte[n];
		this.bytes[n-1] = (byte) (v & 0xFF);
		this.bytes[n-2] = (byte) ((v >> 8) & 0xFF);
		this.bytes[n-3] = (byte) ((v >> 16) & 0xFF);
		this.bytes[n-4] = (byte) ((v >> 24) & 0xFF);
		this.bytes[n-5] = (byte) ((v >> 32) & 0xFF);
		this.bytes[n-6] = (byte) ((v >> 40) & 0xFF);
		this.bytes[n-7] = (byte) ((v >> 48) & 0xFF);
		this.bytes[n-8] = (byte) ((v >> 56) & 0xFF);
		fixedwidth_twoscomplement_signextend(bytes,n-8);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Word) {
			Word w = (Word) o;
			return Arrays.equals(bytes, w.bytes);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(bytes);
	}

	@Override
	public String toString() {
		String r = "";
		for (int i = 0; i != bytes.length; ++i) {
			int b = bytes[i] & 0xff;
			r = r + String.format("%02X", b);
		}
		return "0x" + r;
	}

	public BigInteger toBigInteger() {
		return new BigInteger(bytes);
	}

	public final static class w32 extends Word {

		public w32() {
			super(4);
		}

		public w32(byte[] bytes) {
			super(4, bytes);
		}

		public w32(byte v) {
			super(4,v);
		}

		public w32(short v) {
			super(4,v);
		}

		public w32(int v) {
			super(4,v);
		}

		public w32 add(w32 rhs) {
			byte[] result = fixedwidth_twoscomplement_addition(bytes, rhs.bytes);
			return new w32(result);
		}

		public w32 subtract(w32 rhs) {
			// FIXME: could be more efficient
			return this.add(rhs.negate());
		}

		public w32 negate() {
			byte[] result = fixedwidth_twoscomplement_negation(bytes);
			return new w32(result);
		}

		public w32 signExtend(int offset) {
			boolean sign = (bytes[offset] & 0x80) != 0;
			if (sign) {
				byte[] nbytes = Arrays.copyOf(bytes, bytes.length);
				fixedwidth_twoscomplement_signextend(bytes, offset);
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
			super(20, bytes);
		}

		public w160(byte v) {
			super(20,v);
		}

		public w160(short v) {
			super(20,v);
		}

		public w160(int v) {
			super(20,v);
		}

		public w160(long v) {
			super(20,v);
		}

		public w160 add(w160 rhs) {
			byte[] result = fixedwidth_twoscomplement_addition(bytes, rhs.bytes);
			return new w160(result);
		}

		public w160 subtract(w160 rhs) {
			// FIXME: could be more efficient
			return this.add(rhs.negate());
		}

		public w160 negate() {
			byte[] result = fixedwidth_twoscomplement_negation(bytes);
			return new w160(result);
		}

		public w160 signExtend(int offset) {
			boolean sign = (bytes[offset] & 0x80) != 0;
			if (sign) {
				byte[] nbytes = Arrays.copyOf(bytes, bytes.length);
				fixedwidth_twoscomplement_signextend(bytes, offset);
				return new w160(nbytes);
			} else {
				return this;
			}
		}
	}

	public final static class w256 extends Word {

		public w256() {
			super(32);
		}

		public w256(byte[] bytes) {
			super(32, bytes);
		}

		public w256(byte v) {
			super(32,v);
		}

		public w256(short v) {
			super(32,v);
		}

		public w256(int v) {
			super(32,v);
		}

		public w256(long v) {
			super(32,v);
		}

		public w256 add(w256 rhs) {
			byte[] result = fixedwidth_twoscomplement_addition(bytes, rhs.bytes);
			return new w256(result);
		}

		public w256 subtract(w256 rhs) {
			// FIXME: could be more efficient
			return this.add(rhs.negate());
		}

		public w256 negate() {
			byte[] result = fixedwidth_twoscomplement_negation(bytes);
			return new w256(result);
		}

		public w256 signExtend(int offset) {
			boolean sign = (bytes[offset] & 0x80) != 0;
			if (sign) {
				byte[] nbytes = Arrays.copyOf(bytes, bytes.length);
				fixedwidth_twoscomplement_signextend(bytes, offset);
				return new w256(nbytes);
			} else {
				return this;
			}
		}
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
	private static byte[] fixedwidth_twoscomplement_addition(byte[] lhs, byte[] rhs) {
		byte[] result = new byte[lhs.length];
		int carry = 0;
		for (int i = result.length - 1; i >= 0; --i) {
			int v = (lhs[i] & 0xFF) + (rhs[i] & 0xFF) + carry;
			result[i] = (byte) v;
			carry = (v & 0xffffff00) == 0 ? 0 : 1;
		}
		return result;
	}

	private static byte[] fixedwidth_twoscomplement_negation(byte[] lhs) {
		byte[] result = new byte[lhs.length];
		int carry = 1;
		for (int i = result.length - 1; i >= 0; --i) {
			int v = ((~lhs[i]) & 0xFF) + carry;
			result[i] = (byte) v;
			carry = (v & 0xffffff00) == 0 ? 0 : 1;
		}
		return result;
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
	 * @param bytes
	 * @param msb
	 */
	private static void fixedwidth_twoscomplement_signextend(byte[] bytes, int offset) {
		// Check whether sign bit set or not
		boolean sign = (bytes[offset] & 0x80) != 0;
		if (sign) {
			// Sign extension is required
			for (int i = 0; i < offset; ++i) {
				bytes[i] = (byte) 0xFF;
			}
		}
	}

	public static int MIN = java.lang.Short.MIN_VALUE;
	public static int MAX = java.lang.Short.MAX_VALUE;

	public static void testNegation() {
		System.out.println("*** TESTING NEGATION");
		for (int i = MIN; i < MAX; ++i) {
			w32 w = new w32(i);
			w32 nw = new w32(-i);
			if(!w.negate().equals(nw)) {
				System.out.println("GOT: " + w.toBigInteger() + " vs " + nw.toBigInteger());
			}
		}
	}

	public static void testAddition() {
		System.out.println("*** TESTING ADDITION");
		for (int i = MIN; i < MAX; ++i) {
			for (int j = MIN; j < MAX; ++j) {
				w32 l = new w32(i);
				w32 r = new w32(j);
				w32 t = new w32(i+j);
				if(!l.add(r).equals(t)) {
					System.out.println("GOT: " + l.toBigInteger() + " + " + r.toBigInteger() + " = " + l.add(r));
				}
			}
		}
	}

	public static void main(String[] args) {
//		Word32 w = new Word32(128);
//		System.out.println("GOT: " + w.toBigInteger());
//		System.out.println("GOT: " + w.negate().toBigInteger());
//		System.out.println("GOT: " + w.add(w).toBigInteger());
		//testNegation();
		testAddition();
		System.out.println("DONE");
	}
}
