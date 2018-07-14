package jevm.core;

public abstract class Bytecode {
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
	 * Identifies the opcode used for each bytecode.
	 *
	 * @author David J. Pearce
	 *
	 */
	public enum Opcode {
		// 0s: Stop and Arithmetic Operations
		STOP         (0x00, 0, 0, "Halts execution."),
		ADD          (0x01, 2, 1, "Addition operation."),
		MUL          (0x02, 2, 1, "Multiplication operation."),
		SUB          (0x03, 2, 1, "Subtraction operation."),
		DIV          (0x04, 2, 1, "Integer division operation."),
		SDIV         (0x05, 2, 1, "Signed integer division operation (truncated)."),
		MOD          (0x06, 2, 1, "Modulo remainder operation."),
		SMOD         (0x07, 2, 1, "Signed modulo remainder operation."),
		ADDMOD       (0x08, 3, 1, "Modulo addition operation."),
		MULMOD       (0x09, 3, 1, "Modulo multiplication operation."),
		EXP          (0x0a, 2, 1, "Exponential Operation."),
		SIGNEXTEND   (0x0b, 2, 1, "Extend length of two's complement signed integer."),
		// 10s: Comparison & Bitwise Logic Operations
		LT           (0x10, 2, 1, "Less-than comparison."),
		GT           (0x11, 2, 1, "Greater-than comparison."),
		SLT          (0x12, 2, 1, "Signed less-than comparison."),
		SGT          (0x13, 2, 1, "Signed greater-than comparison."),
		EQ           (0x14, 2, 1, "Equality comparison."),
		ISZERO       (0x15, 1, 1, "Simple not operator."),
		AND          (0x16, 2, 1, "Bitwise AND operation."),
		OR           (0x17, 2, 1, "Bitwise OR operation."),
		XOR          (0x18, 2, 1, "Bitwise XOR operation."),
		NOT          (0x19, 1, 1, "Bitwise NOT operation."),
		BYTE         (0x1a, 2, 1, "Retrieves single byte from word."),
		// 20s: SHA3
		SHA3         (0x20, 2, 1, "Compute Keccak-256 hash."),
		// 30s: Environment Information
		ADDRESS      (0x30, 0, 1, "Get address of currently executing account."),
		BALANCE      (0x31, 1, 1, "Get balance of the given account."),
		ORIGIN       (0x32, 0, 1, "Get execution origination address."),
		CALLER       (0x33, 0, 1, "Get caller address.  This is the address of the account that is directly responsible for this execution."),
		CALLVALUE    (0x34, 0, 1, "Get deposited value by the instruction/transaction responsible for this execution."),
		CALLDATALOAD (0x35, 1, 1, "Get input data of current environment.  This pertains to the input data passed with the message call instruction or transaction."),
		CALLDATASIZE (0x36, 0, 1, "Get size of input data in current environment."),
		CALLDATACOPY (0x37, 3, 1, "Copy input data in current environment to memory.  This pertains to the input data passed with the message call instruction or transaction."),
		CODESIZE     (0x38, 0, 1, "Get size of code running in current environment."),
		CODECOPY     (0x39, 3, 0, "Copy code running in current environment to memory."),
		GASPRICE     (0x3a, 0, 1, "Get price of gas in current environment.  This is gas price speci\fed by the originating transaction."),
		EXTCODESIZE  (0x3b, 1, 1, "Get size of an account's code."),
		EXTCODECOPY  (0x3c, 4, 0, "Copy an account's code to memory."),
		RETURNDATASIZE (0x3d, 0, 1, "Get size of output data from the previous call from the current environment."),
		RETURNDATACOPY (0x3e, 3, 0, "Copy output data from the previous call to memory."),
		// 40s: Block Information
		BLOCKHASH    (0x40, 1, 1, "Get the hash of one of the 256 most recent complete blocks."),
		COINBASE     (0x41, 0, 1, "Get the block's benefciary address."),
		TIMESTAMP    (0x42, 0, 1, "Get the block's timestamp."),
		NUMBER       (0x43, 0, 1, "Get the block's number."),
		DIFFICULTY   (0x44, 0, 1, "Get the block's difficulty."),
		GASLIMIT     (0x45, 0, 1, "Get the block's gas limit."),
		// 50s: Stack, Memory Storage and Flow Operations
		POP          (0x50, 1, 0, "Remove item from stack."),
		MLOAD        (0x51, 1, 1, "Load word from memory."),
		MSTORE       (0x52, 2, 0, "Save word to memory."),
		MSTORE8      (0x53, 2, 0, "Save byte to memory."),
		SLOAD        (0x54, 1, 1, "Load word from storage."),
		SSTORE       (0x55, 2, 0, "Save word to storage."),
		JUMP         (0x56, 1, 0, "Alter the program counter."),
		JUMPI        (0x57, 2, 0, "Conditionally alter the program counter."),
		PC           (0x58, 0, 1, "Get the value of the program counter prior to the increment corresponding to this instruction."),
		MSIZE        (0x59, 0, 1, "Get the size of active memory in bytes."),
		GAS          (0x5a, 0, 1, "Get the amount of available gas, including the corresponding reduction for the cost of this instruction."),
		JUMPDEST     (0x5b, 0, 0, "Mark a valid destination for jumps.  This operation has no eect on machine state during execution."),
		// 60s & 70s: Push Operations
		PUSH1        (0x60, 0, 1, "Place 1-byte item on stack.",1),
		PUSH2        (0x61, 0, 1, "Place 2-byte item on stack.",2),
		PUSH3        (0x62, 0, 1, "Place 3-byte item on stack.",3),
		PUSH4        (0x63, 0, 1, "Place 4-byte item on stack.",4),
		PUSH5        (0x64, 0, 1, "Place 5-byte item on stack.",5),
		PUSH6        (0x65, 0, 1, "Place 6-byte item on stack.",6),
		PUSH7        (0x66, 0, 1, "Place 7-byte item on stack.",7),
		PUSH8        (0x67, 0, 1, "Place 8-byte item on stack.",8),
		PUSH9        (0x68, 0, 1, "Place 9-byte item on stack.",9),
		PUSH10       (0x69, 0, 1, "Place 10-byte item on stack.",10),
		PUSH11       (0x6a, 0, 1, "Place 11-byte item on stack.",11),
		PUSH12       (0x6b, 0, 1, "Place 12-byte item on stack.",12),
		PUSH13       (0x6c, 0, 1, "Place 13-byte item on stack.",13),
		PUSH14       (0x6d, 0, 1, "Place 14-byte item on stack.",14),
		PUSH15       (0x6e, 0, 1, "Place 15-byte item on stack.",15),
		PUSH16       (0x6f, 0, 1, "Place 16-byte item on stack.",16),
		PUSH17       (0x70, 0, 1, "Place 17-byte item on stack.",17),
		PUSH18       (0x71, 0, 1, "Place 18-byte item on stack.",18),
		PUSH19       (0x72, 0, 1, "Place 19-byte item on stack.",19),
		PUSH20       (0x73, 0, 1, "Place 20-byte item on stack.",20),
		PUSH21       (0x74, 0, 1, "Place 21-byte item on stack.",21),
		PUSH22       (0x75, 0, 1, "Place 22-byte item on stack.",22),
		PUSH23       (0x76, 0, 1, "Place 23-byte item on stack.",23),
		PUSH24       (0x77, 0, 1, "Place 24-byte item on stack.",24),
		PUSH25       (0x78, 0, 1, "Place 25-byte item on stack.",25),
		PUSH26       (0x79, 0, 1, "Place 26-byte item on stack.",26),
		PUSH27       (0x7a, 0, 1, "Place 27-byte item on stack.",27),
		PUSH28       (0x7b, 0, 1, "Place 28-byte item on stack.",28),
		PUSH29       (0x7c, 0, 1, "Place 29-byte item on stack.",29),
		PUSH30       (0x7d, 0, 1, "Place 30-byte item on stack.",30),
		PUSH31       (0x7e, 0, 1, "Place 31-byte item on stack.",31),
		PUSH32       (0x7f, 0, 1, "Place 32-byte item on stack.",32),
		// 80s: Duplication Operations
		DUP1         (0x80, 1, 2, "Duplicate 1st stack item."),
		DUP2         (0x81, 2, 3, "Duplicate 2nd stack item."),
		DUP3         (0x82, 3, 4, "Duplicate 3rd stack item."),
		DUP4         (0x83, 4, 5, "Duplicate 4th stack item."),
		DUP5         (0x84, 5, 6, "Duplicate 5th stack item."),
		DUP6         (0x85, 6, 7, "Duplicate 6th stack item."),
		DUP7         (0x86, 7, 8, "Duplicate 7th stack item."),
		DUP8         (0x87, 8, 9, "Duplicate 8th stack item."),
		DUP9         (0x88, 9, 10, "Duplicate 9th stack item."),
		DUP10        (0x89, 10, 11, "Duplicate 10th stack item."),
		DUP11        (0x8a, 11, 12, "Duplicate 11th stack item."),
		DUP12        (0x8b, 12, 13, "Duplicate 12th stack item."),
		DUP13        (0x8c, 13, 14, "Duplicate 13th stack item."),
		DUP14        (0x8d, 14, 15, "Duplicate 14th stack item."),
		DUP15        (0x8e, 15, 16, "Duplicate 15th stack item."),
		DUP16        (0x8f, 16, 17, "Duplicate 16th stack item."),
		// 90s: Exchange Operations
		SWAP1        (0x90, 2, 2, "Exchange 1st and 2nd stack items."),
		SWAP2        (0x91, 3, 3, "Exchange 2nd and 3rd stack items."),
		SWAP3        (0x92, 4, 4, "Exchange 3rd and 4th stack items."),
		SWAP4        (0x93, 5, 5, "Exchange 4th and 5th stack items."),
		SWAP5        (0x94, 6, 6, "Exchange 5th and 6th stack items."),
		SWAP6        (0x95, 7, 7, "Exchange 6th and 7th stack items."),
		SWAP7        (0x96, 8, 8, "Exchange 7th and 8th stack items."),
		SWAP8        (0x97, 9, 9, "Exchange 8th and 9th stack items."),
		SWAP9        (0x98, 10, 10, "Exchange 9th and 10th stack items."),
		SWAP10       (0x99, 11, 11, "Exchange 10th and 11th stack items."),
		SWAP11       (0x9a, 12, 12, "Exchange 11th and 12th stack items."),
		SWAP12       (0x9b, 13, 13, "Exchange 12th and 13th stack items."),
		SWAP13       (0x9c, 14, 14, "Exchange 13th and 14th stack items."),
		SWAP14       (0x9d, 15, 15, "Exchange 14th and 15th stack items."),
		SWAP15       (0x9e, 16, 16, "Exchange 15th and 16th stack items."),
		SWAP16       (0x9f, 17, 17, "Exchange 16th and 17th stack items."),
		// a0s: Logging Operations
		LOG0        (0xa0, 2, 0, "Append log record with no topics."),
		LOG1        (0xa1, 3, 0, "Append log record with one topics."),
		LOG2        (0xa2, 4, 0, "Append log record with two topics."),
		LOG3        (0xa3, 5, 0, "Append log record with three topics."),
		LOG4        (0xa4, 6, 0, "Append log record with four topics."),
		// f0s: System operations
		CREATE      (0xf0, 3, 1, "Create a new account with associated code."),
		CALL        (0xf1, 7, 1, "Message-call into an account."),
		CALLCODE    (0xf2, 7, 1, "Message-call into this account with an alternative account's code."),
		RETURN      (0xf3, 2, 0, "Halt execution returning output data."),
		DELEGATECALL(0xf4, 6, 1, "Message-call into this account with an alternative account's code, but persisting the current values for sender and value."),
		STATICCALL  (0xfa, 6, 1, "Static message-call into an account."),
		REVERT      (0xfd, 2, 0, "Halt execution reverting state changes but returning data and remaining gas."),
		INVALID     (0xfe, 0, 0, "Designated invalid instruction."),
		SELFDESTRUCT(0xff, 1, 0, "Halt execution and register account for later deletion."),
		;

