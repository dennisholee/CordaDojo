package io.forest.realestate.action;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;

public class PropertyDetailsResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == PropertyDetails.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		int propertyId = 0;
		String propertyAddress = null;
		int propertyPrice = 0;
		int buyerId = 0;
		int sellerId = 0;
		boolean isMortgageApproved = false;
		boolean isSurveyorApproved = false;
		Party owner = null;
		String description = null;
		String updatedBy = null;
		String updatedTime = null;
		UniqueIdentifier linearId = null;

		return new PropertyDetails(propertyId, propertyAddress, propertyPrice, buyerId, sellerId, isMortgageApproved,
				isSurveyorApproved, owner, description, updatedBy, updatedTime, linearId);
	}

}
