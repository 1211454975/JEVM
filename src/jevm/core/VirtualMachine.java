package jevm.core;

import jevm.util.Word.w256;

/**
 * Provides various bits of machinery for executing EVM contracts.
 *
 * @author David J. Pearce
 *
 */
public class VirtualMachine {

	/**
	 * Represents the state of a contract executing in the virtual machine.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface State {
		/**
		 * Pop word of the stack.
		 *
		 * @return
		 */
		public w256 pop();

		/**
		 * Push word onto stack.
		 *
		 * @param value
		 */
		public void push(w256 value);

		/**
		 * Get the size of the executing code contract (in bytes).
		 *
		 * @return
		 */
		public w256 codeSize();

		/**
		 * Get code byte from executing contract.
		 *
		 * @param offset
		 * @return
		 */
		public byte readCode(int offset);

		/**
		 * Get the size of the active memory portion (in slots).
		 *
		 * @return
		 */
		public w256 memorySize();

		/**
		 * Read word from give address in memory.
		 *
		 * @param address
		 *            identifies word to read.
		 * @return
		 */
		public w256 readMemory(w256 address);

		/**
		 * Write word word to given address in memory.
		 *
		 * @param address
		 * @param value
		 * @return
		 */
		public boolean writeMemory(w256 address, w256 value);

		/**
		 * Read word from given address in storage.
		 *
		 * @param address
		 *            identifies word to read.
		 * @return
		 */
		public w256 readStorage(w256 address);

		/**
		 * Write word word to given address in storage.
		 *
		 * @param address
		 * @param value
		 * @return
		 */
		public boolean writeStorage(w256 address, w256 value);
	}
}
