package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 브로워 동작 상태 보고
 * 
 * @author inspace
 *
 */
@Setter
@Getter
@ToString(callSuper = true)
public class BlowerValueReport extends AbstractUsnMessage implements UsnIncomingMessage {

	private int seq;
	
	private int btn1Status;
	
	private int btn2Status;
	
	private int btn3Status;
	
	private int btn4Status;
	
	public BlowerValueReport(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
		seq = buffer.readUnsignedByte();
		btn1Status = buffer.readUnsignedByte();
		btn2Status = buffer.readUnsignedByte();
		btn3Status = buffer.readUnsignedByte();
		btn4Status = buffer.readUnsignedByte();
	}

}
