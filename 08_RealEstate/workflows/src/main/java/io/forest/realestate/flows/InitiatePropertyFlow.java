package io.forest.realestate.flows;

import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

import co.paralleluniverse.fibers.Suspendable;
import io.forest.realestate.contracts.PropertyCommands;
import io.forest.realestate.contracts.PropertyCommands.Create;
import io.forest.realestate.contracts.PropertyContract;
import io.forest.realestate.states.PropertyDetails;
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
import net.corda.core.utilities.ProgressTracker.Step;

@InitiatingFlow
@StartableByRPC
public class InitiatePropertyFlow extends FlowLogic<SignedTransaction>{
	
	private final Party owner;
	private final int propertyId;
	private final String propertyAddress;
	private final int propertyPrice;
	private final int buyerId;
	private final int sellerId;
	private final String updatedBy;
	private final String updatedTime;
	
	private final Step GENERATING_TRANSACTION = new Step("Generating transaction based on new PropertyDetails.");
	private final Step SIGNING_TRANSACTION = new Step("Signing transaction with our private key.");
	private final Step FINALISING_TRANSACTION = new Step("Obtaining notary signature and recording transaction.") {
		@Override
		public ProgressTracker childProgressTracker() {
			return FinalityFlow.Companion.tracker();
		}
	};

	// The progress tracker checkpoints each stage of the flow and outputs the
	// specified messages when each
	// checkpoint is reached in the code.
	private final ProgressTracker progressTracker = new ProgressTracker(GENERATING_TRANSACTION, SIGNING_TRANSACTION,
			FINALISING_TRANSACTION);

	public InitiatePropertyFlow(int propertyId, String propertyAddress, int propertyPrice, int buyerId, int sellerId,
			String updatedBy, String updatedTime, Party owner) {
		this.propertyId = propertyId;
		this.propertyAddress = propertyAddress;
		this.propertyPrice = propertyPrice;
		this.owner = owner;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
	}

	@Override
	public ProgressTracker getProgressTracker() {
		return progressTracker;
	}

	@Suspendable
	public SignedTransaction call() throws FlowException {
		
		progressTracker.setCurrentStep(GENERATING_TRANSACTION);
		
		PropertyDetails propertyDetails = new PropertyDetails(
				propertyId, 
				propertyAddress, 
				propertyPrice, 
				buyerId,
				sellerId,
				false, 
				false, 
				owner, 
				"New Property Transaction Initiated", 
				updatedBy, 
				updatedTime);
		
		progressTracker.setCurrentStep(SIGNING_TRANSACTION);
		
		PublicKey publicKey = getOurIdentity().getOwningKey();
		CommandData commandData = new PropertyCommands.Create();
		Command<CommandData> command = new Command<CommandData>(commandData, publicKey);
		
		Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
		
		TransactionBuilder builder = new TransactionBuilder();
		builder.setNotary(notary);
		builder.addCommand(command);
		builder.addOutputState(propertyDetails);
		builder.setTimeWindow(Instant.now(), Duration.ofSeconds(10));
		
		progressTracker.setCurrentStep(FINALISING_TRANSACTION);
		
		builder.verify(getServiceHub());
        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(builder);
        FinalityFlow finalityFlow = new FinalityFlow(signedTransaction,  Collections.emptyList());
        
		return subFlow(finalityFlow);
	}
}