		/**
		 * The opcode for this opcode
		 */
		private final int opcode;
		/**
		 * Number of items taken off the stack (in bytes).
		 */
		private final int arguments;
		/**
		 * Number of items placed back on the stack (in bytes).
		 */
		private final int returns;
		/**
		 * Description of this opcode.
		 */
		private final String description;
		/**
		 * Width (in bytes) of operands
		 */
		private final int[] operands;
		//
		private Opcode(int opcode, int args, int returns, String description, int... operands) {
			this.opcode = opcode;
			this.arguments = args;
			this.returns = returns;
			this.description = description;
			this.operands = operands;
		}
	}

	//
	private final Opcode opcode;

	public Bytecode(Opcode opcode) {
		this.opcode = opcode;
	}

	/**
	 * Return the size of this bytecode in bytes.
	 *
	 * @return
	 */
	public int size() {
		return 1;
	}

	/**
	 * Get the description associated with this bytecode.
	 *
	 * @return
	 */
	public String getDescription() {
		return opcode.description;
	}

	/**
	 * Get the amount of gas required to execute this bytecode instruction.
	 *
	 * @return
	 */
	public abstract int getGasRequired();

	@Override
	public String toString() {
		return opcode.toString();
	}

	/**
	 * The class of bytecodes which require a fixed amount of gas to execute.
	 *
	 * @author David J. Pearce
	 *
	 */
	public static abstract class FixedGasBytecode extends Bytecode {
		private final int gas;

