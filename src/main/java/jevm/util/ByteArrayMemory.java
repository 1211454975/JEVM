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

import jevm.core.VirtualMachine;
import jevm.util.Word.w256;

public class ByteArrayMemory implements VirtualMachine.Memory<Byte> {
	/**
	 * Free pointer
	 */
	protected int fp;

	/**
	 * Memory bytes.
	 */
	private byte[] memory;

	/**
	 * Construct a memory initialised with the given array of bytes. The free memory
	 * pointer is set to the number of bytes.
	 *
	 * @param data
	 */
	public ByteArrayMemory(byte[] data) {
		this.memory = data;
		this.fp = data.length;
	}

	@Override
	public Byte read(w256 address) {
		if(address.isInt()) {
			int addr = address.toInt();
			// NOTE: negative address caught here
			return memory[addr];
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public boolean write(w256 address, Byte value) {
		int addr = address.toInt();
		if(address.isInt()) {
			memory[addr] = value;
			return true;
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public boolean expand(w256 address) {
		int addr = address.toInt();
		if(address.isInt()) {
			if(fp <= addr) {
				fp = addr + 1;
				if(addr >= memory.length) {
					memory = Arrays.copyOf(memory, Math.max(fp,memory.length * 2));
				}
			}
			return true;
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public w256 size() {
		return new w256(fp);
	}

	@Override
	public int used() {
		return fp;
	}

	@Override
	public Byte peek(int address) {
		return memory[address];
	}

	public byte[] peekBytes(int start, int end) {
		return Arrays.copyOfRange(memory, start, end);
	}
}
