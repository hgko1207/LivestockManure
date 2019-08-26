package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

/**
 * 브러워 제어 요청
 * 
 * @author hgko
 *
 */
public class BlowerControlRequest extends AbstractUsnMessage implements UsnOutgoingMessage {

	public BlowerControlRequest(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void encode(ByteBuf buffer) {
		
	}

}
