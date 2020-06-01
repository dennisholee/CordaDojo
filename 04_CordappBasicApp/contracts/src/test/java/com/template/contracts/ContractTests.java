package com.template.contracts;

import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;

import static net.corda.testing.node.NodeTestUtils.ledger;

import java.util.Arrays;
import static java.util.Collections.singletonList;

import org.junit.Test;

import com.template.states.TemplateState;

import kotlin.Unit;

public class ContractTests {
	private TestIdentity megaCorp = new TestIdentity(new CordaX500Name("PartyA", "London", "GB"));
    private TestIdentity miniCorp = new TestIdentity(new CordaX500Name("PartyB", "New York", "US"));
    
    private final MockServices ledgerServices = new MockServices(
    		Arrays.asList("com.template.contracts", "com.template.states"));

    @Test
    public void dummyTest() {
    	TemplateState state = new TemplateState("FooBar", miniCorp.getParty());
    	
    	ledger(ledgerServices, (l -> {
    		l.transaction(tx -> {
    			tx.output(TemplateContract.ID, state);
    		
    			tx.command(Arrays.asList(megaCorp.getPublicKey(), miniCorp.getPublicKey()), new TemplateContract.Commands.Create());
    			return tx.verifies();
    		});
    		return Unit.INSTANCE;
    	}));
    }
}