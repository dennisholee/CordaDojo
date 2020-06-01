package com.template;

import com.google.common.collect.ImmutableList;
import com.template.flows.Initiator;
import com.template.flows.Responder;

import net.corda.core.concurrent.CordaFuture;
import net.corda.core.transactions.SignedTransaction;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import net.corda.testing.node.TestCordapp;

import java.security.SignatureException;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlowTests {
    private final MockNetwork network = new MockNetwork(new MockNetworkParameters(ImmutableList.of(
        TestCordapp.findCordapp("com.template.contracts"),
        TestCordapp.findCordapp("com.template.flows")
    )));
   
    private final StartedMockNode a = network.createPartyNode(null);
    private final StartedMockNode b = network.createPartyNode(null);

    public FlowTests() {
        a.registerInitiatedFlow(Responder.class);
        b.registerInitiatedFlow(Responder.class);
    }

    @Before
    public void setup() {
        network.runNetwork();
    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

    @Test
    public void dummyTest() throws InterruptedException, ExecutionException, SignatureException {
    	
    	Initiator flow = new Initiator("FooBar", b.getInfo().getLegalIdentities().get(0));
        CordaFuture<SignedTransaction> future = a.startFlow(flow);
        network.runNetwork();

        SignedTransaction signedTx = future.get();
        signedTx.verifySignaturesExcept(b.getInfo().getLegalIdentities().get(0).getOwningKey());

    }
}