		public FixedGasBytecode(Opcode opcode, int gas) {
			super(opcode);
			this.gas = gas;
		}

		@Override
		public int getGasRequired() {
			return gas;
		}
	}

	public static class Stop extends FixedGasBytecode {
		public Stop() {
			super(Opcode.STOP, G_zero);
		}
	}

	public static class Add extends FixedGasBytecode {
		public Add() {
			super(Opcode.ADD, G_verylow);
		}
	}

	public static class Sub extends FixedGasBytecode {
		public Sub() {
			super(Opcode.SUB, G_verylow);
		}
	}

	public static class Mul extends FixedGasBytecode {
		public Mul() {
			super(Opcode.MUL, G_low);
		}
	}

	public static class Div extends FixedGasBytecode {
		public Div() {
			super(Opcode.DIV, G_low);
		}
	}

	public static class SignedDiv extends FixedGasBytecode {
		public SignedDiv() {
			super(Opcode.SDIV, G_low);
		}
	}

	public static class Mod extends FixedGasBytecode {
		public Mod() {
			super(Opcode.MOD, G_low);
		}
	}

	public static class SignedMod extends FixedGasBytecode {
		public SignedMod() {
			super(Opcode.SMOD, G_low);
		}
	}

	public static class AddMod extends FixedGasBytecode {
		public AddMod() {
			super(Opcode.ADDMOD, G_mid);
		}
	}

