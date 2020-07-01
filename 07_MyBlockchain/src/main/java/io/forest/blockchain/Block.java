package io.forest.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block {

	private Block next;

	private Block prev;

	private int index;

	private String transaction;

	private Date timestamp;

	private String prevHash;

	private int nounce;

	private String hash;

	public void incrementNounce() {
		this.nounce += 1;
	}

	public Block getNext() {
		return next;
	}

	public Block getPrev() {
		return prev;
	}

	public int getIndex() {
		return index;
	}

	public String getTransaction() {
		return transaction;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getPrevHash() {
		return prevHash;
	}

	public int getNounce() {
		return nounce;
	}

	public String getHash() {
		return hash;
	}

	public void setNext(Block next) {
		this.next = next;
	}

	public void setPrev(Block prev) {
		this.prev = prev;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}

	public void setNounce(int nounce) {
		this.nounce = nounce;
	}
	
	public String computeHash() throws NoSuchAlgorithmException {
		// TODO: Treat hash as immutable value.
		this.hash =  bytesToHex(hash(this.toString()));
		return this.hash;
	}
	

	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("transaction: ").append(this.transaction).append("\n");
		strBuf.append("  timestamp: ").append(this.timestamp).append("\n");
		strBuf.append("   prevHash: ").append(this.prevHash).append("\n");
		strBuf.append("     nounce: ").append(this.nounce).append("\n");
		strBuf.append("       hash: ").append(this.hash).append("\n");
		
		return strBuf.toString();
	}

	private byte[] hash(String originalString) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return encodedhash;
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

}
