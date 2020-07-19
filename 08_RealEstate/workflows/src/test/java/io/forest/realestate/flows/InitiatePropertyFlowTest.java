package io.forest.realestate.flows;

import static net.corda.testing.node.TestCordapp.findCordapp;

import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.google.common.collect.ImmutableList;

//import io.forest.realestate.action.PropertyDetailsResolver;
import io.forest.realestate.states.PropertyDetails;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;

class InitiatePropertyFlowTest {
	
	private MockNetwork network = new MockNetwork(new MockNetworkParameters(ImmutableList.of(
		findCordapp("io.forest.realestate.contracts"),
		findCordapp("io.forest.realestate.flows")
	)));
	
	private StartedMockNode estateAgent;

	
	@Before
	void setup() {
		estateAgent = network.createNode(new CordaX500Name("EstateAgent", "London", "GB"));
		network.startNodes();
		network.runNetwork();
	}
	
//	@Test
//	@ExtendWith(PropertyDetailsResolver.class)
//	void test(PropertyDetails propertyDetails) throws InterruptedException, ExecutionException, SignatureException {
//		InitiatePropertyFlow flow = new InitiatePropertyFlow(
//				propertyDetails.getPropertyId(),
//				propertyDetails.getPropertyAddress(), 
//				propertyDetails.getPropertyPrice(), 
//				propertyDetails.getBuyerId(), 
//				propertyDetails.getSellerId(), 
//				propertyDetails.getUpdatedBy(), 
//				propertyDetails.getUpdatedTime(), 
//				propertyDetails.getOwner());
//		
//		CordaFuture<SignedTransaction> future = estateAgent.startFlow(flow);
//		
//		
//		SignedTransaction signedTransaction = future.get();
//		signedTransaction.verifySignaturesExcept(estateAgent.getInfo().getLegalIdentities().get(0).getOwningKey());
//	
//	}
//	
	@After
	void cleanup () {
		network.stopNodes();
	}

}
