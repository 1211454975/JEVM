package jevm.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import jevm.core.Bytecode;

/**
 * Read a stream of hexadecimal strings and convert them into bytes. For
 * example, "<code>0a30ff</code>" gives the byte sequence
 * <code>[10,48,255]</code>.
 *
 * @author David J. Pearce
 *
 */
public class HexInputStream extends InputStream {
	private final Reader reader;

	public HexInputStream(Reader reader) {
		this.reader = reader;
	}

	@Override
	public int read() throws IOException {
		// Each byte requires two hexadecimal characters
		int first = Character.digit(reader.read(), 16);
		int second = Character.digit(reader.read(), 16);
		return (first << 4) | second;
	}

	public static void main(String[] args) throws IOException {
		byte[] bytes = new byte[256];
		HexInputStream hin = new HexInputStream(new StringReader("6080604052600080fd00a165627a7a72305820244b075b3bc74e154aad0d89366cf45d149c586f8a5fceaf7918a54fa41d3f520029"));
		hin.read(bytes);
		System.out.println("READ: " + Arrays.toString(bytes));
		Bytecode[] bytecodes = Bytecode.decode(bytes);
		for(Bytecode b : bytecodes) {
			System.out.println("GOT: " + b);
		}
	}
}
