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
		 * Get the amount of remaining GAS for this execution.
		 *
		 * @return
		 */
		public int gas();

		/**
		 * Get the value of the program counter.
		 *
		 * @return
		 */
		public int pc();

		/**
		 * Get the contract code associated with this contract.
		 *
		 * @return
		 */
		public Memory<Byte> getCodeMemory();

		/**
		 * Get the stack memory associated with this machine state.
		 * @return
		 */
		public Stack<w256> getStackMemory();

		/**
		 * Get the local memory associated with this machine state.
		 * @return
		 */
		public Memory<w256> getLocalMemory();
		/**
		 * Get the contract storage associated with this machine state.
		 * @return
		 */
		public Memory<w256> getStorageMemory();
	}

	/**
	 * Represents portion of memory in the machine, such as for local or contract
	 * storage.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Memory<T> {
		/**
		 * Read word from given address in memory.
		 *
		 * @param address
		 *            identifies word to read.
		 * @return
		 */
		public T read(w256 address);

		/**
		 * Write word word to given address in memory.
		 *
		 * @param address
		 * @param value
		 * @return
		 */
		public boolean write(w256 address, T value);

		/**
		 * Ensure sufficient storage to access the given address. If the address does not
		 * already exist then it is initialised with zero.
		 *
		 * @param address
		 * @return Flag indicating whether was able to expand storage or not (e.g.
		 *         because hard limit encountered).
		 */
		public boolean expand(w256 address);

		/**
		 * Get the size of the active memory portion (in slots).
		 *
		 * @return
		 */
		public w256 size();

		/**
		 * Get the amount of memory actually used (in bytes).
		 *
		 * @return
		 */
		public int used();

		/**
		 * Peek a word from the given address in memory
		 *
		 * @param address
		 * @return
		 */
		public T peek(int address);
	}

	public interface Stack<T> extends Memory<T> {

		/**
		 * Pop word of stack memory.
		 *
		 * @return
		 */
		public T pop();

		/**
		 * Push word onto stack memory.
		 *
		 * @param value
		 */
		public void push(T value);
	}
}