	public static class MulMod extends FixedGasBytecode {
		public MulMod() {
			super(Opcode.MULMOD, G_mid);
		}
	}

	public static class Exp extends Bytecode {
		public Exp() {
			super(Opcode.EXP);
		}

		@Override
		public int getGasRequired() {
			throw new IllegalArgumentException("implement me");
		}
	}

	public static class SignExtend extends FixedGasBytecode {
		public SignExtend() {
			super(Opcode.SIGNEXTEND, G_low);
		}
	}

	public static class LessThan extends FixedGasBytecode {
		public LessThan() {
			super(Opcode.LT, G_verylow);
		}
	}

	public static class GreaterThan extends FixedGasBytecode {
		public GreaterThan() {
			super(Opcode.GT, G_verylow);
		}
	}

	public static class SignedLessThan extends FixedGasBytecode {
		public SignedLessThan() {
			super(Opcode.SLT, G_verylow);
		}
	}

	public static class SignedGreaterThan extends FixedGasBytecode {
		public SignedGreaterThan() {
			super(Opcode.SGT, G_verylow);
		}
	}

	public static class Equal extends FixedGasBytecode {
		public Equal() {
			super(Opcode.EQ, G_verylow);
		}
	}

	public static class IsZero extends FixedGasBytecode {
		public IsZero() {
			super(Opcode.ISZERO, G_verylow);
		}
	}

