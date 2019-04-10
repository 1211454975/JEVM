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

import java.util.Arrays;
import java.util.Stack;

import jevm.core.VirtualMachine.State.Status;
import jevm.util.ArrayState;
import jevm.util.Hex;
import jevm.util.Word.w256;

/**
 * Provides an encoding of the information contained in "Appendix H" of the
 * yellow paper.
 *
 * @author David J. Pearce
 *
 */
public class Bytecode {

	public static final int G_zero = 0;
	public static final int G_base = 2;
	public static final int G_verylow = 3;
	public static final int G_low = 5;
	public static final int G_mid = 8;
	public static final int G_high = 10;
	public static final int G_extcode = 700;
	public static final int G_balance = 400;
	public static final int G_sload = 200;
	public static final int G_jumpdest = 1;
	public static final int G_sset = 20000;
	public static final int G_sreset = 5000;
	public static final int R_sclear = 15000;
	public static final int R_selfdestruct = 24000;
	public static final int G_selfdestruct = 5000;
	public static final int G_create = 32000;
	public static final int G_codedeposit = 200;
	public static final int G_call = 700;
	public static final int G_callvalue = 9000;
	public static final int G_callstipend = 2300;
	public static final int G_newaccount = 25000;
	public static final int G_exp = 10;
	public static final int G_expbyte = 50;
	public static final int G_memory = 3;
	public static final int G_txcreate = 32000;
	public static final int G_txdatazero = 4;
	public static final int G_txdatanonzero = 68;
	public static final int G_transaction = 21000;
	public static final int G_log = 375;
	public static final int G_logdata = 8;
	public static final int G_logtopic = 375;
	public static final int G_sha3 = 30;
	public static final int G_sha3word = 6;
	public static final int G_copy = 3;
	public static final int G_blockhash = 20;
	public static final int G_quaddivisor = 100;

