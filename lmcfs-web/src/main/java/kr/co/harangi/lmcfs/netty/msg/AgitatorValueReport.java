package kr.co.harangi.lmcfs.netty.msg;

import io.netty.buffer.ByteBuf;
import kr.co.harangi.lmcfs.netty.msg.common.AbstractUsnMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 교반기 동작 상태 보고
 * 
 * @author hgko
 *
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AgitatorValueReport extends AbstractUsnMessage implements UsnIncomingMessage {
	
	private int seq;
	
	private int status;

	public AgitatorValueReport(UsnMessageHeader header) {
		super(header);
	}

	@Override
	public void decode(ByteBuf buffer) {
		seq = buffer.readUnsignedByte();
		status = buffer.readUnsignedByte();
	}

}
