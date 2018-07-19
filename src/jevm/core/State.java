package jevm.core;

import java.util.Map;

import jevm.util.Word.w160;

public class State {
	/**
	 * A mapping between addresses (160-bit identifiers) and account states
	 */
	private Map<w160,Account> accounts;

}
