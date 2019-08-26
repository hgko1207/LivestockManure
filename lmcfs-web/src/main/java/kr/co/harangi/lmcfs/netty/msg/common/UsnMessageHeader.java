package kr.co.harangi.lmcfs.netty.msg.common;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * 1. 해더에 포함되는 필드를 선언한다.
 * 2. getRequiredHeaderSize() 와 getRequiredBodySize() 를 구현한다.
 * 3. 해더의 필드에서 계산되는 checksum() 을 구현한다.
 * 
 * @author hgko
 *
 */
@Getter
@Setter 
public class UsnMessageHeader {
	
	public static final int STX = 0x02;
	public static final int ETX = 0x03;
	public static final int MESSAGE_HEADER_LENGTH = 4;

	/** SRC_Addr : 데이터 송신 주소 - 패킷을 전송하는 장치 주소 */
	private int deviceId;
	
	/** DST_Addr : 데이터 수신 주소 - 패킷을 전송하고자 하는 장치 주소 */
	private int serverId;
	
	private int bodyLength;
	
	private int seq;
	
	private UsnMessageType messageType;
	
	public UsnMessageHeader(UsnMessageType messageType) {
		this.messageType = messageType;
	}
	
	public int getRequiredBodySize() {
		return bodyLength;
	}
	
	public int checksum() {
		return 0;
	}
	
	public void encode(ByteBuf buffer) {
	}

	@Override
	public String toString() {
		return "UsnMessageHeader [deviceMacId=" + String.format("%02X", deviceId) + ", serverId=" + String.format("%02X", serverId)
				+ ", bodyLength=" + bodyLength + ", messageType=" + messageType + "]";
	}
}