	public static class And extends FixedGasBytecode {
		public And() {
			super(Opcode.AND, G_verylow);
		}
	}

	public static class Or extends FixedGasBytecode {
		public Or() {
			super(Opcode.OR, G_verylow);
		}
	}

	public static class Xor extends FixedGasBytecode {
		public Xor() {
			super(Opcode.XOR, G_verylow);
		}
	}

	public static class Not extends FixedGasBytecode {
		public Not() {
			super(Opcode.NOT, G_verylow);
		}
	}

	/**
	 * Decode a sequence of one or more bytes into the corresponding bytecode.
	 *
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static Bytecode decode(byte[] bytes, int offset) {
		byte opcode = bytes[offset++];
		switch(opcode) {
		case 0x00:
			return new Bytecode.Stop();
		case 0x01:
			return new Bytecode.Add();
		case 0x02:
			return new Bytecode.Mul();
		case 0x03:
			return new Bytecode.Sub();
		case 0x04:
			return new Bytecode.Div();
		case 0x05:
			return new Bytecode.SignedDiv();
		case 0x06:
			return new Bytecode.Mod();
		case 0x07:
			return new Bytecode.SignedMod();
		case 0x08:
			return new Bytecode.AddMod();
		case 0x09:
			return new Bytecode.MulMod();
		case 0x0a:
			return new Bytecode.Exp();
		case 0x0b:
			return new Bytecode.SignExtend();
		// 10s: Comparison & Bitwise Logic Operations
		case 0x10:
			return new Bytecode.LessThan();
		case 0x11:
			return new Bytecode.GreaterThan();
		case 0x12:
			return new Bytecode.SignedLessThan();
		case 0x13:
			return new Bytecode.SignedGreaterThan();
		case 0x14:
			return new Bytecode.Equal();
		case 0x15:
			return new Bytecode.IsZero();
		case 0x16:
			return new Bytecode.And();
		case 0x17:
			return new Bytecode.Or();
		case 0x18:
			return new Bytecode.Xor();
		case 0x19:
			return new Bytecode.Not();
//		BYTE         case 0x1a:
//		// 20s: SHA3
//		SHA3         case 0x20:
//		// 30s: Environment Information
//		ADDRESS      case 0x30:
//		BALANCE      case 0x31:
//		ORIGIN       case 0x32:
//		CALLER       case 0x33:
//		CALLVALUE    case 0x34:
//		CALLDATALOAD case 0x35:
//		CALLDATASIZE case 0x36:
//		CALLDATACOPY case 0x37:
//		CODESIZE     case 0x38:
//		CODECOPY     case 0x39:
//		GASPRICE     case 0x3a:
//		EXTCODESIZE  case 0x3b:
//		EXTCODECOPY  case 0x3c:
//		RETURNDATASIZE case 0x3d:
//		RETURNDATACOPY case 0x3e:
//		// 40s: Block Information
//		BLOCKHASH    case 0x40:
//		COINBASE     case 0x41:
//		TIMESTAMP    case 0x42:
//		NUMBER       case 0x43:
//		DIFFICULTY   case 0x44:
//		GASLIMIT     case 0x45:
//		// 50s: Stack, Memory Storage and Flow Operations
//		POP          case 0x50:
//		MLOAD        case 0x51:
//		MSTORE       case 0x52:
//		MSTORE8      case 0x53:
//		SLOAD        case 0x54:
//		SSTORE       case 0x55:
//		JUMP         case 0x56:
//		JUMPI        case 0x57:
//		PC           case 0x58:
//		MSIZE        case 0x59:
//		GAS          case 0x5a:
//		JUMPDEST     case 0x5b:
//		// 60s & 70s: Push Operations
//		PUSH1        case 0x60:
//		PUSH2        case 0x61:
//		PUSH3        case 0x62:
//		PUSH4        case 0x63:
//		PUSH5        case 0x64:
//		PUSH6        case 0x65:
//		PUSH7        case 0x66:
//		PUSH8        case 0x67:
//		PUSH9        case 0x68:
//		PUSH10       case 0x69:
//		PUSH11       case 0x6a:
//		PUSH12       case 0x6b:
//		PUSH13       case 0x6c:
//		PUSH14       case 0x6d:
//		PUSH15       case 0x6e:
//		PUSH16       case 0x6f:
//		PUSH17       case 0x70:
//		PUSH18       case 0x71:
//		PUSH19       case 0x72:
//		PUSH20       case 0x73:
//		PUSH21       case 0x74:
//		PUSH22       case 0x75:
//		PUSH23       case 0x76:
//		PUSH24       case 0x77:
//		PUSH25       case 0x78:
//		PUSH26       case 0x79:
//		PUSH27       case 0x7a:
//		PUSH28       case 0x7b:
//		PUSH29       case 0x7c:
//		PUSH30       case 0x7d:
//		PUSH31       case 0x7e:
//		PUSH32       case 0x7f:
//		// 80s: Duplication Operations
//		DUP1         case 0x80:
//		DUP2         case 0x81:
//		DUP3         case 0x82:
//		DUP4         case 0x83:
//		DUP5         case 0x84:
//		DUP6         case 0x85:
//		DUP7         case 0x86:
//		DUP8         case 0x87:
//		DUP9         case 0x88:
//		DUP10        case 0x89:
//		DUP11        case 0x8a:
//		DUP12        case 0x8b:
//		DUP13        case 0x8c:
//		DUP14        case 0x8d:
//		DUP15        case 0x8e:
//		DUP16        case 0x8f:
//		// 90s: Exchange Operations
//		SWAP1        case 0x90:
//		SWAP2        case 0x91:
//		SWAP3        case 0x92:
//		SWAP4        case 0x93:
//		SWAP5        case 0x94:
//		SWAP6        case 0x95:
//		SWAP7        case 0x96:
//		SWAP8        case 0x97:
//		SWAP9        case 0x98:
//		SWAP10       case 0x99:
//		SWAP11       case 0x9a:
//		SWAP12       case 0x9b:
//		SWAP13       case 0x9c:
//		SWAP14       case 0x9d:
//		SWAP15       case 0x9e:
//		SWAP16       case 0x9f:
//		// a0s: Logging Operations
//		LOG0        case 0xa0:
//		LOG1        case 0xa1:
//		LOG2        case 0xa2:
//		LOG3        case 0xa3:
//		LOG4        case 0xa4:
//		// f0s: System operations
//		CREATE      case 0xf0:
//		CALL        case 0xf1:
//		CALLCODE    case 0xf2:
//		RETURN      case 0xf3:
//		DELEGATECALLcase 0xf4:
//		STATICCALL  case 0xfa:
//		REVERT      case 0xfd:
//		INVALID     case 0xfe:
//		SELFDESTRUCTcase 0xff:
		default:
			throw new IllegalArgumentException("unknown bytecode opcode: " + opcode);
		}
	}

	public static void main(String[] args) {
		System.out.println(Opcode.STOP.ordinal());
	}
}
