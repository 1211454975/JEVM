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

import static jevm.util.Word.*;

public class Account {
	/**
	 * A scalar value equal to the number of transactions sent from this address
	 * or, in the case of accounts with associated code, the number of
	 * contract-creations made by this account.
	 */
	private w256 nonce;

	/**
	 * A scalar value equal to the number of Wei owned by this address
	 */
	private w256 balance;

	/**
	 * A 256-bit hash of the root node of a Merkle Patricia tree that encodes the
	 * storage contents of the account (a mapping between 256-bit integer values),
	 * encoded into the trie as a mapping from the Keccak 256-bit hash of the
	 * 256-bit integer keys to the RLP-encoded 256-bit integer values
	 */
	private w256 storage;

	/**
	 * The hash of the EVM code of this account—this is the code that gets executed
	 * should this address receive a message call; it is immutable and thus, unlike
	 * all other fields, cannot be changed after construction. All such code
	 * fragments are contained in the state database un- der their corresponding
	 * hashes for later retrieval
	 */
	private final w256 codeHash;

	/**
	 * Construct a new account, with a corresponding code hash.
	 *
	 * @param codeHash
	 */
	public Account(w256 codeHash) {
		this.nonce = new w256(0);
		this.balance = new w256(0);
		this.codeHash = codeHash;
	}
}
