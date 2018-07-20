package jevm.util;

import java.util.Arrays;

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
	 * Status identifies current execution status of this state.
	 */
	private VirtualMachine.State.Status status;

	public ArrayState(byte[] code) {
		this.code = code;
		this.sp = 0;
		this.stack = new w256[32];
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
	public w256 codeSize() {
		return new w256(code.length);
	}

	@Override
	public byte readCode(int offset) {
		return code[offset];
	}

	@Override
	public w256 memorySize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public w256 readMemory(w256 address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeMemory(w256 address, w256 value) {
		// TODO Auto-generated method stub
		return false;
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
		return sp + Arrays.toString(stack);
	}
}
