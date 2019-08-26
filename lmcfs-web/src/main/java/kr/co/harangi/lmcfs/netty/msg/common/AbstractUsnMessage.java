package kr.co.harangi.lmcfs.netty.msg.common;

import lombok.Getter;

public abstract class AbstractUsnMessage {

	@Getter
	private UsnMessageHeader header;
	
	public AbstractUsnMessage(UsnMessageHeader header) {
		this.header = header;
	}
	
	public UsnMessageType getMessageType() {
		return header.getMessageType();
	}

	public String getMacId() {
		return String.format("%02X", header.getDeviceId());
	}
	
	/**
	 * MessageHeader 와 MessageBody 의 데이터를 합친 checksum
	 * 
	 * @return
	 */
	public int checksum() {
		return bodyDataSum() + header.checksum();
	}
	
	private int bodyDataSum() {
		return 0;
	}
	
	/**
	 * 필드의 데이터 사이즈가 2 바이트인 경우에 
	 * Checksum 계산을 위해서 상위 바이트와 하위 바이트를 더한다. 
	 * 
	 * @param twoBytesArray
	 * @return
	 */
	protected static int calqTwoBytesChecksum(int ... twoBytesArray) {
		int result = 0;
		for (int twoBytes : twoBytesArray) {
			result += ((twoBytes >> 8) & 0xFF) + (twoBytes & 0xFF);
		}
		return result;
	}
	
	public String toString() {
		return header.toString();
	}
}
