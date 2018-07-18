package jevm.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import jevm.core.Bytecode;

/**
 * Various utilities for dealing with hexadecimal ASCII data.
 *
 * @author David J. Pearce
 *
 */
public class Hex {
	/**
	 * Read a stream of hexadecimal strings and convert them into bytes. For
	 * example, "<code>0a30ff</code>" gives the byte sequence
	 * <code>[10,48,255]</code>.
	 *
	 * @author David J. Pearce
	 *
	 */
	public static class InputStream extends java.io.InputStream {
		private final Reader reader;

		public InputStream(Reader reader) {
			this.reader = reader;
		}

		@Override
		public int read() throws IOException {
			// Each byte requires two hexadecimal characters
			int first = Character.digit(reader.read(), 16);
			int second = Character.digit(reader.read(), 16);
			return (first << 4) | second;
		}
	}


	/**
	 * Convert a sequence of bytes into a hexadecimal string.
	 *
	 * @param bytes
	 * @return
	 */
	public static String toBigEndianString(byte[] bytes) {
		String r = "";
		for(int i=0;i!=bytes.length;++i) {
			int b = bytes[i] & 0xff;
			r = r + String.format("%02X",b);
		}
		return r;
	}

	/**
	 * Give a sequence of hexadecimal characters return an array of bytes. If the
	 * sequence is not divisible by two an exception is thrown.
	 *
	 * @param string
	 * @return
	 */
	public static byte[] fromBigEndianString(String string) {
		if (string.length() % 2 != 0) {
			throw new IllegalArgumentException("length of hex string not multiple of two");
		}
		byte[] data = new byte[string.length() / 2];
		for (int i = 0; i != data.length; ++i) {
			int pos = i << 1;
			int first = Character.digit(string.charAt(pos), 16);
			int second = Character.digit(string.charAt(pos + 1), 16);
			data[i] = (byte) ((first << 4) | second);
		}
		return data;
	}

	public static void main(String[] args) throws IOException {
		byte[] bytes = fromBigEndianString("60806040526000805460ff1916607b179055348015601c57600080fd5b50603580602a6000396000f3006080604052600080fd00a165627a7a72305820e0643a302559e78256b8973d9ead587f53365c97427b0b57956bfa80a9ef0dc40029");
		System.out.println("READ: " + Arrays.toString(bytes));
		for(int i=0;i<bytes.length;) {
			Bytecode.Opcode opcode = Bytecode.decode(bytes[i]);
			System.out.print("GOT: " + opcode);
			if(opcode.width > 1) {
				byte[] operand = Arrays.copyOfRange(bytes, i + 1, i + opcode.width);
				System.out.print(" 0x" + toBigEndianString(operand));
			}
			System.out.println();
			i = i + opcode.width;
		}
	}
}
