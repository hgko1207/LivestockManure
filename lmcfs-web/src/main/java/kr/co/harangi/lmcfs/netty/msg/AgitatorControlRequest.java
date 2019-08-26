package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;

/**
 * 교반기 제어 요청
 * 
 * @author hgko
 *
 */
public class AgitatorControlRequest extends AbstractUsnMessage implements UsnOutgoingMessage {
	
	private int onControl;
	
	private int offControl;

	public AgitatorControlRequest(UsnMessageHeader header, int onControl, int offControl) {
		super(header);
		this.onControl = onControl;
		this.offControl = offControl;
	}

	@Override
	public void encode(ByteBuf buffer) {
		buffer.writeByte(getHeader().getSeq());
		buffer.writeByte(onControl);
		buffer.writeByte(offControl);
	}

}
