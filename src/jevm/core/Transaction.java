package jevm.core;

import static jevm.util.Number.*;

/**
 *
 * @author David J. Pearce
 *
 */
public class Transaction {
	/**
	 * A scalar value equal to the number of transactions sent by the sender;
	 * formally T_n.
	 */
	private u256 nonce;
	/**
	 * A scalar value equal to the number of Wei to be paid per unit of gas for all
	 * computa- tion costs incurred as a result of the execution of this
	 * transaction; formally T_p.
	 */
	private u256 gasPrice;
	/**
	 * A scalar value equal to the maximum amount of gas that should be used in
	 * executing this transaction. This is paid up-front, before any computation is
	 * done and may not be increased later; formally T_g.
	 */
	private u256 gasLimit;
	private u160 to;
	private u256 value;
	private u256 v, r, s;
}
