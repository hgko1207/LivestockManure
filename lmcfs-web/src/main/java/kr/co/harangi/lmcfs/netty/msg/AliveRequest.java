package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

/**
 * Alive 신호 요청
 * 
 * @author hgko
 *
 */
public class AliveRequest extends AbstractUsnMessage implements UsnOutgoingMessage {

	public AliveRequest(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void encode(ByteBuf buffer) {
		
	}

}
