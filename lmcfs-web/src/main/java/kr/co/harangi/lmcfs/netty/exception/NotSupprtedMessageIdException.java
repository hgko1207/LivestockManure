package kr.co.harangi.lmcfs.netty.exception;

public class NotSupprtedMessageIdException extends RuntimeException {
	
	public NotSupprtedMessageIdException(int messageId) {
		super(messageId + " is invalid.");
	}
}
