package io.forest.realestate.action;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.TransactionState;
import net.corda.core.transactions.LedgerTransaction;

@ExtendWith(MockitoExtension.class)
class ActionApprovedBySurveyorTest {

	@InjectMocks
	private ActionApprovedBySurveyor classUnderTest;
	
	@Mock
	private CommandWithParties<PropertyCommands> commands;
	
	@Mock
	private LedgerTransaction txn;
	
	@Spy
	List<StateAndRef<ContractState>> spyInputList;

	@Spy
	List<TransactionState<ContractState>> spyOutputList;

	@Spy
	List<PropertyDetails> spyInputPropertyList;

	@Spy
	List<PropertyDetails> spyOutputPropertyList;
	
	@BeforeEach
	void setup() {
		spyInputPropertyList = spy(new ArrayList<PropertyDetails>());
		spyInputList = spy(new ArrayList<StateAndRef<ContractState>>());

		spyOutputPropertyList = spy(new ArrayList<PropertyDetails>());
		spyOutputList = spy(new ArrayList<TransactionState<ContractState>>());
	}
	
	@Test
	@ExtendWith(PropertyDetailsResolver.class)
	void test(PropertyDetails propertyDetails) {
		PropertyDetails actual = classUnderTest.action(propertyDetails, true);
		
		assertAll(
			() -> assertNotEquals(propertyDetails, actual),
			() -> assertTrue(actual.isSurveyorApproved())
		);
	}
	
	@Test
	@ExtendWith(PropertyDetailsResolver.class)
	void testVerify_withValidTransaction_expectSuccess(PropertyDetails propertyDetail) {
		when(txn.getInputs()).thenReturn(spyInputList);
		when(spyInputList.size()).thenReturn(1);
		
		when(txn.inputsOfType(PropertyDetails.class)).thenReturn(spyInputPropertyList);
		spyInputPropertyList.add(propertyDetail);
		when(spyInputPropertyList.get(0)).thenReturn(propertyDetail);
		
		when(txn.getOutputs()).thenReturn(spyOutputList);
		when(spyOutputList.size()).thenReturn(1);

		PropertyDetails surveyourApprovedProperty = propertyDetail.toBuilder().isSurveyorApproved(true).build();
		spyOutputPropertyList.add(surveyourApprovedProperty);
		when(txn.outputsOfType(PropertyDetails.class)).thenReturn(spyOutputPropertyList);

		classUnderTest.verify(commands, txn);

	}

}
