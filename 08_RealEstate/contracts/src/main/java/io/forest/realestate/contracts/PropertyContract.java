package io.forest.realestate.contracts;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;

import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

public class PropertyContract implements Contract {

	public static final String ID = "io.forest.realestate.contracts.PropertyContract";

	@Override
	public void verify(LedgerTransaction txn) throws IllegalArgumentException {
		CommandWithParties<PropertyCommands> command = requireSingleCommand(txn.getCommands(), PropertyCommands.class);

		PropertyCommandVerifier.instance(command).verify(command, txn);
	}
}
