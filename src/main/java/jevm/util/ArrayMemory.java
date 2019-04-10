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

/**
 * An array-oriented implementation of Memory.
 *
 * @author David J. Pearce
 *
 * @param <T>
 */
public class ArrayMemory<T> implements VirtualMachine.Memory<T> {

	/**
	 * The initial value used for all locations.
	 */
	protected T value;

	/**
	 * Free pointer
	 */
	protected int fp;

	/**
	 * Memory slots.
	 */
	protected T[] memory;

	public ArrayMemory(T value, T[] data) {
		this.value = value;
		this.memory = data;
	}

	@Override
	public T read(w256 address) {
		if(address.isInt()) {
			int addr = address.toInt();
			// NOTE: negative address caught here
			return memory[addr];
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public boolean write(w256 address, T value) {
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
					// Expand the underlying array
					T[] expanded = Arrays.copyOf(memory, Math.max(fp, (1 + memory.length) * 2));
					// Ensure all elements have default value
					Arrays.fill(expanded, memory.length, expanded.length, value);
					// copy over the new array
					this.memory = expanded;
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
	public T peek(int address) {
		return memory[address];
	}

	@Override
	public String toString() {
		System.out.println("FP: " + fp);
		String r = "[";
		for(int i=0;i!=fp;++i) {
			if(i != 0) {
				r += ",";
			}
			r += memory[i];
		}
		return r + "]";
	}
}
