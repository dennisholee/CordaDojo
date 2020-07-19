package io.forest.realestate.action;

import static net.corda.core.contracts.ContractsDSL.requireThat;

import io.forest.realestate.contracts.PropertyCommandVerifier;
import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.contracts.Verifier;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Requirements;
import net.corda.core.transactions.LedgerTransaction;

public class ActionApprovedBySurveyor implements PropertyAction<Boolean>, Verifier{
	@Override
	public PropertyDetails action(PropertyDetails propertyDetails, Boolean isApproved) {
		return propertyDetails.toBuilder().isSurveyorApproved(isApproved).build();
	}

	@Override
	public void verify(CommandWithParties<PropertyCommands> commands, LedgerTransaction txn) {
		requireThat(require -> {
			require.using("A Surveyor approved transaction should consume one input states.", txn.getInputs().size() == 1);
			PropertyDetails inputPropertyDetail = txn.inputsOfType(PropertyDetails.class).get(0);
			
			require.using("A Surveyor approved transaction should create one output states.", txn.getOutputs().size() == 1);
			PropertyDetails outputPropertyDetail = txn.outputsOfType(PropertyDetails.class).get(0);
			
			require.using("Property surveyor transaction input state should not have surveyed.", inputPropertyDetail.isSurveyorApproved() == false);
			require.using("Property surveyor transaction outpu state should be surveyed", outputPropertyDetail.isSurveyorApproved() == true);
			return null;
		});
	}
}