	// 0s: Stop and Arithmetic Operations
	public static final int STOP = 0x0;
	public static final int ADD = 0x01;
	public static final int MUL = 0x02;
	public static final int SUB = 0x03;
	public static final int DIV = 0x04;
	public static final int SDIV = 0x05;
	public static final int MOD = 0x06;
	public static final int SMOD = 0x07;
	public static final int ADDMOD = 0x08;
	public static final int MULMOD = 0x09;
	public static final int EXP = 0x0a;
	public static final int SIGNEXTEND = 0x0b;
	// 10s: Comparison & Bitwise Logic Operations
	public static final int LT = 0x10;
	public static final int GT = 0x11;
	public static final int SLT = 0x12;
	public static final int SGT = 0x13;
	public static final int EQ = 0x14;
	public static final int ISZERO = 0x15;
	public static final int AND = 0x16;
	public static final int OR = 0x17;
	public static final int XOR = 0x18;
	public static final int NOT = 0x19;
	public static final int BYTE = 0x1a;
	// 20s: SHA3
	public static final int SHA3 = 0x20;
	// 30s: Environment Information
	public static final int ADDRESS = 0x30;
	public static final int BALANCE = 0x31;
	public static final int ORIGIN = 0x32;
	public static final int CALLER = 0x33;
	public static final int CALLVALUE = 0x34;
	public static final int CALLDATALOAD = 0x35;
	public static final int CALLDATASIZE = 0x36;
	public static final int CALLDATACOPY = 0x37;
	public static final int CODESIZE = 0x38;
	public static final int CODECOPY = 0x39;
	public static final int GASPRICE = 0x3a;
	public static final int EXTCODESIZE = 0x3b;
	public static final int EXTCODECOPY = 0x3c;
	public static final int RETURNDATASIZE = 0x3d;
	public static final int RETURNDATACOPY = 0x3e;
	// 40s: Block Information
	public static final int BLOCKHASH = 0x40;
	public static final int COINBASE = 0x41;
	public static final int TIMESTAMP = 0x42;
	public static final int NUMBER = 0x43;
	public static final int DIFFICULTY = 0x44;
	public static final int GASLIMIT = 0x45;
	// 50s: Stack, Memory Storage and Flow Operations
	public static final int POP = 0x50;
	public static final int MLOAD = 0x51;
	public static final int MSTORE = 0x52;
	public static final int MSTORE8 = 0x53;
	public static final int SLOAD = 0x54;
	public static final int SSTORE = 0x55;
	public static final int JUMP = 0x56;
	public static final int JUMPI = 0x57;
	public static final int PC = 0x58;
	public static final int MSIZE = 0x59;
	public static final int GAS = 0x5a;
	public static final int JUMPDEST = 0x5b;
	// 60s & 70s: Push Operations
	public static final int PUSH1 = 0x60;
	public static final int PUSH2 = 0x61;
	public static final int PUSH3 = 0x62;
	public static final int PUSH4 = 0x63;
	public static final int PUSH5 = 0x64;
	public static final int PUSH6 = 0x65;
	public static final int PUSH7 = 0x66;
	public static final int PUSH8 = 0x67;
	public static final int PUSH9 = 0x68;
	public static final int PUSH10 = 0x69;
	public static final int PUSH11 = 0x6a;
	public static final int PUSH12 = 0x6b;
	public static final int PUSH13 = 0x6c;
	public static final int PUSH14 = 0x6d;
	public static final int PUSH15 = 0x6e;
	public static final int PUSH16 = 0x6f;
	public static final int PUSH17 = 0x70;
	public static final int PUSH18 = 0x71;
	public static final int PUSH19 = 0x72;
	public static final int PUSH20 = 0x73;
	public static final int PUSH21 = 0x74;
	public static final int PUSH22 = 0x75;
	public static final int PUSH23 = 0x76;
	public static final int PUSH24 = 0x77;
	public static final int PUSH25 = 0x78;
	public static final int PUSH26 = 0x79;
	public static final int PUSH27 = 0x7a;
	public static final int PUSH28 = 0x7b;
	public static final int PUSH29 = 0x7c;
	public static final int PUSH30 = 0x7d;
	public static final int PUSH31 = 0x7e;
	public static final int PUSH32 = 0x7f;
	// 80s: Duplication Operations
	public static final int DUP1 = 0x80;
	public static final int DUP2 = 0x81;
	public static final int DUP3 = 0x82;
	public static final int DUP4 = 0x83;
	public static final int DUP5 = 0x84;
	public static final int DUP6 = 0x85;
	public static final int DUP7 = 0x86;
	public static final int DUP8 = 0x87;
	public static final int DUP9 = 0x88;
	public static final int DUP10 = 0x89;
	public static final int DUP11 = 0x8a;
	public static final int DUP12 = 0x8b;
	public static final int DUP13 = 0x8c;
	public static final int DUP14 = 0x8d;
	public static final int DUP15 = 0x8e;
	public static final int DUP16 = 0x8f;
	// 90s: Exchange Operations
	public static final int SWAP1 = 0x90;
	public static final int SWAP2 = 0x91;
	public static final int SWAP3 = 0x92;
	public static final int SWAP4 = 0x93;
	public static final int SWAP5 = 0x94;
	public static final int SWAP6 = 0x95;
	public static final int SWAP7 = 0x96;
	public static final int SWAP8 = 0x97;
	public static final int SWAP9 = 0x98;
	public static final int SWAP10 = 0x99;
	public static final int SWAP11 = 0x9a;
	public static final int SWAP12 = 0x9b;
	public static final int SWAP13 = 0x9c;
	public static final int SWAP14 = 0x9d;
	public static final int SWAP15 = 0x9e;
	public static final int SWAP16 = 0x9f;
	// a0s: Logging Operations
	public static final int LOG0 = 0xa0;
	public static final int LOG1 = 0xa1;
	public static final int LOG2 = 0xa2;
	public static final int LOG3 = 0xa3;
	public static final int LOG4 = 0xa4;
	// f0s: System operations
	public static final int CREATE = 0xf0;
	public static final int CALL = 0xf1;
	public static final int CALLCODE = 0xf2;
	public static final int RETURN = 0xf3;
	public static final int DELEGATECALL = 0xf4;
	public static final int STATICCALL = 0xfa;
	public static final int REVERT = 0xfd;
	public static final int INVALID = 0xfe;
	public static final int SELFDESTRUCT = 0xff;
	/**
	 * Provides useful information on all bytecodes.
	 *
	 * @author David J. Pearce
	 *
	 */
	public enum Opcode {
		// 0s: Stop and Arithmetic Operations
		STOP(Bytecode.STOP, 0, 0, "Halts execution.",1),
		//
		ADD(Bytecode.ADD, 2, 1, "Addition operation.",1),
		//
		MUL(Bytecode.MUL, 2, 1, "Multiplication operation.",1),
		//
		SUB(Bytecode.SUB, 2, 1, "Subtraction operation.",1),
		//
		DIV(Bytecode.DIV, 2, 1, "Integer division operation.",1),
		//
		SDIV(Bytecode.SDIV, 2, 1, "Signed integer division operation (Bytecode.truncated).",1),
		//
		MOD(Bytecode.MOD, 2, 1, "Modulo remainder operation.",1),
		//
		SMOD(Bytecode.SMOD, 2, 1, "Signed modulo remainder operation.",1),
		//
		ADDMOD(Bytecode.ADDMOD, 3, 1, "Modulo addition operation.",1),
		//
		MULMOD(Bytecode.MULMOD, 3, 1, "Modulo multiplication operation.",1),
		//
		EXP(Bytecode.EXP, 2, 1, "Exponential Operation.",1),
		//
		SIGNEXTEND(Bytecode.SIGNEXTEND, 2, 1, "Extend length of two's complement signed integer.",1),
		// 10s: Comparison & Bitwise Logic Operations
		LT(Bytecode.LT, 2, 1, "Less-than comparison.",1),
		//
		GT(Bytecode.GT, 2, 1, "Greater-than comparison.",1),
		//
		SLT(Bytecode.SLT, 2, 1, "Signed less-than comparison.",1),
		//
		SGT(Bytecode.SGT, 2, 1, "Signed greater-than comparison.",1),
		//
		EQ(Bytecode.EQ, 2, 1, "Equality comparison.",1),
		//
		ISZERO(Bytecode.ISZERO, 1, 1, "Simple not operator.",1),
		//
		AND(Bytecode.AND, 2, 1, "Bitwise AND operation.",1),
		//
		OR(Bytecode.OR, 2, 1, "Bitwise OR operation.",1),
		//
		XOR(Bytecode.XOR, 2, 1, "Bitwise XOR operation.",1),
		//
		NOT(Bytecode.NOT, 1, 1, "Bitwise NOT operation.",1),
		//
		BYTE(Bytecode.BYTE, 2, 1, "Retrieves single byte from word.",1),
		// 20s: SHA3
		SHA3(Bytecode.SHA3, 2, 1, "Compute Keccak-256 hash.",1),
		// 30s: Environment Information
		ADDRESS(Bytecode.ADDRESS, 0, 1, "Get address of currently executing account.",1),
		//
		BALANCE(Bytecode.BALANCE, 1, 1, "Get balance of the given account.",1),
		//
		ORIGIN(Bytecode.ORIGIN, 0, 1, "Get execution origination address.",1),
		//
		CALLER(Bytecode.CALLER, 0, 1,
				"Get caller address.  This is the address of the account that is directly responsible for this execution.",1),
		//
		CALLVALUE(Bytecode.CALLVALUE, 0, 1, "Get deposited value by the instruction/transaction responsible for this execution.",1),
		//
		CALLDATALOAD(Bytecode.CALLDATALOAD, 1, 1,
				"Get input data of current environment.  This pertains to the input data passed with the message call instruction or transaction.",1),
		//
		CALLDATASIZE(Bytecode.CALLDATASIZE, 0, 1, "Get size of input data in current environment.",1),
		//
		CALLDATACOPY(Bytecode.CALLDATACOPY, 3, 1,
				"Copy input data in current environment to memory.  This pertains to the input data passed with the message call instruction or transaction.",1),
		//
		CODESIZE(Bytecode.CODESIZE, 0, 1, "Get size of code running in current environment.",1),
		//
		CODECOPY(Bytecode.CODECOPY, 3, 0, "Copy code running in current environment to memory.",1),
		//
		GASPRICE(Bytecode.GASPRICE, 0, 1,
				"Get price of gas in current environment.  This is gas price speci\fed by the originating transaction.",1),
		//
		EXTCODESIZE(Bytecode.EXTCODESIZE, 1, 1, "Get size of an account's code.",1),
		//
		EXTCODECOPY(Bytecode.EXTCODECOPY, 4, 0, "Copy an account's code to memory.",1),
		//
		RETURNDATASIZE(Bytecode.RETURNDATASIZE, 0, 1, "Get size of output data from the previous call from the current environment.",1),
		//
		RETURNDATACOPY(Bytecode.RETURNDATACOPY, 3, 0, "Copy output data from the previous call to memory.",1),
		// 40s: Block Information
		BLOCKHASH(Bytecode.BLOCKHASH, 1, 1, "Get the hash of one of the 256 most recent complete blocks.",1),
		//
		COINBASE(Bytecode.COINBASE, 0, 1, "Get the block's benefciary address.",1),
		//
		TIMESTAMP(Bytecode.TIMESTAMP, 0, 1, "Get the block's timestamp.",1),
		//
		NUMBER(Bytecode.NUMBER, 0, 1, "Get the block's number.",1),
		//
		DIFFICULTY(Bytecode.DIFFICULTY, 0, 1, "Get the block's difficulty.",1),
		//
		GASLIMIT(Bytecode.GASLIMIT, 0, 1, "Get the block's gas limit.",1),
		// 50s: Stack, Memory Storage and Flow Operations
		POP(Bytecode.POP, 1, 0, "Remove item from stack.",1),
		//
		MLOAD(Bytecode.MLOAD, 1, 1, "Load word from memory.",1),
		//
		MSTORE(Bytecode.MSTORE, 2, 0, "Save word to memory.",1),
		//
		MSTORE8(Bytecode.MSTORE8, 2, 0, "Save byte to memory.",1),
		//
		SLOAD(Bytecode.SLOAD, 1, 1, "Load word from storage.",1),
		//
		SSTORE(Bytecode.SSTORE, 2, 0, "Save word to storage.",1),
		//
		JUMP(Bytecode.JUMP, 1, 0, "Alter the program counter.",1),
		//
		JUMPI(Bytecode.JUMPI, 2, 0, "Conditionally alter the program counter.",1),
		//
		PC(Bytecode.PC, 0, 1,
				"Get the value of the program counter prior to the increment corresponding to this instruction.",1),
		//
		MSIZE(Bytecode.MSIZE, 0, 1, "Get the size of active memory in bytes.",1),
		//
		GAS(Bytecode.GAS, 0, 1,
				"Get the amount of available gas, including the corresponding reduction for the cost of this instruction.",1),
		//
		JUMPDEST(Bytecode.JUMPDEST, 0, 0,
				"Mark a valid destination for jumps.  This operation has no effect on machine state during execution.",1),
		// 60s & 70s: Push Operations
		PUSH1(Bytecode.PUSH1, 0, 1, "Place 1-byte item on stack.", 2),
		//
		PUSH2(Bytecode.PUSH2, 0, 1, "Place 2-byte item on stack.", 3),
		//
		PUSH3(Bytecode.PUSH3, 0, 1, "Place 3-byte item on stack.", 4),
		//
		PUSH4(Bytecode.PUSH4, 0, 1, "Place 4-byte item on stack.", 5),
		//
		PUSH5(Bytecode.PUSH5, 0, 1, "Place 5-byte item on stack.", 6),
		//
		PUSH6(Bytecode.PUSH6, 0, 1, "Place 6-byte item on stack.", 7),
		//
		PUSH7(Bytecode.PUSH7, 0, 1, "Place 7-byte item on stack.", 8),
		//
		PUSH8(Bytecode.PUSH8, 0, 1, "Place 8-byte item on stack.", 9),
		//
		PUSH9(Bytecode.PUSH9, 0, 1, "Place 9-byte item on stack.", 10),
		//
		PUSH10(Bytecode.PUSH10, 0, 1, "Place 10-byte item on stack.", 11),
		//
		PUSH11(Bytecode.PUSH11, 0, 1, "Place 11-byte item on stack.", 12),
		//
		PUSH12(Bytecode.PUSH12, 0, 1, "Place 12-byte item on stack.", 13),
		//
		PUSH13(Bytecode.PUSH13, 0, 1, "Place 13-byte item on stack.", 14),
		//
		PUSH14(Bytecode.PUSH14, 0, 1, "Place 14-byte item on stack.", 15),
		//
		PUSH15(Bytecode.PUSH15, 0, 1, "Place 15-byte item on stack.", 16),
		//
		PUSH16(Bytecode.PUSH16, 0, 1, "Place 16-byte item on stack.", 17),
		//
		PUSH17(Bytecode.PUSH17, 0, 1, "Place 17-byte item on stack.", 18),
		//
		PUSH18(Bytecode.PUSH18, 0, 1, "Place 18-byte item on stack.", 19),
		//
		PUSH19(Bytecode.PUSH19, 0, 1, "Place 19-byte item on stack.", 20),
		//
		PUSH20(Bytecode.PUSH20, 0, 1, "Place 20-byte item on stack.", 21),
		//
		PUSH21(Bytecode.PUSH21, 0, 1, "Place 21-byte item on stack.", 22),
		//
		PUSH22(Bytecode.PUSH22, 0, 1, "Place 22-byte item on stack.", 23),
		//
		PUSH23(Bytecode.PUSH23, 0, 1, "Place 23-byte item on stack.", 24),
		//
		PUSH24(Bytecode.PUSH24, 0, 1, "Place 24-byte item on stack.", 25),
		//
		PUSH25(Bytecode.PUSH25, 0, 1, "Place 25-byte item on stack.", 26),
		//
		PUSH26(Bytecode.PUSH26, 0, 1, "Place 26-byte item on stack.", 27),
		//
		PUSH27(Bytecode.PUSH27, 0, 1, "Place 27-byte item on stack.", 28),
		//
		PUSH28(Bytecode.PUSH28, 0, 1, "Place 28-byte item on stack.", 29),
		//
		PUSH29(Bytecode.PUSH29, 0, 1, "Place 29-byte item on stack.", 30),
		//
		PUSH30(Bytecode.PUSH30, 0, 1, "Place 30-byte item on stack.", 31),
		//
		PUSH31(Bytecode.PUSH31, 0, 1, "Place 31-byte item on stack.", 32),
		//
		PUSH32(Bytecode.PUSH32, 0, 1, "Place 32-byte item on stack.", 33),
		// 80s: Duplication Operations
		DUP1(Bytecode.DUP1, 1, 2, "Duplicate 1st stack item.",1),
		//
		DUP2(Bytecode.DUP2, 2, 3, "Duplicate 2nd stack item.",1),
		//
		DUP3(Bytecode.DUP3, 3, 4, "Duplicate 3rd stack item.",1),
		//
		DUP4(Bytecode.DUP4, 4, 5, "Duplicate 4th stack item.",1),
		//
		DUP5(Bytecode.DUP5, 5, 6, "Duplicate 5th stack item.",1),
		//
		DUP6(Bytecode.DUP6, 6, 7, "Duplicate 6th stack item.",1),
		//
		DUP7(Bytecode.DUP7, 7, 8, "Duplicate 7th stack item.",1),
		//
		DUP8(Bytecode.DUP8, 8, 9, "Duplicate 8th stack item.",1),
		//
		DUP9(Bytecode.DUP9, 9, 10, "Duplicate 9th stack item.",1),
		//
		DUP10(Bytecode.DUP10, 10, 11, "Duplicate 10th stack item.",1),
		//
		DUP11(Bytecode.DUP11, 11, 12, "Duplicate 11th stack item.",1),
		//
		DUP12(Bytecode.DUP12, 12, 13, "Duplicate 12th stack item.",1),
		//
		DUP13(Bytecode.DUP13, 13, 14, "Duplicate 13th stack item.",1),
		//
		DUP14(Bytecode.DUP14, 14, 15, "Duplicate 14th stack item.",1),
		//
		DUP15(Bytecode.DUP15, 15, 16, "Duplicate 15th stack item.",1),
		//
		DUP16(Bytecode.DUP16, 16, 17, "Duplicate 16th stack item.",1),
		// 90s: Exchange Operations
		SWAP1(Bytecode.SWAP1, 2, 2, "Exchange 1st and 2nd stack items.",1),
		//
		SWAP2(Bytecode.SWAP2, 3, 3, "Exchange 2nd and 3rd stack items.",1),
		//
		SWAP3(Bytecode.SWAP3, 4, 4, "Exchange 3rd and 4th stack items.",1),
		//
		SWAP4(Bytecode.SWAP4, 5, 5, "Exchange 4th and 5th stack items.",1),
		//
		SWAP5(Bytecode.SWAP5, 6, 6, "Exchange 5th and 6th stack items.",1),
		//
		SWAP6(Bytecode.SWAP6, 7, 7, "Exchange 6th and 7th stack items.",1),
		//
		SWAP7(Bytecode.SWAP7, 8, 8, "Exchange 7th and 8th stack items.",1),
		//
		SWAP8(Bytecode.SWAP8, 9, 9, "Exchange 8th and 9th stack items.",1),
		//
		SWAP9(Bytecode.SWAP9, 10, 10, "Exchange 9th and 10th stack items.",1),
		//
		SWAP10(Bytecode.SWAP10, 11, 11, "Exchange 10th and 11th stack items.",1),
		//
		SWAP11(Bytecode.SWAP11, 12, 12, "Exchange 11th and 12th stack items.",1),
		//
		SWAP12(Bytecode.SWAP12, 13, 13, "Exchange 12th and 13th stack items.",1),
		//
		SWAP13(Bytecode.SWAP13, 14, 14, "Exchange 13th and 14th stack items.",1),
		//
		SWAP14(Bytecode.SWAP14, 15, 15, "Exchange 14th and 15th stack items.",1),
		//
		SWAP15(Bytecode.SWAP15, 16, 16, "Exchange 15th and 16th stack items.",1),
		//
		SWAP16(Bytecode.SWAP16, 17, 17, "Exchange 16th and 17th stack items.",1),
		// a0s: Logging Operations
		LOG0(Bytecode.LOG0, 2, 0, "Append log record with no topics.",1),
		//
		LOG1(Bytecode.LOG1, 3, 0, "Append log record with one topics.",1),
		//
		LOG2(Bytecode.LOG2, 4, 0, "Append log record with two topics.",1),
		//
		LOG3(Bytecode.LOG3, 5, 0, "Append log record with three topics.",1),
		//
		LOG4(Bytecode.LOG4, 6, 0, "Append log record with four topics.",1),
		// f0s: System operations
		CREATE(Bytecode.CREATE, 3, 1, "Create a new account with associated code.",1),
		//
		CALL(Bytecode.CALL, 7, 1, "Message-call into an account.",1),
		//
		CALLCODE(Bytecode.CALLCODE, 7, 1, "Message-call into this account with an alternative account's code.",1),
		//
		RETURN(Bytecode.RETURN, 2, 0, "Halt execution returning output data.",1),
		//
		DELEGATECALL(Bytecode.DELEGATECALL, 6, 1,
				"Message-call into this account with an alternative account's code, but persisting the current values for sender and value.",1),
		//
		STATICCALL(Bytecode.STATICCALL, 6, 1, "Static message-call into an account.",1),
		//
		REVERT(Bytecode.REVERT, 2, 0, "Halt execution reverting state changes but returning data and remaining gas.",1),
		//
		INVALID(Bytecode.INVALID, 0, 0, "Designated invalid instruction.",1),
		//
		SELFDESTRUCT(Bytecode.SELFDESTRUCT, 1, 0, "Halt execution and register account for later deletion.",1),
		//
		UNKNOWN(-1, 0, 0, "Unknown data byte", 1);
		/**
		 * The opcode for this opcode
		 */
		public final int opcode;
		/**
		 * Number of items taken off the stack (in bytes).
		 */
		public final int arguments;
		/**
		 * Number of items placed back on the stack (in bytes).
		 */
		public final int returns;
		/**
		 * Description of this opcode.
		 */
		public final String description;
		/**
		 * Width (in bytes) of the bytecode. This will be longer than one if there are a
		 * number of trailing operand bytes.
		 */
		public final int width;

