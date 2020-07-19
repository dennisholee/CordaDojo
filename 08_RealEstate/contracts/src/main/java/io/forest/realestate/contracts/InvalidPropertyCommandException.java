package io.forest.realestate.contracts;

public class InvalidPropertyCommandException extends RuntimeException {

	private static final long serialVersionUID = -8100068738091923419L;


	public InvalidPropertyCommandException(String message) {
		super(message);
	}
}
