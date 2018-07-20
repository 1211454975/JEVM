package jevm.util;

import java.util.Arrays;

import jevm.core.Bytecode;
import jevm.core.VirtualMachine;
import jevm.util.Word.w256;

public class ArrayState implements VirtualMachine.State {
	/**
	 * The set of bytecodes associated with this state.
	 */
	private final byte[] code;
	/**
	 * Program Counter identifies next instruction to execute.
	 */
	private int pc;
	/**
	 * Stack Pointer identifies next free slot on the stack.
	 */
	private int sp;
	/**
	 * Stack of 256bit words.
	 */
	private w256[] stack;
	/**
	 * Memory Pointer identifies first unused slot in memory
	 */
	private int mp;
	/**
	 * Memory consisting of 256bit words.
	 */
	private w256[] memory;
	/**
	 * Status identifies current execution status of this state.
	 */
	private VirtualMachine.State.Status status;

	public ArrayState(byte[] code) {
		this.status = VirtualMachine.State.Status.OK;
		this.code = code;
		this.sp = 0;
		this.stack = new w256[32];
		this.mp = 0;
		this.memory = new w256[32];
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
	public int sp() {
		return sp;
	}

	@Override
	public int mp() {
		return mp;
	}


	@Override
	public w256 pop() {
		return stack[--sp];
	}

	@Override
	public void push(w256 value) {
		if(sp == stack.length) {
			stack = Arrays.copyOf(stack, stack.length * 2);
		}
		stack[sp++] = value;
	}

	@Override
	public w256 peek(int i) {
		return stack[i];
	}

	@Override
	public int codeSize() {
		return code.length;
	}

	@Override
	public byte readCode(int offset) {
		return code[offset];
	}

	@Override
	public w256 memorySize() {
		return new w256(mp);
	}

	@Override
	public w256 readMemory(w256 address) {
		if(address.isInt()) {
			int addr = address.toInt();
			// NOTE: negative address caught here
			return memory[addr];
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public boolean expandMemory(w256 address) {
		int addr = address.toInt();
		if(address.isInt()) {
			if(mp <= addr) {
				mp = addr + 1;
				if(addr >= memory.length) {
					memory = Arrays.copyOf(memory, Math.max(mp,memory.length * 2));
				}
			}
			return true;
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public boolean writeMemory(w256 address, w256 value) {
		int addr = address.toInt();
		if(address.isInt()) {
			memory[addr] = value;
			return true;
		} else {
			throw new IllegalArgumentException("invalid memory address");
		}
	}

	@Override
	public w256 peekMemory(int i) {
		return memory[i];
	}

	@Override
	public w256 readStorage(w256 address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeStorage(w256 address, w256 value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		String r = status + ";[";
		for(int i=0;i!=sp;++i) {
			if(i != 0) {
				r += ",";
			}
			r += stack[i];
		}
		r += "];[";
		for(int i=0;i!=mp;++i) {
			if(i != 0) {
				r += ",";
			}
			r += memory[i];
		}
		r += "];[";
		for(int i=0;i!=code.length;) {
			if(i != 0) {
				r += ",";
			}
			if(pc == i) {
				r += "*";
			}
			Bytecode.Opcode op = Bytecode.decode(code[i]);
			r += op;
			if(op.width > 1) {
				r += " 0x"+Hex.toBigEndianString(Arrays.copyOfRange(code, i+1, i+op.width));
			}
			i = i + op.width;
		}
		return r + "]";
	}
}