		//
		private Opcode(int opcode, int args, int returns, String description, int width) {
			this.opcode = opcode;
			this.arguments = args;
			this.returns = returns;
			this.description = description;
			this.width = width;
		}
	}

	/**
	 * Decode a given opcode at a given position in an offset stream. This returns
	 * useful information about the bytecode, such as its mnemonic, a description
	 * information about how it affects the stack.
	 *
	 * @param ints
	 * @param offset
	 * @return
	 */
	public static Opcode decode(byte _opcode) {
		int opcode = (_opcode & 0xff);
		switch (opcode) {
		case STOP:
			return Opcode.STOP;
		case ADD:
			return Opcode.ADD;
		case MUL:
			return Opcode.MUL;
		case SUB:
			return Opcode.SUB;
		case DIV:
			return Opcode.DIV;
		case SDIV:
			return Opcode.SDIV;
		case MOD:
			return Opcode.MOD;
		case SMOD:
			return Opcode.SMOD;
		case ADDMOD:
			return Opcode.ADDMOD;
		case MULMOD:
			return Opcode.MULMOD;
		case EXP:
			return Opcode.EXP;
		case SIGNEXTEND:
			return Opcode.SIGNEXTEND;
		// 10s: Comparison & Bitwise Logic Operations
		case LT:
			return Opcode.LT;
		case GT:
			return Opcode.GT;
		case SLT:
			return Opcode.SLT;
		case SGT:
			return Opcode.SGT;
		case EQ:
			return Opcode.EQ;
		case ISZERO:
			return Opcode.ISZERO;
		case AND:
			return Opcode.AND;
		case OR:
			return Opcode.OR;
		case XOR:
			return Opcode.XOR;
		case NOT:
			return Opcode.NOT;
		case BYTE:
			return Opcode.BYTE;
		// 20s: SHA3
		case SHA3:
			return Opcode.SHA3;
		// 30s: Environment Information
		case ADDRESS:
			return Opcode.ADDRESS;
		case BALANCE:
			return Opcode.BALANCE;
		case ORIGIN:
			return Opcode.ORIGIN;
		case CALLER:
			return Opcode.CALLER;
		case CALLVALUE:
			return Opcode.CALLVALUE;
		case CALLDATALOAD:
			return Opcode.CALLDATALOAD;
		case CALLDATASIZE:
			return Opcode.CALLDATASIZE;
		case CALLDATACOPY:
			return Opcode.CALLDATACOPY;
		case CODESIZE:
			return Opcode.CODESIZE;
		case CODECOPY:
			return Opcode.CODECOPY;
		case GASPRICE:
			return Opcode.GASPRICE;
		case EXTCODESIZE:
			return Opcode.EXTCODESIZE;
		case EXTCODECOPY:
			return Opcode.EXTCODECOPY;
		case RETURNDATASIZE:
			return Opcode.RETURNDATASIZE;
		case RETURNDATACOPY:
			return Opcode.RETURNDATACOPY;
		// // 40s: Block Information
		case BLOCKHASH:
			return Opcode.BLOCKHASH;
		case COINBASE:
			return Opcode.COINBASE;
		case TIMESTAMP:
			return Opcode.TIMESTAMP;
		case NUMBER:
			return Opcode.NUMBER;
		case DIFFICULTY:
			return Opcode.DIFFICULTY;
		case GASLIMIT:
			return Opcode.GASLIMIT;
		// 50s: Stack, Memory Storage and Flow Operations
		case POP:
			return Opcode.POP;
		case MLOAD:
			return Opcode.MLOAD;
		case MSTORE:
			return Opcode.MSTORE;
		case MSTORE8:
			return Opcode.MSTORE8;
		case SLOAD:
			return Opcode.SLOAD;
		case SSTORE:
			return Opcode.SSTORE;
		case JUMP:
			return Opcode.JUMP;
		case JUMPI:
			return Opcode.JUMPI;
		case PC:
			return Opcode.PC;
		case MSIZE:
			return Opcode.MSIZE;
		case GAS:
			return Opcode.GAS;
		case JUMPDEST:
			return Opcode.JUMPDEST;
		// 60s & 70s: Push Operations
		case PUSH1:
			return Opcode.PUSH1;
		case PUSH2:
			return Opcode.PUSH2;
		case PUSH3:
			return Opcode.PUSH3;
		case PUSH4:
			return Opcode.PUSH4;
		case PUSH5:
			return Opcode.PUSH5;
		case PUSH6:
			return Opcode.PUSH6;
		case PUSH7:
			return Opcode.PUSH7;
		case PUSH8:
			return Opcode.PUSH8;
		case PUSH9:
			return Opcode.PUSH9;
		case PUSH10:
			return Opcode.PUSH10;
		case PUSH11:
			return Opcode.PUSH11;
		case PUSH12:
			return Opcode.PUSH12;
		case PUSH13:
			return Opcode.PUSH13;
		case PUSH14:
			return Opcode.PUSH14;
		case PUSH15:
			return Opcode.PUSH15;
		case PUSH16:
			return Opcode.PUSH16;
		case PUSH17:
			return Opcode.PUSH17;
		case PUSH18:
			return Opcode.PUSH18;
		case PUSH19:
			return Opcode.PUSH19;
		case PUSH20:
			return Opcode.PUSH20;
		case PUSH21:
			return Opcode.PUSH21;
		case PUSH22:
			return Opcode.PUSH22;
		case PUSH23:
			return Opcode.PUSH23;
		case PUSH24:
			return Opcode.PUSH24;
		case PUSH25:
			return Opcode.PUSH25;
		case PUSH26:
			return Opcode.PUSH26;
		case PUSH27:
			return Opcode.PUSH27;
		case PUSH28:
			return Opcode.PUSH28;
		case PUSH29:
			return Opcode.PUSH29;
		case PUSH30:
			return Opcode.PUSH30;
		case PUSH31:
			return Opcode.PUSH31;
		case PUSH32:
			return Opcode.PUSH32;
		// 80s: Duplication Operations
		case DUP1:
			return Opcode.DUP1;
		case DUP2:
			return Opcode.DUP2;
		case DUP3:
			return Opcode.DUP3;
		case DUP4:
			return Opcode.DUP4;
		case DUP5:
			return Opcode.DUP5;
		case DUP6:
			return Opcode.DUP6;
		case DUP7:
			return Opcode.DUP7;
		case DUP8:
			return Opcode.DUP8;
		case DUP9:
			return Opcode.DUP9;
		case DUP10:
			return Opcode.DUP10;
		case DUP11:
			return Opcode.DUP11;
		case DUP12:
			return Opcode.DUP12;
		case DUP13:
			return Opcode.DUP13;
		case DUP14:
			return Opcode.DUP14;
		case DUP15:
			return Opcode.DUP15;
		case DUP16:
			return Opcode.DUP16;
		// 90s: Exchange Operations
		case SWAP1:
			return Opcode.SWAP1;
		case SWAP2:
			return Opcode.SWAP2;
		case SWAP3:
			return Opcode.SWAP3;
		case SWAP4:
			return Opcode.SWAP4;
		case SWAP5:
			return Opcode.SWAP5;
		case SWAP6:
			return Opcode.SWAP6;
		case SWAP7:
			return Opcode.SWAP7;
		case SWAP8:
			return Opcode.SWAP8;
		case SWAP9:
			return Opcode.SWAP9;
		case SWAP10:
			return Opcode.SWAP10;
		case SWAP11:
			return Opcode.SWAP11;
		case SWAP12:
			return Opcode.SWAP12;
		case SWAP13:
			return Opcode.SWAP13;
		case SWAP14:
			return Opcode.SWAP14;
		case SWAP15:
			return Opcode.SWAP15;
		case SWAP16:
			return Opcode.SWAP16;
		// a0s: Logging Operations
		case LOG0:
			return Opcode.LOG0;
		case LOG1:
			return Opcode.LOG1;
		case LOG2:
			return Opcode.LOG2;
		case LOG3:
			return Opcode.LOG3;
		case LOG4:
			return Opcode.LOG4;
		// f0s: System operations
		case CREATE:
			return Opcode.CREATE;
		case CALL:
			return Opcode.CALL;
		case CALLCODE:
			return Opcode.CALLCODE;
		case RETURN:
			return Opcode.RETURN;
		case DELEGATECALL:
			return Opcode.DELEGATECALL;
		case STATICCALL:
			return Opcode.STATICCALL;
		case REVERT:
			return Opcode.REVERT;
		case INVALID:
			return Opcode.INVALID;
		case SELFDESTRUCT:
			return Opcode.SELFDESTRUCT;
		default:
			return Opcode.UNKNOWN;
		}
	}

