package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

/**
 * BroadCasting 방식으로 요청
 * 
 * @author hgko
 *
 */
public class SensorSamplingStart extends AbstractUsnMessage implements UsnOutgoingMessage {

	public SensorSamplingStart(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void encode(ByteBuf buffer) {
		
	}

}
