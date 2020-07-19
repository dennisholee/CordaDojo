package com.template.flows;

import static net.corda.testing.node.TestCordapp.findCordapp;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import net.corda.core.identity.CordaX500Name;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;

class InitiatorTest {

	private MockNetwork network = new MockNetwork(new MockNetworkParameters(ImmutableList.of(
		findCordapp("io.forest.realestate.contracts"),
		findCordapp("io.forest.realestate.flows")
	)));
	
	private StartedMockNode estateAgent;

	@Before
	void setup() {
		estateAgent = network.createNode(new CordaX500Name("EstateAgent", "London", "GB"));
	}
	

	@Test
	void test() {
	//	SignedTransaction transaction = estateAgent.getServices().getValidatedTransactions().getTransaction(transaction.getId());
	}

	@After
	void cleanup () {
		network.stopNodes();
	}
}
