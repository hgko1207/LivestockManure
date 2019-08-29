package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 온도 (pt100) 센서 값 응답
 * 
 * @author hgko
 *
 */
@Setter
@Getter
@ToString(callSuper = true)
public class TempValueResponse extends AbstractUsnMessage implements UsnIncomingMessage {
	
	private int seq;
	
	/** 온도 */
	private float temp;
	
	public TempValueResponse(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
		seq = buffer.readUnsignedByte();
		temp = buffer.readUnsignedShort();
	}

}