	/**
	 * Execute
	 *
	 * @param state
	 */
	public static boolean execute(VirtualMachine.State state) {
		VirtualMachine.Memory<Byte> code = state.getCodeMemory();
		int pc = state.pc();
		int opcode = code.read(pc) & 0xff;
		switch (opcode) {
		case STOP:
			return executeSTOP(pc, state);
		case ADD:
			return executeADD(pc, state);
		case MUL:
			return executeMUL(pc, state);
		case SUB:
			return executeSUB(pc, state);
		case DIV:
			return executeDIV(pc, state);
		case SDIV:
			return executeSDIV(pc, state);
		case MOD:
			return executeMOD(pc, state);
		case SMOD:
			return executeSMOD(pc, state);
		case ADDMOD:
			return executeADDMOD(pc, state);
		case MULMOD:
			return executeMULMOD(pc, state);
		case EXP:
			return executeEXP(pc, state);
		case SIGNEXTEND:
			return executeSIGNEXTEND(pc, state);
		// 10s: Comparison & Bitwise Logic Operations
		case LT:
			return executeLT(pc, state);
		case GT:
			return executeGT(pc, state);
		case SLT:
			return executeSLT(pc, state);
		case SGT:
			return executeSGT(pc, state);
		case EQ:
			return executeEQ(pc, state);
		case ISZERO:
			return executeISZERO(pc, state);
		case AND:
			return executeAND(pc, state);
		case OR:
			return executeOR(pc, state);
		case XOR:
			return executeXOR(pc, state);
		case NOT:
			return executeNOT(pc, state);
		case BYTE:
			return executeBYTE(pc, state);
		// 20s: SHA3
		case SHA3:
			throw new IllegalArgumentException("implement me");
		// 30s: Environment Information
		case ADDRESS:
			throw new IllegalArgumentException("implement me");
		case BALANCE:
			throw new IllegalArgumentException("implement me");
		case ORIGIN:
			throw new IllegalArgumentException("implement me");
		case CALLER:
			throw new IllegalArgumentException("implement me");
		case CALLVALUE:
			throw new IllegalArgumentException("implement me");
		case CALLDATALOAD:
			throw new IllegalArgumentException("implement me");
		case CALLDATASIZE:
			throw new IllegalArgumentException("implement me");
		case CALLDATACOPY:
			throw new IllegalArgumentException("implement me");
		case CODESIZE:
			throw new IllegalArgumentException("implement me");
		case CODECOPY:
			throw new IllegalArgumentException("implement me");
		case GASPRICE:
			throw new IllegalArgumentException("implement me");
		case EXTCODESIZE:
			throw new IllegalArgumentException("implement me");
		case EXTCODECOPY:
			throw new IllegalArgumentException("implement me");
		case RETURNDATASIZE:
			throw new IllegalArgumentException("implement me");
		case RETURNDATACOPY:
			throw new IllegalArgumentException("implement me");
		// // 40s: Block Information
		case BLOCKHASH:
			throw new IllegalArgumentException("implement me");
		case COINBASE:
			throw new IllegalArgumentException("implement me");
		case TIMESTAMP:
			throw new IllegalArgumentException("implement me");
		case NUMBER:
			throw new IllegalArgumentException("implement me");
		case DIFFICULTY:
			throw new IllegalArgumentException("implement me");
		case GASLIMIT:
			throw new IllegalArgumentException("implement me");
		// 50s: Stack, Memory Storage and Flow Operations
		case POP:
			return executePOP(pc,state);
		case MLOAD:
			return executeMLOAD(pc,state);
		case MSTORE:
			return executeMSTORE(pc,state);
		case MSTORE8:
			throw new IllegalArgumentException("implement me");
		case SLOAD:
			return executeSLOAD(pc,state);
		case SSTORE:
			return executeSSTORE(pc,state);
		case JUMP:
			return executeJUMP(pc, state);
		case JUMPI:
			return executeJUMPI(pc, state);
		case PC:
			throw new IllegalArgumentException("implement me");
		case MSIZE:
			throw new IllegalArgumentException("implement me");
		case GAS:
			return executeGAS(pc, state);
		case JUMPDEST:
			return executeJUMPDEST(pc, state);
		// 60s & 70s: Push Operations
		case PUSH1:
		case PUSH2:
		case PUSH3:
		case PUSH4:
		case PUSH5:
		case PUSH6:
		case PUSH7:
		case PUSH8:
		case PUSH9:
		case PUSH10:
		case PUSH11:
		case PUSH12:
		case PUSH13:
		case PUSH14:
		case PUSH15:
		case PUSH16:
		case PUSH17:
		case PUSH18:
		case PUSH19:
		case PUSH20:
		case PUSH21:
		case PUSH22:
		case PUSH23:
		case PUSH24:
		case PUSH25:
		case PUSH26:
		case PUSH27:
		case PUSH28:
		case PUSH29:
		case PUSH30:
		case PUSH31:
		case PUSH32: {
			int count = opcode - PUSH1 + 1;
			return executePUSH(count, pc, state);
		}
				// 80s: Duplication Operations
		case DUP1:
		case DUP2:
		case DUP3:
		case DUP4:
		case DUP5:
		case DUP6:
		case DUP7:
		case DUP8:
		case DUP9:
		case DUP10:
		case DUP11:
		case DUP12:
		case DUP13:
		case DUP14:
		case DUP15:
		case DUP16:  {
			int count = opcode - DUP1 + 1;
			return executeDUP(count, pc, state);
		}
		// 90s: Exchange Operations
		case SWAP1:
		case SWAP2:
		case SWAP3:
		case SWAP4:
		case SWAP5:
		case SWAP6:
		case SWAP7:
		case SWAP8:
		case SWAP9:
		case SWAP10:
		case SWAP11:
		case SWAP12:
		case SWAP13:
		case SWAP14:
		case SWAP15:
		case SWAP16: {
			int count = opcode - SWAP1 + 1;
			return executeSWAP(count, pc, state);
		}
		// a0s: Logging Operations
		case LOG0:
			throw new IllegalArgumentException("implement me");
		case LOG1:
			throw new IllegalArgumentException("implement me");
		case LOG2:
			throw new IllegalArgumentException("implement me");
		case LOG3:
			throw new IllegalArgumentException("implement me");
		case LOG4:
			throw new IllegalArgumentException("implement me");
		// f0s: System operations
		case CREATE:
			throw new IllegalArgumentException("implement me");
		case CALL:
			throw new IllegalArgumentException("implement me");
		case CALLCODE:
			throw new IllegalArgumentException("implement me");
		case RETURN:
			throw new IllegalArgumentException("implement me");
		case DELEGATECALL:
			throw new IllegalArgumentException("implement me");
		case STATICCALL:
			throw new IllegalArgumentException("implement me");
		case REVERT:
			throw new IllegalArgumentException("implement me");
		case INVALID:
			return executeINVALID(pc, state);
		case SELFDESTRUCT:
			throw new IllegalArgumentException("implement me");
		default:
			throw new IllegalArgumentException("unknown bytecode encountered");
		}
	}

