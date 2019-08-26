package kr.co.harangi.lmcfs.utils;

import io.netty.buffer.ByteBuf;

public class PrintUtil {

	public static String printReceivedChannelBuffer(String msg, ByteBuf buffer) {
		StringBuilder str = new StringBuilder();
		str.append(msg + " : ");
		int startIndex = buffer.readerIndex();
		for (int i = 0; i < buffer.readableBytes(); i++) {
			str.append(String.format("%02X ", buffer.getUnsignedByte(startIndex + i)));
		}
		
		return str.toString();
	}
}
