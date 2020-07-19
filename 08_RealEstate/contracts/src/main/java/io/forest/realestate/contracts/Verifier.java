package io.forest.realestate.contracts;

import net.corda.core.contracts.CommandWithParties;
import net.corda.core.transactions.LedgerTransaction;

public interface Verifier {

	void verify(CommandWithParties<PropertyCommands> commands, LedgerTransaction txn);
}
