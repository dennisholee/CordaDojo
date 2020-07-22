package io.forest.realestate.flows;

import static net.corda.testing.node.TestCordapp.findCordapp;

import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.google.common.collect.ImmutableList;

import net.corda.core.concurrent.CordaFuture;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;

class InitiatePropertyFlowTest {

	private static final String NAME = Faker.instance().name().name();


	private MockNetwork network = new MockNetwork(new MockNetworkParameters(ImmutableList
			.of(findCordapp("io.forest.realestate.contracts"), findCordapp("io.forest.realestate.flows"))));;
	
	private StartedMockNode estateAgent = network.createNode(new CordaX500Name("EstateAgent", "London", "GB"));

	@BeforeEach
	void setup() {
		//network.startNodes();
		network.runNetwork();
	}

	@Test
	void test() throws InterruptedException, ExecutionException, SignatureException {
		Party party = estateAgent.getInfo().getLegalIdentities().get(0);
		
		Faker faker = new Faker();
		InitiatePropertyFlow flow = new InitiatePropertyFlow(
				faker.number().randomDigitNotZero(), 
				faker.address().fullAddress(), 
				faker.number().randomDigitNotZero(), 
				faker.number().randomDigitNotZero(), 
				faker.number().randomDigitNotZero(), 
				"", 
				"",
				party);

		CordaFuture<SignedTransaction> future = estateAgent.startFlow(flow);

		network.runNetwork();
		
		SignedTransaction signedTransaction = future.get();
		signedTransaction.verifySignaturesExcept(estateAgent.getInfo().getLegalIdentities().get(0).getOwningKey());
	}

	@AfterEach
	void cleanup() {
		network.stopNodes();
	}

}
