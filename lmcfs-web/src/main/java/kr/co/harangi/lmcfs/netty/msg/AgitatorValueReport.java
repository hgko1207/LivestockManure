package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;

/**
 * 교반기 동작 상태 보고
 * 
 * @author hgko
 *
 */
public class AgitatorValueReport extends AbstractUsnMessage implements UsnIncomingMessage {

	public AgitatorValueReport(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
		
	}

}
