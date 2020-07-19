package io.forest.realestate.flows;

import static net.corda.testing.node.TestCordapp.findCordapp;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import net.corda.core.identity.CordaX500Name;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;

class InitiatePropertyFlowTest {
	
	private MockNetwork network = new MockNetwork(new MockNetworkParameters(ImmutableList.of(
		findCordapp("io.forest.realestate.contracts"),
		findCordapp("io.forest.realestate.flows")
	)));
	
	private StartedMockNode estateAgent;

	private StartedMockNode bank;

	
	@Before
	void setup() {
		estateAgent = network.createNode(new CordaX500Name("EstateAgent", "London", "GB"));
		bank = network.createNode(new CordaX500Name("Bank", "London", "GB"));
	}
	
//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
//	
	@After
	void cleanup () {
		network.stopNodes();
	}

}
