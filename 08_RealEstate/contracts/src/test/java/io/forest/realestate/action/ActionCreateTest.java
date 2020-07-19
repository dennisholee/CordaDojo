package io.forest.realestate.action;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
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
import net.corda.core.transactions.LedgerTransaction;

@ExtendWith(MockitoExtension.class)
class ActionCreateTest {

	@InjectMocks
	private ActionCreate action;
	
	@Mock
	private CommandWithParties<PropertyCommands> commands;
	
	@Mock
	private LedgerTransaction txn;

	@Spy
	private List<StateAndRef<ContractState>> spyInputList;

	@Spy
	private List<TransactionState<ContractState>> spyOutputList;

	@Spy
	private List<PropertyDetails> spyOutputPropertList;
	
	@BeforeEach
	void setup() {
		spyInputList = spy(new ArrayList<StateAndRef<ContractState>>());	
		spyOutputList = spy(new ArrayList<TransactionState<ContractState>>());
		spyOutputPropertList = spy(new ArrayList<PropertyDetails>());
	}
	
	@Test
	@ExtendWith(PropertyDetailsResolver.class)
	void test(PropertyDetails propertyDetails) {
		when(txn.getInputs()).thenReturn(spyInputList);
		
		when(txn.getOutputs()).thenReturn(spyOutputList);
		when(spyOutputList.size()).thenReturn(1);
		
		spyOutputPropertList.add(propertyDetails);
		when(txn.outputsOfType(PropertyDetails.class)).thenReturn(spyOutputPropertList);
		
		action.verify(commands, txn);
	}

}