	private static boolean executeGAS(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		stack.push(new w256(state.gas()));
		return true;
	}

	private static boolean executeINVALID(int pc, VirtualMachine.State state) {
		state.halt(VirtualMachine.State.Status.EXCEPTION);
		return false;
	}

	private static boolean executeSTOP(int pc, VirtualMachine.State state) {
		state.halt(VirtualMachine.State.Status.STOP);
		return false;
	}

	private static boolean executeADD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		stack.push(lhs.add(rhs));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeSUB(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		stack.push(lhs.subtract(rhs));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeMUL(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		stack.push(lhs.multiply(rhs));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeDIV(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeSDIV(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeMOD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeSMOD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeADDMOD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeMULMOD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeEXP(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		throw new UnsupportedOperationException();
	}

	private static boolean executeSIGNEXTEND(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 w1 = stack.pop();
		w256 w0 = stack.pop();
		int offset = w0.toInt();
		// NOTE: negative offset has no effect
		w1 = (offset < 31) ? w1.signExtend(offset + 1) : w1;
		stack.push(w1);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeLT(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		boolean b = lhs.unsignedLessThan(rhs);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeSLT(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		boolean b = lhs.signedLessThan(rhs);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeGT(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		boolean b = rhs.unsignedLessThan(lhs);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeSGT(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		boolean b = rhs.signedLessThan(lhs);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeEQ(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		boolean b = lhs.equals(rhs);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeISZERO(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 lhs = stack.pop();
		boolean b = lhs.equals(w256.ZERO);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeAND(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		stack.push(lhs.and(rhs));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeOR(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		stack.push(lhs.or(rhs));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeXOR(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 rhs = stack.pop();
		w256 lhs = stack.pop();
		stack.push(lhs.xor(rhs));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeNOT(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 lhs = stack.pop();
		boolean b = lhs.equals(w256.ZERO);
		stack.push(b ? w256.ONE : w256.ZERO);
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeBYTE(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		w256 w0 = stack.pop();
		w256 w1 = stack.pop();
		int offset = w0.toInt();
		if(!w0.isInt() || offset < 0 || offset >= 32) {
			stack.push(w256.ZERO);
		} else {
			byte[] bytes = w1.toByteArray();
			stack.push(new w256(bytes[offset]));
		}
		state.jump(pc+1);
		return true;
	}

	private static boolean executePOP(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		stack.pop();
		return true;
	}

	private static boolean executeMLOAD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<w256> local = state.getLocalMemory();
		w256 address = stack.pop();
		// Ensure enough memory
		if(!local.expand(address)) {
			state.halt(VirtualMachine.State.Status.EXCEPTION);
			return false;
		} else {
			stack.push(local.read(address));
			state.jump(pc+1);
			return true;
		}
	}

	private static boolean executeMSTORE(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<w256> local = state.getLocalMemory();
		w256 address = stack.pop();
		w256 w = stack.pop();
		// Ensure enough memory
		if (!local.expand(address)) {
			state.halt(VirtualMachine.State.Status.EXCEPTION);
			return false;
		} else {
			// Update the target location
			local.write(address, w);
			state.jump(pc+1);
			return true;
		}
	}

	private static boolean executeSLOAD(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<w256> storage = state.getStorageMemory();
		w256 address = stack.pop();
		// Ensure enough memory
		if(!storage.expand(address)) {
			state.halt(VirtualMachine.State.Status.EXCEPTION);
			return false;
		} else {
			stack.push(storage.read(address));
			state.jump(pc+1);
			return true;
		}
	}

	private static boolean executeSSTORE(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<w256> storage = state.getStorageMemory();
		w256 address = stack.pop();
		w256 w = stack.pop();
		// Ensure enough memory
		if (!storage.expand(address)) {
			state.halt(VirtualMachine.State.Status.EXCEPTION);
			return false;
		} else {
			// Update the target location
			storage.write(address, w);
			state.jump(pc+1);
			return true;
		}
	}

	private static boolean executeJUMPI(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<Byte> code = state.getCodeMemory();
		int target = pc + 1;
		w256 address = stack.pop();
		w256 w = stack.pop();
		// FIXME: do we fail early if address invalid?
		if (w.equals(w256.ZERO)) {
			// Jump to the target address
			target = address.toInt();
			// Sanity check jump target
			if (!address.isInt() || target < 0 || target >= code.used() || code.read(target) != JUMPDEST) {
				state.halt(Status.EXCEPTION);
				return false;
			}
		}
		// Branch either to next instruction, or jumpdest
		state.jump(target);
		return true;
	}

	private static boolean executeJUMP(int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<Byte> code = state.getCodeMemory();
		w256 address = stack.pop();
		int target = address.toInt();
		// Sanity check jump target
		if (!address.isInt() || target < 0 || target >= code.used() || code.read(target) != JUMPDEST) {
			state.halt(Status.EXCEPTION);
			return false;
		} else {
			// Jump target looks fine, so proceed
			state.jump(target);
			return true;
		}
	}
	private static boolean executeJUMPDEST(int pc, VirtualMachine.State state) {
		state.jump(pc+1);
		return true;
	}

	private static boolean executePUSH(int count, int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<Byte> code = state.getCodeMemory();
		// extract operand
		byte[] bytes = new byte[count];
		pc = pc + 1;
		for (int i = 0; i != count; ++i) {
			bytes[i] = code.read(pc + i);
		}
		// push onto stack
		stack.push(new w256(bytes));
		state.jump(pc + count);
		return true;
	}

	private static boolean executeDUP(int count, int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		stack.push(stack.read(stack.used() - count));
		state.jump(pc + 1);
		return true;
	}

	private static boolean executeSWAP(int count, int pc, VirtualMachine.State state) {
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		int ith = stack.used() - 1;
		int jth = stack.used() - (count + 1);
		//
		w256 tmp = stack.read(ith);
		stack.write(ith, stack.read(jth));
		stack.write(jth, tmp);
		state.jump(pc + 1);
		return true;
	}


	public static void printState(VirtualMachine.State state) {
		VirtualMachine.Memory<Byte> code = state.getCodeMemory();
		VirtualMachine.Stack<w256> stack = state.getStackMemory();
		VirtualMachine.Memory<w256> local = state.getLocalMemory();
		VirtualMachine.Memory<w256> storage = state.getStorageMemory();
		//
		System.out.println(state.status() + ", PC=" + state.pc() + ", SP=" + stack.used() + ", MP=" + local.used());
		System.out.println("ADDRESS            STACK              MEMORY");

		for(int i=0;i!=Math.max(stack.used(),local.used());++i) {
			System.out.print(new w256(i));
			System.out.print(" ");
			// Stack
			if(i < stack.used()) {
				System.out.print(stack.read(i));
			} else {
				System.out.print("                  ");
			}
			System.out.print(" ");
			if(i < local.used()) {
				System.out.print(local.read(i));
			} else {
				System.out.print("                  ");
			}
			System.out.print(" ");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		//byte[] bytes = Hex.fromBigEndianString("6080604052607b600055348015601457600080fd5b5060358060226000396000f3006080604052600080fd00a165627a7a72305820eb2a49ca9445598c397756374a0f997239da31baed31403e05d0fbe5666571930029");
		//ArrayState state = new ArrayState(bytes);
		ArrayState state = new ArrayState(new byte[] { PUSH1, 0x1, PUSH1, 0x0, MSTORE, PUSH1, 0x0, MLOAD, PUSH1, 0x16 });
		while (state.status() == VirtualMachine.State.Status.OK && state.pc() < state.getCodeMemory().used()) {
			Bytecode.execute(state);
		}
		System.out.println("STATE: " + state);
		//printState(state);
	}
}
