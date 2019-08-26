package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

/**
 * 센서 값 요청
 * 
 * @author hgko
 *
 */
public class SensorValueRequest extends AbstractUsnMessage implements UsnOutgoingMessage {

	public SensorValueRequest(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void encode(ByteBuf buffer) {
		buffer.writeByte(getHeader().getSeq());
	}

}
