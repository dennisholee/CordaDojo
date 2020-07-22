package io.forest.realestate.action;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.github.javafaker.Faker;

import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.testing.core.TestIdentity;

public class PropertyDetailsResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == PropertyDetails.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Faker faker = new Faker();
		
	//	TestIdentity ownerIdentity = new TestIdentity(new CordaX500Name(faker.name().name(), "London", "GB"));
		
		int propertyId = faker.number().randomDigitNotZero();
		String propertyAddress = faker.address().fullAddress();
		int propertyPrice = faker.number().numberBetween(100000, 1000000);
		int buyerId = 0;
		int sellerId = 0;
		boolean isMortgageApproved = false;
		boolean isSurveyorApproved = false;
		Party owner = null;// ownerIdentity.getParty();
		String description = faker.lorem().sentence();
		String updatedBy = null;
		String updatedTime = null;
		UniqueIdentifier linearId = null;

		return new PropertyDetails(propertyId, propertyAddress, propertyPrice, buyerId, sellerId, isMortgageApproved,
				isSurveyorApproved, owner, description, updatedBy, updatedTime, linearId);
	}

}
