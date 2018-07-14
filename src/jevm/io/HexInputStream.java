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
		byte[] bytes = new byte[10];
		HexInputStream hin = new HexInputStream(new StringReader("6080604052"));
		hin.read(bytes);
		System.out.println("READ: " + Arrays.toString(bytes));
		Bytecode bytecode = Bytecode.decode(bytes,0);
		System.out.println("GOT: " + bytecode);
	}
}
