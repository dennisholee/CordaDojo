package io.forest.blockchain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.Test;

class BlockchainTest {

	@Test
	void testProofOfWork_withDefaultBlock_expectSuccess() throws NoSuchAlgorithmException {
		Blockchain blockchain = new Blockchain();
		Block block = new Block();
		String proofOfWork = blockchain.proofOfWork(block);
		
		String preamble = String.join("", Collections.nCopies(blockchain.getDifficulty(), "0"));
		
		assertTrue(proofOfWork.startsWith(preamble), "Proof of work does not statisfy preamble.");
	}

	@Test
	void testAppendUnconfirmed_withTenTxn_expectSuccess() {
		Blockchain blockchain = new Blockchain();
		
		Block block = null;
		for(int i = 0; i <= 10; i++) {
			block = new Block();
			block.setTransaction(String.format("txn: %d", i));
			blockchain.appendUnconfirmed(block);
		}
		
		for(Block unconfirmedBlock = blockchain.getUnconfirmedHead(); 
			unconfirmedBlock !=null;  
			unconfirmedBlock = unconfirmedBlock.getNext()) {
		}
	}
	
	@Test
	void testMine_withTenUnconfirmedBlock_expectSuccess() throws NoSuchAlgorithmException {
		Blockchain blockchain = new Blockchain();
		Block block = null;
		for(int i = 0; i < 10; i++) {
			block = new Block();
			block.setTimestamp(new Date());
			block.setTransaction(String.format("txn: %d", i));
			blockchain.appendUnconfirmed(block);
		}
		
		Block unconfirmedBlock = null;
		while(blockchain.getUnconfirmedHead() != null) {
			unconfirmedBlock = blockchain.getUnconfirmedHead();
			blockchain.mine();
		}
	
		int counter = 0;
		Block genesisBlock = blockchain.getHead();
		for(Block blockItem = genesisBlock.getNext(); blockItem !=null; blockItem = blockItem.getNext()) {
			assertEquals(String.format("txn: %d", counter), blockItem.getTransaction());
			
			counter ++;
		}
	}
	
	@Test
	void testZeroPreamble_withNoRepeat_expectEmpty() {
		Blockchain blockchain = new Blockchain();
		assertEquals("", blockchain.zeroPreamble(0));
	}
	
	@Test
	void testZeroPreamble_withAtLeaseOneRepeat_expectSuccess() {
		Blockchain blockchain = new Blockchain();
		assertEquals("0", blockchain.zeroPreamble(1));
		assertEquals("00", blockchain.zeroPreamble(2));
		assertEquals("0000000000", blockchain.zeroPreamble(10));
	}
	
	@Test
	void testZeroPreamble_withNegativeRepeat_expectEmpty() {
		Blockchain blockchain = new Blockchain();
		assertEquals("", blockchain.zeroPreamble(-1));
	}
}
