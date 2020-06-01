package com.template.flows;

import com.template.contracts.TemplateContract;
import com.template.states.TemplateState;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import static com.template.contracts.TemplateContract.ID;


// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class Initiator extends FlowLogic<SignedTransaction> {
    private final ProgressTracker progressTracker = new ProgressTracker();

    
    private final String name;
    private final Party otherParty;
    
    public Initiator(String name, Party otherParty) {
    	this.name = name;
    	this.otherParty = otherParty;
    }
    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Initiator flow logic goes here.
    	
    	TemplateState state = new TemplateState(this.name, this.otherParty);
    	
    	CommandData cmdType = new TemplateContract.Commands.Action();
        Command cmd = new Command<>(cmdType, getOurIdentity().getOwningKey());
        
        final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        
        final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(state, ID)
                .addCommand(cmd);

        // Signing the transaction.
        final SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

        // Finalising the transaction.
        return subFlow(new FinalityFlow(signedTx));
    }
}
