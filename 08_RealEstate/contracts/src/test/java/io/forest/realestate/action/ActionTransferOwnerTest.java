package io.forest.realestate.action;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
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
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.testing.core.TestIdentity;

@ExtendWith(MockitoExtension.class)
class ActionTransferOwnerTest {

	@InjectMocks
	private ActionTransferOwner classUnderTest;

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
	
	@Spy
	List<PublicKey> signers;

	@BeforeEach
	void setup() {
		spyInputPropertyList = spy(new ArrayList<PropertyDetails>());
		spyInputList = spy(new ArrayList<StateAndRef<ContractState>>());

		spyOutputPropertyList = spy(new ArrayList<PropertyDetails>());
		spyOutputList = spy(new ArrayList<TransactionState<ContractState>>());
		
		signers = spy(new ArrayList<PublicKey>());
	}

	@Test
	@ExtendWith(PropertyDetailsResolver.class)
	@ExtendWith(TestIdentityResolver.class)
	void testAction_withApproved_expectSuccess(TestIdentity identity, PropertyDetails propertyDetail) {
		Party party = identity.getParty();
		PropertyDetails actual = classUnderTest.action(propertyDetail, party);

		assertAll(() -> assertEquals(actual.getOwner().getName().getOrganisation(),
				identity.getName().getOrganisation()));
	}

	@Test
	@ExtendWith(PropertyDetailsResolver.class)
	@ExtendWith(TestIdentityResolver.class)
	void testVerify_withChangeOwnership_expectSuccess(PropertyDetails propertyDetail, TestIdentity owner) {

		when(txn.getInputs()).thenReturn(spyInputList);
		when(spyInputList.size()).thenReturn(1);
		
		spyInputPropertyList.add(propertyDetail);
		when(txn.inputsOfType(PropertyDetails.class)).thenReturn(spyInputPropertyList);
		
		when(txn.getOutputs()).thenReturn(spyOutputList);
		when(spyOutputList.size()).thenReturn(1);

		PropertyDetails newOwner = propertyDetail.toBuilder().owner(owner.getParty()).build();
		spyOutputPropertyList.add(newOwner);
		when(txn.outputsOfType(PropertyDetails.class)).thenReturn(spyOutputPropertyList);
		
		when(commands.getSigners()).thenReturn(signers);
		when(signers.size()).thenReturn(1);

		classUnderTest.verify(commands, txn);
	}
}
