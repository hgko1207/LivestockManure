package kr.co.harangi.lmcfs.netty.msg.common;

import io.netty.buffer.ByteBuf;

public interface UsnOutgoingMessage {

	void encode(ByteBuf buffer);

	int checksum();
	
	UsnMessageType getMessageType();
	
	UsnMessageHeader getHeader();
}
