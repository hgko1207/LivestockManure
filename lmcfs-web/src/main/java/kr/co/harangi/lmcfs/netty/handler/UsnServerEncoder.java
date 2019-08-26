package kr.co.harangi.lmcfs.netty.handler;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.handler.common.AbstractServerEncoder;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

public class UsnServerEncoder extends AbstractServerEncoder {

	@Override
	public void encode(UsnOutgoingMessage message, ByteBuf out) {
		UsnMessageHeader header = (UsnMessageHeader) message.getHeader();
		
		out.writeByte(UsnMessageHeader.STX);
		out.writeByte(header.getServerId());
		out.writeByte(header.getDeviceId());
		out.writeByte(header.getMessageType().getId());
		out.writeByte(header.getMessageType().getRequireBodySize());
		
		message.encode(out);
		
		out.writeByte(UsnMessageHeader.ETX);
	}
}
