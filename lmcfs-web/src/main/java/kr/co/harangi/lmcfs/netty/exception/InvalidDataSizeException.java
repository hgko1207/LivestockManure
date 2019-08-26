package kr.co.harangi.lmcfs.netty.exception;

import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageType;

public class InvalidDataSizeException extends RuntimeException {

	public InvalidDataSizeException(UsnMessageType msg, int size) {
		super("Size of " + msg + " is " + msg.getRequireBodySize());
	}
}
