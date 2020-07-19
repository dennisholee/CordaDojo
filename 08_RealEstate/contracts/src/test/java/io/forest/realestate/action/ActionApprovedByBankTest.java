package io.forest.realestate.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.TransactionState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

@ExtendWith(MockitoExtension.class)
class ActionApprovedByBankTest {

	@InjectMocks
	private ActionApprovedByBank action;

	@Mock
	private PropertyDetails propertyDetail;

	@Mock
	private LedgerTransaction txn;

	@Mock
	private CommandWithParties<PropertyCommands> commands;
	
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
	void testAction_withApproved_expectSuccess(PropertyDetails propertyDetail) {
		assertFalse(propertyDetail.isMortgageApproved());
		
		PropertyDetails actual = action.action(propertyDetail, true);
		
		assertAll(
			() -> assertTrue(actual.isMortgageApproved())
		);
	}

	@Test
	@ExtendWith(PropertyDetailsResolver.class)
	void testVerify_withValidTransaction_expectSuccess(PropertyDetails propertyDetail) {

		when(txn.getInputs()).thenReturn(spyInputList);
		when(spyInputList.size()).thenReturn(1);
		
		spyInputPropertyList.add(propertyDetail);
		when(txn.inputsOfType(PropertyDetails.class)).thenReturn(spyInputPropertyList);
		
		when(txn.getOutputs()).thenReturn(spyOutputList);
		when(spyOutputList.size()).thenReturn(1);
		
		PropertyDetails outputPropertyDetail = propertyDetail.toBuilder().isMortgageApproved(true).build();
		spyOutputPropertyList.add(outputPropertyDetail);
		when(txn.outputsOfType(PropertyDetails.class)).thenReturn(spyOutputPropertyList);
		
		action.verify(commands, txn);

	}
}
