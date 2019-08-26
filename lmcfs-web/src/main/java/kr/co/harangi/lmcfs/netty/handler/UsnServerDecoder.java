package kr.co.harangi.lmcfs.netty.handler;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.handler.common.AbstractServerDecoder;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageType;

public class UsnServerDecoder extends AbstractServerDecoder {

	@Override
	public int requireHeaderSize() {
		return UsnMessageHeader.MESSAGE_HEADER_LENGTH;
	}

	@Override
	public UsnMessageHeader makeMessageHeader(ByteBuf buffer) {
		int start = buffer.readUnsignedByte();
		if (start != UsnMessageHeader.STX) {
			throw new UnsupportedOperationException("Invalid STX : " + start);
		}
		
		int srcAddr = buffer.readUnsignedByte();
		int dstAddr = buffer.readUnsignedByte();
		int packetType = buffer.readUnsignedByte();
		int bodyLength = buffer.readUnsignedByte();
		
		UsnMessageHeader header = new UsnMessageHeader(UsnMessageType.valueOf(packetType));
		header.setDeviceId(srcAddr);
		header.setServerId(dstAddr);
		header.setBodyLength(bodyLength);
		header.setSeq(1);
		
		return header;
	}

	@Override
	public void discardBufferByFailHeader(ByteBuf buffer) {
		buffer.readBytes(buffer.readableBytes());
	}

	@Override
	public void discardBufferByFailBody(ByteBuf buffer, UsnMessageHeader header) {
		buffer.readBytes(header.getRequiredBodySize());
	}

	@Override
	public boolean processChecksum(ByteBuf buffer, UsnIncomingMessage incomingMessage) {
		return true;
	}
	
}
