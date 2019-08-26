package kr.co.harangi.lmcfs.netty.handler.common;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;
import kr.co.harangi.lmcfs.utils.PrintUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractServerEncoder extends MessageToByteEncoder<UsnOutgoingMessage> {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void encode(ChannelHandlerContext ctx, UsnOutgoingMessage msg, ByteBuf out) throws Exception {
		log.info("[Outgoing] {} => {}", ctx.channel(), msg);
		encode(msg, out.order(ByteOrder.LITTLE_ENDIAN));
		
		log.debug("{} {}", ctx.channel(), PrintUtil.printReceivedChannelBuffer("out", out));
	}

	/**
	 * 1. 데이터 전송에 필요한 ChannelBuffer를 생성한다.
	 * 2. 생성한 buffer에 데이터를 담고, 반환한다.
	 * 
	 * @param message
	 * @param out 
	 * @return
	 */
	public abstract void encode(UsnOutgoingMessage message, ByteBuf out);

}