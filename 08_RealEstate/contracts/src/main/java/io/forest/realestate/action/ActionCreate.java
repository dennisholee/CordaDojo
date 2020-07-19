package io.forest.realestate.action;

import static net.corda.core.contracts.ContractsDSL.requireThat;

import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.contracts.Verifier;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.transactions.LedgerTransaction;


public class ActionCreate implements Verifier{

	@Override
	public void verify(CommandWithParties<PropertyCommands> commands, LedgerTransaction txn) {
		requireThat(require -> {
            require.using("A Property Transfer transaction should consume no input states.",
                    txn.getInputs().isEmpty());
            require.using("A Property Transfer transaction should only create one output state.",
                    txn.getOutputs().size() == 1);
            final PropertyDetails out = txn.outputsOfType(PropertyDetails.class).get(0);
            return null;
		});
		
	}

}
