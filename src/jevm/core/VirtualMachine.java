package jevm.core;

import jevm.util.Number.i256;
import jevm.util.Number.u256;

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
		public i256 pop();

		/**
		 * Push word onto stack.
		 *
		 * @param value
		 */
		public void push(i256 value);

		/**
		 * Get the size of the executing code contract (in bytes).
		 *
		 * @return
		 */
		public u256 codeSize();

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
		public u256 memorySize();

		/**
		 * Read word from give address in memory.
		 *
		 * @param address
		 *            identifies word to read.
		 * @return
		 */
		public i256 readMemory(i256 address);

		/**
		 * Write word word to given address in memory.
		 *
		 * @param address
		 * @param value
		 * @return
		 */
		public boolean writeMemory(i256 address, i256 value);

		/**
		 * Read word from given address in storage.
		 *
		 * @param address
		 *            identifies word to read.
		 * @return
		 */
		public i256 readStorage(i256 address);

		/**
		 * Write word word to given address in storage.
		 *
		 * @param address
		 * @param value
		 * @return
		 */
		public boolean writeStorage(i256 address, i256 value);
	}
}
