package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;

/**
 * 브로워 동작 상태 보고
 * 
 * @author inspace
 *
 */
public class BlowerValueReport extends AbstractUsnMessage implements UsnIncomingMessage {

	public BlowerValueReport(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
		
	}

}
