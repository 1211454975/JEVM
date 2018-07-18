package jevm.core;

/**
 * Provides an encoding of the information contained in "Appendix H" of the
 * yellow paper.
 *
 * @author David J. Pearce
 *
 */
public interface Bytecode {

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

	/**
	 * Provides useful information on all bytecodes.
	 *
	 * @author David J. Pearce
	 *
	 */
	public enum Opcode {
		// 0s: Stop and Arithmetic Operations
		STOP(0x00, 0, 0, "Halts execution.",1),
		//
		ADD(0x01, 2, 1, "Addition operation.",1),
		//
		MUL(0x02, 2, 1, "Multiplication operation.",1),
		//
		SUB(0x03, 2, 1, "Subtraction operation.",1),
		//
		DIV(0x04, 2, 1, "Integer division operation.",1),
		//
		SDIV(0x05, 2, 1, "Signed integer division operation (truncated).",1),
		//
		MOD(0x06, 2, 1, "Modulo remainder operation.",1),
		//
		SMOD(0x07, 2, 1, "Signed modulo remainder operation.",1),
		//
		ADDMOD(0x08, 3, 1, "Modulo addition operation.",1),
		//
		MULMOD(0x09, 3, 1, "Modulo multiplication operation.",1),
		//
		EXP(0x0a, 2, 1, "Exponential Operation.",1),
		//
		SIGNEXTEND(0x0b, 2, 1, "Extend length of two's complement signed integer.",1),
		// 10s: Comparison & Bitwise Logic Operations
		LT(0x10, 2, 1, "Less-than comparison.",1),
		//
		GT(0x11, 2, 1, "Greater-than comparison.",1),
		//
		SLT(0x12, 2, 1, "Signed less-than comparison.",1),
		//
		SGT(0x13, 2, 1, "Signed greater-than comparison.",1),
		//
		EQ(0x14, 2, 1, "Equality comparison.",1),
		//
		ISZERO(0x15, 1, 1, "Simple not operator.",1),
		//
		AND(0x16, 2, 1, "Bitwise AND operation.",1),
		//
		OR(0x17, 2, 1, "Bitwise OR operation.",1),
		//
		XOR(0x18, 2, 1, "Bitwise XOR operation.",1),
		//
		NOT(0x19, 1, 1, "Bitwise NOT operation.",1),
		//
		BYTE(0x1a, 2, 1, "Retrieves single byte from word.",1),
		// 20s: SHA3
		SHA3(0x20, 2, 1, "Compute Keccak-256 hash.",1),
		// 30s: Environment Information
		ADDRESS(0x30, 0, 1, "Get address of currently executing account.",1),
		//
		BALANCE(0x31, 1, 1, "Get balance of the given account.",1),
		//
		ORIGIN(0x32, 0, 1, "Get execution origination address.",1),
		//
		CALLER(0x33, 0, 1,
				"Get caller address.  This is the address of the account that is directly responsible for this execution.",1),
		//
		CALLVALUE(0x34, 0, 1, "Get deposited value by the instruction/transaction responsible for this execution.",1),
		//
		CALLDATALOAD(0x35, 1, 1,
				"Get input data of current environment.  This pertains to the input data passed with the message call instruction or transaction.",1),
		//
		CALLDATASIZE(0x36, 0, 1, "Get size of input data in current environment.",1),
		//
		CALLDATACOPY(0x37, 3, 1,
				"Copy input data in current environment to memory.  This pertains to the input data passed with the message call instruction or transaction.",1),
		//
		CODESIZE(0x38, 0, 1, "Get size of code running in current environment.",1),
		//
		CODECOPY(0x39, 3, 0, "Copy code running in current environment to memory.",1),
		//
		GASPRICE(0x3a, 0, 1,
				"Get price of gas in current environment.  This is gas price speci\fed by the originating transaction.",1),
		//
		EXTCODESIZE(0x3b, 1, 1, "Get size of an account's code.",1),
		//
		EXTCODECOPY(0x3c, 4, 0, "Copy an account's code to memory.",1),
		//
		RETURNDATASIZE(0x3d, 0, 1, "Get size of output data from the previous call from the current environment.",1),
		//
		RETURNDATACOPY(0x3e, 3, 0, "Copy output data from the previous call to memory.",1),
		// 40s: Block Information
		BLOCKHASH(0x40, 1, 1, "Get the hash of one of the 256 most recent complete blocks.",1),
		//
		COINBASE(0x41, 0, 1, "Get the block's benefciary address.",1),
		//
		TIMESTAMP(0x42, 0, 1, "Get the block's timestamp.",1),
		//
		NUMBER(0x43, 0, 1, "Get the block's number.",1),
		//
		DIFFICULTY(0x44, 0, 1, "Get the block's difficulty.",1),
		//
		GASLIMIT(0x45, 0, 1, "Get the block's gas limit.",1),
		// 50s: Stack, Memory Storage and Flow Operations
		POP(0x50, 1, 0, "Remove item from stack.",1),
		//
		MLOAD(0x51, 1, 1, "Load word from memory.",1),
		//
		MSTORE(0x52, 2, 0, "Save word to memory.",1),
		//
		MSTORE8(0x53, 2, 0, "Save byte to memory.",1),
		//
		SLOAD(0x54, 1, 1, "Load word from storage.",1),
		//
		SSTORE(0x55, 2, 0, "Save word to storage.",1),
		//
		JUMP(0x56, 1, 0, "Alter the program counter.",1),
		//
		JUMPI(0x57, 2, 0, "Conditionally alter the program counter.",1),
		//
		PC(0x58, 0, 1,
				"Get the value of the program counter prior to the increment corresponding to this instruction.",1),
		//
		MSIZE(0x59, 0, 1, "Get the size of active memory in bytes.",1),
		//
		GAS(0x5a, 0, 1,
				"Get the amount of available gas, including the corresponding reduction for the cost of this instruction.",1),
		//
		JUMPDEST(0x5b, 0, 0,
				"Mark a valid destination for jumps.  This operation has no effect on machine state during execution.",1),
		// 60s & 70s: Push Operations
		PUSH1(0x60, 0, 1, "Place 1-byte item on stack.", 2),
		//
		PUSH2(0x61, 0, 1, "Place 2-byte item on stack.", 3),
		//
		PUSH3(0x62, 0, 1, "Place 3-byte item on stack.", 4),
		//
		PUSH4(0x63, 0, 1, "Place 4-byte item on stack.", 5),
		//
		PUSH5(0x64, 0, 1, "Place 5-byte item on stack.", 6),
		//
		PUSH6(0x65, 0, 1, "Place 6-byte item on stack.", 7),
		//
		PUSH7(0x66, 0, 1, "Place 7-byte item on stack.", 8),
		//
		PUSH8(0x67, 0, 1, "Place 8-byte item on stack.", 9),
		//
		PUSH9(0x68, 0, 1, "Place 9-byte item on stack.", 10),
		//
		PUSH10(0x69, 0, 1, "Place 10-byte item on stack.", 11),
		//
		PUSH11(0x6a, 0, 1, "Place 11-byte item on stack.", 12),
		//
		PUSH12(0x6b, 0, 1, "Place 12-byte item on stack.", 13),
		//
		PUSH13(0x6c, 0, 1, "Place 13-byte item on stack.", 14),
		//
		PUSH14(0x6d, 0, 1, "Place 14-byte item on stack.", 15),
		//
		PUSH15(0x6e, 0, 1, "Place 15-byte item on stack.", 16),
		//
		PUSH16(0x6f, 0, 1, "Place 16-byte item on stack.", 17),
		//
		PUSH17(0x70, 0, 1, "Place 17-byte item on stack.", 18),
		//
		PUSH18(0x71, 0, 1, "Place 18-byte item on stack.", 19),
		//
		PUSH19(0x72, 0, 1, "Place 19-byte item on stack.", 20),
		//
		PUSH20(0x73, 0, 1, "Place 20-byte item on stack.", 21),
		//
		PUSH21(0x74, 0, 1, "Place 21-byte item on stack.", 22),
		//
		PUSH22(0x75, 0, 1, "Place 22-byte item on stack.", 23),
		//
		PUSH23(0x76, 0, 1, "Place 23-byte item on stack.", 24),
		//
		PUSH24(0x77, 0, 1, "Place 24-byte item on stack.", 25),
		//
		PUSH25(0x78, 0, 1, "Place 25-byte item on stack.", 26),
		//
		PUSH26(0x79, 0, 1, "Place 26-byte item on stack.", 27),
		//
		PUSH27(0x7a, 0, 1, "Place 27-byte item on stack.", 28),
		//
		PUSH28(0x7b, 0, 1, "Place 28-byte item on stack.", 29),
		//
		PUSH29(0x7c, 0, 1, "Place 29-byte item on stack.", 30),
		//
		PUSH30(0x7d, 0, 1, "Place 30-byte item on stack.", 31),
		//
		PUSH31(0x7e, 0, 1, "Place 31-byte item on stack.", 32),
		//
		PUSH32(0x7f, 0, 1, "Place 32-byte item on stack.", 33),
		// 80s: Duplication Operations
		DUP1(0x80, 1, 2, "Duplicate 1st stack item.",1),
		//
		DUP2(0x81, 2, 3, "Duplicate 2nd stack item.",1),
		//
		DUP3(0x82, 3, 4, "Duplicate 3rd stack item.",1),
		//
		DUP4(0x83, 4, 5, "Duplicate 4th stack item.",1),
		//
		DUP5(0x84, 5, 6, "Duplicate 5th stack item.",1),
		//
		DUP6(0x85, 6, 7, "Duplicate 6th stack item.",1),
		//
		DUP7(0x86, 7, 8, "Duplicate 7th stack item.",1),
		//
		DUP8(0x87, 8, 9, "Duplicate 8th stack item.",1),
		//
		DUP9(0x88, 9, 10, "Duplicate 9th stack item.",1),
		//
		DUP10(0x89, 10, 11, "Duplicate 10th stack item.",1),
		//
		DUP11(0x8a, 11, 12, "Duplicate 11th stack item.",1),
		//
		DUP12(0x8b, 12, 13, "Duplicate 12th stack item.",1),
		//
		DUP13(0x8c, 13, 14, "Duplicate 13th stack item.",1),
		//
		DUP14(0x8d, 14, 15, "Duplicate 14th stack item.",1),
		//
		DUP15(0x8e, 15, 16, "Duplicate 15th stack item.",1),
		//
		DUP16(0x8f, 16, 17, "Duplicate 16th stack item.",1),
		// 90s: Exchange Operations
		SWAP1(0x90, 2, 2, "Exchange 1st and 2nd stack items.",1),
		//
		SWAP2(0x91, 3, 3, "Exchange 2nd and 3rd stack items.",1),
		//
		SWAP3(0x92, 4, 4, "Exchange 3rd and 4th stack items.",1),
		//
		SWAP4(0x93, 5, 5, "Exchange 4th and 5th stack items.",1),
		//
		SWAP5(0x94, 6, 6, "Exchange 5th and 6th stack items.",1),
		//
		SWAP6(0x95, 7, 7, "Exchange 6th and 7th stack items.",1),
		//
		SWAP7(0x96, 8, 8, "Exchange 7th and 8th stack items.",1),
		//
		SWAP8(0x97, 9, 9, "Exchange 8th and 9th stack items.",1),
		//
		SWAP9(0x98, 10, 10, "Exchange 9th and 10th stack items.",1),
		//
		SWAP10(0x99, 11, 11, "Exchange 10th and 11th stack items.",1),
		//
		SWAP11(0x9a, 12, 12, "Exchange 11th and 12th stack items.",1),
		//
		SWAP12(0x9b, 13, 13, "Exchange 12th and 13th stack items.",1),
		//
		SWAP13(0x9c, 14, 14, "Exchange 13th and 14th stack items.",1),
		//
		SWAP14(0x9d, 15, 15, "Exchange 14th and 15th stack items.",1),
		//
		SWAP15(0x9e, 16, 16, "Exchange 15th and 16th stack items.",1),
		//
		SWAP16(0x9f, 17, 17, "Exchange 16th and 17th stack items.",1),
		// a0s: Logging Operations
		LOG0(0xa0, 2, 0, "Append log record with no topics.",1),
		//
		LOG1(0xa1, 3, 0, "Append log record with one topics.",1),
		//
		LOG2(0xa2, 4, 0, "Append log record with two topics.",1),
		//
		LOG3(0xa3, 5, 0, "Append log record with three topics.",1),
		//
		LOG4(0xa4, 6, 0, "Append log record with four topics.",1),
		// f0s: System operations
		CREATE(0xf0, 3, 1, "Create a new account with associated code.",1),
		//
		CALL(0xf1, 7, 1, "Message-call into an account.",1),
		//
		CALLCODE(0xf2, 7, 1, "Message-call into this account with an alternative account's code.",1),
		//
		RETURN(0xf3, 2, 0, "Halt execution returning output data.",1),
		//
		DELEGATECALL(0xf4, 6, 1,
				"Message-call into this account with an alternative account's code, but persisting the current values for sender and value.",1),
		//
		STATICCALL(0xfa, 6, 1, "Static message-call into an account.",1),
		//
		REVERT(0xfd, 2, 0, "Halt execution reverting state changes but returning data and remaining gas.",1),
		//
		INVALID(0xfe, 0, 0, "Designated invalid instruction.",1),
		//
		SELFDESTRUCT(0xff, 1, 0, "Halt execution and register account for later deletion.",1),
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
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static Opcode decode(byte _opcode) {
		int opcode = (_opcode & 0xff);
		switch (opcode) {
		case 0x00:
			return Opcode.STOP;
		case 0x01:
			return Opcode.ADD;
		case 0x02:
			return Opcode.MUL;
		case 0x03:
			return Opcode.SUB;
		case 0x04:
			return Opcode.DIV;
		case 0x05:
			return Opcode.SDIV;
		case 0x06:
			return Opcode.MOD;
		case 0x07:
			return Opcode.SMOD;
		case 0x08:
			return Opcode.ADDMOD;
		case 0x09:
			return Opcode.MULMOD;
		case 0x0a:
			return Opcode.EXP;
		case 0x0b:
			return Opcode.SIGNEXTEND;
		// 10s: Comparison & Bitwise Logic Operations
		case 0x10:
			return Opcode.LT;
		case 0x11:
			return Opcode.GT;
		case 0x12:
			return Opcode.SLT;
		case 0x13:
			return Opcode.SGT;
		case 0x14:
			return Opcode.EQ;
		case 0x15:
			return Opcode.ISZERO;
		case 0x16:
			return Opcode.AND;
		case 0x17:
			return Opcode.OR;
		case 0x18:
			return Opcode.XOR;
		case 0x19:
			return Opcode.NOT;
		case 0x1a:
			return Opcode.BYTE;
		// 20s: SHA3
		case 0x20:
			return Opcode.SHA3;
		// 30s: Environment Information
		case 0x30:
			return Opcode.ADDRESS;
		case 0x31:
			return Opcode.BALANCE;
		case 0x32:
			return Opcode.ORIGIN;
		case 0x33:
			return Opcode.CALLER;
		case 0x34:
			return Opcode.CALLVALUE;
		case 0x35:
			return Opcode.CALLDATALOAD;
		case 0x36:
			return Opcode.CALLDATASIZE;
		case 0x37:
			return Opcode.CALLDATACOPY;
		case 0x38:
			return Opcode.CODESIZE;
		case 0x39:
			return Opcode.CODECOPY;
		case 0x3a:
			return Opcode.GASPRICE;
		case 0x3b:
			return Opcode.EXTCODESIZE;
		case 0x3c:
			return Opcode.EXTCODECOPY;
		case 0x3d:
			return Opcode.RETURNDATASIZE;
		case 0x3e:
			return Opcode.RETURNDATACOPY;
		// // 40s: Block Information
		case 0x40:
			return Opcode.BLOCKHASH;
		case 0x41:
			return Opcode.COINBASE;
		case 0x42:
			return Opcode.TIMESTAMP;
		case 0x43:
			return Opcode.NUMBER;
		case 0x44:
			return Opcode.DIFFICULTY;
		case 0x45:
			return Opcode.GASLIMIT;
		// 50s: Stack, Memory Storage and Flow Operations
		case 0x50:
			return Opcode.POP;
		case 0x51:
			return Opcode.MLOAD;
		case 0x52:
			return Opcode.MSTORE;
		case 0x53:
			return Opcode.MSTORE8;
		case 0x54:
			return Opcode.SLOAD;
		case 0x55:
			return Opcode.SSTORE;
		case 0x56:
			return Opcode.JUMP;
		case 0x57:
			return Opcode.JUMPI;
		case 0x58:
			return Opcode.PC;
		case 0x59:
			return Opcode.MSIZE;
		case 0x5a:
			return Opcode.GAS;
		case 0x5b:
			return Opcode.JUMPDEST;
		// 60s & 70s: Push Operations
		case 0x60:
			return Opcode.PUSH1;
		case 0x61:
			return Opcode.PUSH2;
		case 0x62:
			return Opcode.PUSH3;
		case 0x63:
			return Opcode.PUSH4;
		case 0x64:
			return Opcode.PUSH5;
		case 0x65:
			return Opcode.PUSH6;
		case 0x66:
			return Opcode.PUSH7;
		case 0x67:
			return Opcode.PUSH8;
		case 0x68:
			return Opcode.PUSH9;
		case 0x69:
			return Opcode.PUSH10;
		case 0x6a:
			return Opcode.PUSH11;
		case 0x6b:
			return Opcode.PUSH12;
		case 0x6c:
			return Opcode.PUSH13;
		case 0x6d:
			return Opcode.PUSH14;
		case 0x6e:
			return Opcode.PUSH15;
		case 0x6f:
			return Opcode.PUSH16;
		case 0x70:
			return Opcode.PUSH17;
		case 0x71:
			return Opcode.PUSH18;
		case 0x72:
			return Opcode.PUSH19;
		case 0x73:
			return Opcode.PUSH20;
		case 0x74:
			return Opcode.PUSH21;
		case 0x75:
			return Opcode.PUSH22;
		case 0x76:
			return Opcode.PUSH23;
		case 0x77:
			return Opcode.PUSH24;
		case 0x78:
			return Opcode.PUSH25;
		case 0x79:
			return Opcode.PUSH26;
		case 0x7a:
			return Opcode.PUSH27;
		case 0x7b:
			return Opcode.PUSH28;
		case 0x7c:
			return Opcode.PUSH29;
		case 0x7d:
			return Opcode.PUSH30;
		case 0x7e:
			return Opcode.PUSH31;
		case 0x7f:
			return Opcode.PUSH32;
		// 80s: Duplication Operations
		case 0x80:
			return Opcode.DUP1;
		case 0x81:
			return Opcode.DUP2;
		case 0x82:
			return Opcode.DUP3;
		case 0x83:
			return Opcode.DUP4;
		case 0x84:
			return Opcode.DUP5;
		case 0x85:
			return Opcode.DUP6;
		case 0x86:
			return Opcode.DUP7;
		case 0x87:
			return Opcode.DUP8;
		case 0x88:
			return Opcode.DUP9;
		case 0x89:
			return Opcode.DUP10;
		case 0x8a:
			return Opcode.DUP11;
		case 0x8b:
			return Opcode.DUP12;
		case 0x8c:
			return Opcode.DUP13;
		case 0x8d:
			return Opcode.DUP14;
		case 0x8e:
			return Opcode.DUP15;
		case 0x8f:
			return Opcode.DUP16;
		// 90s: Exchange Operations
		case 0x90:
			return Opcode.SWAP1;
		case 0x91:
			return Opcode.SWAP2;
		case 0x92:
			return Opcode.SWAP3;
		case 0x93:
			return Opcode.SWAP4;
		case 0x94:
			return Opcode.SWAP5;
		case 0x95:
			return Opcode.SWAP6;
		case 0x96:
			return Opcode.SWAP7;
		case 0x97:
			return Opcode.SWAP8;
		case 0x98:
			return Opcode.SWAP9;
		case 0x99:
			return Opcode.SWAP10;
		case 0x9a:
			return Opcode.SWAP11;
		case 0x9b:
			return Opcode.SWAP12;
		case 0x9c:
			return Opcode.SWAP13;
		case 0x9d:
			return Opcode.SWAP14;
		case 0x9e:
			return Opcode.SWAP15;
		case 0x9f:
			return Opcode.SWAP16;
		// a0s: Logging Operations
		case 0xa0:
			return Opcode.LOG0;
		case 0xa1:
			return Opcode.LOG1;
		case 0xa2:
			return Opcode.LOG2;
		case 0xa3:
			return Opcode.LOG3;
		case 0xa4:
			return Opcode.LOG4;
		// f0s: System operations
		case 0xf0:
			return Opcode.CREATE;
		case 0xf1:
			return Opcode.CALL;
		case 0xf2:
			return Opcode.CALLCODE;
		case 0xf3:
			return Opcode.RETURN;
		case 0xf4:
			return Opcode.DELEGATECALL;
		case 0xfa:
			return Opcode.STATICCALL;
		case 0xfd:
			return Opcode.REVERT;
		case 0xfe:
			return Opcode.INVALID;
		case 0xff:
			return Opcode.SELFDESTRUCT;
		default:
			return Opcode.UNKNOWN;
		}
	}

}
