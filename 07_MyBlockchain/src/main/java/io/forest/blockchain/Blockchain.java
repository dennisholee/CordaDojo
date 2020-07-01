package io.forest.blockchain;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;

public class Blockchain {
	
	private Logger log = Logger.getLogger(Blockchain.class.getName());
	
	private static int difficulty = 3;

	private Block head;
	
	private Block tail;
	
	private Block unconfirmedHead;
	
	private Block unconfirmedTail;
	
	public Blockchain() {
		
		this.head = createGenesisBlock();
		this.tail = this.head;
		
	}
	
	public Block tail() {
		return this.tail;
	}
	
	public boolean append(Block block, String proof) {
		String tailHashCode = this.tail.getHash();
		
		// Check new block's previous hash is the same as the current tail's hash.
		if(!StringUtils.equalsIgnoreCase(tailHashCode, block.getPrevHash())) {
			return false;
		}
		if(!isValidProof(block, proof)) {
			return false;
		}
		
		this.tail.setNext(block);
		block.setPrev(this.tail);
		this.tail = block;
		
		return true;
	}
	
	public boolean appendUnconfirmed(Block block) {
		if(this.unconfirmedHead == null) {
			this.unconfirmedHead = block;
			this.unconfirmedTail = block;
		} else {
			block.setPrev(this.unconfirmedTail);
			this.unconfirmedTail.setNext(block);
			this.unconfirmedTail = block;
		}
		return true;
	}
	
	public static int getDifficulty() {
		return difficulty;
	}

	
	public Block getHead() {
		return head;
	}

	public Block getTail() {
		return tail;
	}

	public Block getUnconfirmedHead() {
		return unconfirmedHead;
	}

	public Block getUnconfirmedTail() {
		return unconfirmedTail;
	}

	void mine() throws NoSuchAlgorithmException {
		if(this.unconfirmedHead == null) {
			return;
		}
		
		Block tailBlock = this.tail;
		String tailHash = tailBlock.getHash();
		
		Block unconfirmedBlock = this.unconfirmedHead;
		
		Block block = new Block();
		block.setTransaction(unconfirmedBlock.getTransaction());
		block.setPrevHash(tailHash);
		
		String proofOfWork = proofOfWork(block);
		boolean isAppended = this.append(block, proofOfWork);
		
		if(isAppended) {
			log.info("Unconfirmed block appended to blockchain.");
			this.unconfirmedHead = this.unconfirmedHead.getNext();
			// Remove prev node since this is the head node.
			if(this.unconfirmedHead != null) {
				this.unconfirmedHead.setPrev(null);
			}
			
			unconfirmedBlock.setPrev(null);
			unconfirmedBlock.setNext(null);
		}
	}
	
	String proofOfWork(Block block) throws NoSuchAlgorithmException {
		if(!isValidBlock(block)) {
			
		}
		
		String hash = block.computeHash();
		
		while( !hash.startsWith(zeroPreamble(Blockchain.difficulty)) ) {
            block.incrementNounce();
            hash = block.computeHash();
            
           // log.info(String.format("Iteration: %d, Value: %s", block.getNounce(), hash));
		}
        return hash;
	}
	
	private Block createGenesisBlock() {
		Block block = new Block();
		block.setTimestamp(new Date());
		return new Block();
	}
	
	String zeroPreamble(int n) {
		return StringUtils.repeat("0", n);
	}
	
	boolean isValidProof(Block block, String hash) {
		return StringUtils.startsWith(hash, zeroPreamble(this.difficulty)) && 
				StringUtils.equalsIgnoreCase(hash, block.getHash());
	}
	
	private boolean isValidBlock(Block block) {
		return block != null;
	}

}
