package io.forest.realestate.action;

import static net.corda.core.contracts.ContractsDSL.requireThat;

import java.util.List;

import io.forest.realestate.contracts.PropertyCommandVerifier;
import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.contracts.Verifier;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.transactions.LedgerTransaction;

public class ActionApprovedByBank implements PropertyAction<Boolean>, Verifier {

	@Override
	public PropertyDetails action(PropertyDetails propertyDetails, Boolean isApproved) {
		return propertyDetails.toBuilder().isMortgageApproved(isApproved).build();
	}

	@Override
	public void verify(CommandWithParties<PropertyCommands> commands, LedgerTransaction txn) {
		requireThat(require -> {
			require.using("Property mortgage transaction should only consume one input state.", txn.getInputs().size() == 1);
			PropertyDetails inputPropertyDetail = txn.inputsOfType(PropertyDetails.class).get(0);
			
			require.using("Property mortgage transaction should only create one output state", txn.getOutputs().size() == 1);
			PropertyDetails outputPropertyDetail = txn.outputsOfType(PropertyDetails.class).get(0);
			
			
			require.using("Property mortgage transaction input state should not have mortgage.", inputPropertyDetail.isMortgageApproved() == false);
			require.using("Property mortgage transaction outpu state should have mortgage", outputPropertyDetail.isMortgageApproved() == true);
			
			return null;
		});
	}
}
