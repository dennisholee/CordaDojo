package io.forest.realestate.action;

import static net.corda.core.contracts.ContractsDSL.requireThat;

import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.contracts.Verifier;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

/**
 * 
 * 
 * @author dennislee
 */
public class ActionTransferOwner implements PropertyAction<Party>, Verifier{
	
	@Override
	public PropertyDetails action(PropertyDetails propertyDetails, Party party) {
		return propertyDetails.toBuilder().owner(party).build();
	}
	
	@Override
	public void verify(CommandWithParties<PropertyCommands> command, LedgerTransaction txn) {
        requireThat(require -> {
            require.using("A Property Transfer transaction should only consume one input state.",
                    txn.getInputs().size() == 1);
            require.using("A Property Transfer transaction should only create one output state.",
                    txn.getOutputs().size() == 1);
            
            final PropertyDetails in = txn.inputsOfType(PropertyDetails.class).get(0);
            final PropertyDetails out = txn.outputsOfType(PropertyDetails.class).get(0);
            
            require.using("The owner Property must change in a Property Transfer transaction.",
                    in.getOwner() != out.getOwner());
            require.using("There must only be one signer (the current owner) in a Property Transfer transaction.",
                    command.getSigners().size() == 1);
            
            return null;
        });
    }
}
