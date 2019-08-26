package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;

/**
 * Alive 신호 응답
 * 
 * @author hgko
 *
 */
public class AliveResponse extends AbstractUsnMessage implements UsnIncomingMessage {

	public AliveResponse(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
	}
}
