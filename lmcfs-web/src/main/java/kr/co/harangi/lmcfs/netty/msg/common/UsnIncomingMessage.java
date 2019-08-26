package kr.co.harangi.lmcfs.netty.msg.common;

import io.netty.buffer.ByteBuf;

public interface UsnIncomingMessage {

	UsnMessageHeader getHeader();
	
	UsnMessageType getMessageType();

	void decode(ByteBuf buffer);

	int checksum();
}
