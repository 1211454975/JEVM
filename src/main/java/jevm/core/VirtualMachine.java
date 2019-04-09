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
		 * Identifies the execution status of a given contract.
		 */
		public enum Status {
			OK,
			STOP,
			REVERT,
			EXCEPTION
		}

		/**
		 * Get execution status of this contract.
		 *
		 * @return
		 */
		public Status status();

		/**
		 * Set the status of this machine
		 */
		public void halt(Status status);

		/**
		 * Update the program counter for this state.
		 *
		 * @param pc
		 */
		public void jump(int pc);

		/**
		 * Get the value of the program counter.
		 *
		 * @return
		 */
		public int pc();

		/**
		 * Get the value of the stack pointer.
		 *
		 * @return
		 */
		public int sp();

		/**
		 * Get the value of the memory pointer.
		 *
		 * @return
		 */
		public int mp();

		/**
		 * Read ith item off stack, where zero'th item is last pushed on.
		 *
		 * @return
		 */
		public w256 readStack(int ith);

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
		 * Peek a word from the given address on the stack
		 *
		 * @param address
		 * @return
		 */
		public w256 peek(int address);


		/**
		 * Get the size of the executing code contract (in bytes).
		 *
		 * @return
		 */
		public int codeSize();

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
		 * Ensure sufficient memory to access the given address. If the address does not
		 * already exist then it is initialised with zero.
		 *
		 * @param address
		 * @return Flag indicating whether was able to expand memory or not (e.g.
		 *         because hard limit encountered).
		 */
		public boolean expandMemory(w256 address);

		/**
		 * Read word from give address in memory.
		 *
		 * @param address
		 *            identifies word to read.
		 * @return
		 */
		public w256 readMemory(w256 address);

		/**
		 * Peek a word from the given address in memory.
		 *
		 * @param address
		 * @return
		 */
		public w256 peekMemory(int address);


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
