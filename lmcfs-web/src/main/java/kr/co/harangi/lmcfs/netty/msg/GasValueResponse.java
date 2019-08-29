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
public class GasValueResponse extends AbstractUsnMessage implements UsnIncomingMessage {
	
	private int seq;
	
	/** 온도 */
	private float temp;
	
	/** 습도 */
	private float hum;
	
	private float nh3;
	
	private float h2s;
	
	private float co2;
	
	private float o2;
	
	public GasValueResponse(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
		seq = buffer.readUnsignedByte();
		nh3 = buffer.readUnsignedByte();
		h2s = buffer.readUnsignedByte();
		co2 = buffer.readUnsignedShort();
		o2 = buffer.readUnsignedByte();
		temp = buffer.readUnsignedShort();
		hum = buffer.readUnsignedByte();
	}

}
