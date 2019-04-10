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

import java.util.Arrays;

import jevm.core.Bytecode;
import jevm.core.VirtualMachine;
import jevm.util.Word.w256;

public class ArrayState implements VirtualMachine.State {
	/**
	 * Program Counter identifies next instruction to execute.
	 */
	private int pc;

	/**
	 * GAS counter identifies amount of remaining GAS.
	 */
	private int gas;

	/**
	 * The set of bytecodes associated with this state.
	 */
	private ByteArrayMemory code;

	/**
	 * Stack of 256bit words.
	 */
	private ArrayStack<w256> stack;
	/**
	 * Memory Pointer identifies first unused slot in memory
	 */
	private ArrayMemory<w256> memory;

	/**
	 * Memory Pointer identifies first unused slot in memory
	 */
	private ArrayMemory<w256> storage;

	/**
	 * Status identifies current execution status of this state.
	 */
	private VirtualMachine.State.Status status;

	public ArrayState(byte[] code) {
		this.status = VirtualMachine.State.Status.OK;
		this.code = new ByteArrayMemory(code);
		this.stack = new ArrayStack<>(w256.ZERO,new w256[0]);
		this.memory = new ArrayMemory<>(w256.ZERO,new w256[0]);
		this.storage = new ArrayMemory<>(w256.ZERO,new w256[0]);
	}

	@Override
	public Status status() {
		return status;
	}

	@Override
	public void halt(Status status) {
		this.status = status;
	}

	@Override
	public void jump(int pc) {
		this.pc = pc;
	}

	@Override
	public int pc() {
		return pc;
	}

	@Override
	public int gas() {
		return gas;
	}

	@Override
	public VirtualMachine.Memory<Byte> getCodeMemory() {
		return code;
	}

	@Override
	public VirtualMachine.Stack<w256> getStackMemory() {
		return stack;
	}

	@Override
	public VirtualMachine.Memory<w256> getLocalMemory() {
		return memory;
	}

	@Override
	public VirtualMachine.Memory<w256> getStorageMemory() {
		return storage;
	}

	@Override
	public String toString() {
		String r = status + ";" + stack + ";" + memory + ";" + storage;
		r += ";[";
		for(int i=0;i!=code.used();) {
			if(i != 0) {
				r += ",";
			}
			if(pc == i) {
				r += "*";
			}
			Bytecode.Opcode op = Bytecode.decode(code.peek(i));
			r += op;
			if(op.width > 1) {
				r += " 0x" + Hex.toBigEndianString(code.peekBytes(i + 1, i + op.width));
			}
			i = i + op.width;
		}
		return r + "]";
	}
}
