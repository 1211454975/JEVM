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

public class ArrayStack<T> extends ArrayMemory<T> implements VirtualMachine.Stack<T> {

	public ArrayStack(T value, T[] data) {
		super(value,data);
	}

	@Override
	public T pop() {
		return read(--fp);
	}

	@Override
	public void push(T value) {
		if(memory.length <= fp) {
			// Expand the underlying array
			T[] expanded = Arrays.copyOf(memory, Math.max(fp, (1 + memory.length) * 2));
			// Ensure all elements have default value
			Arrays.fill(expanded, memory.length, expanded.length, value);
			// copy over the new array
			this.memory = expanded;
		}
		memory[fp++] = value;
	}
}
