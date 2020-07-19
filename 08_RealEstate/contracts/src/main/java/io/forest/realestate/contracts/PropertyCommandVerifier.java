package io.forest.realestate.contracts;

import org.jetbrains.annotations.NotNull;

import io.forest.realestate.action.ActionApprovedByBank;
import io.forest.realestate.action.ActionApprovedBySurveyor;
import io.forest.realestate.action.ActionCreate;
import io.forest.realestate.action.ActionTransferOwner;
import net.corda.core.contracts.CommandWithParties;

public class PropertyCommandVerifier {
	
	public PropertyCommandVerifier() {
		
	}

	public static Verifier instance(@NotNull CommandWithParties<PropertyCommands> commandType) {
		PropertyCommands propertyType = commandType.getValue();
		
		if(propertyType instanceof PropertyCommands.Create) {
			return new ActionCreate();
		} else if ( propertyType instanceof PropertyCommands.Transfer ) {
			return new ActionTransferOwner();
		} else if ( propertyType instanceof PropertyCommands.BankApproval ) {
			return new ActionApprovedByBank();
		} else if (propertyType instanceof PropertyCommands.SurveyorApproval ) {
			return new ActionApprovedBySurveyor();
		}
		
		throw new InvalidPropertyCommandException(String.format("Unrecognized property command [type=%s]", propertyType.getClass()));
	}

}
