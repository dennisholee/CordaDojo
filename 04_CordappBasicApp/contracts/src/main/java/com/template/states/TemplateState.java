package com.template.states;

import com.template.contracts.TemplateContract;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import net.corda.core.contracts.Command;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.flows.FlowException;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(TemplateContract.class)
public class TemplateState implements ContractState {

    private final String name;
    private final Party author;

    public TemplateState(String name, Party author) {
        this.name = name;
        this.author = author;

    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(this.author);
    }
}
