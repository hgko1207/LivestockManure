package kr.co.harangi.lmcfs.netty.msg.common;

import java.util.concurrent.atomic.AtomicInteger;

import kr.co.harangi.lmcfs.netty.msg.AgitatorControlRequest;
import kr.co.harangi.lmcfs.netty.msg.AliveRequest;
import kr.co.harangi.lmcfs.netty.msg.SensorValueRequest;

/**
 * 송신 메시지를 만들어 주는 헬퍼 클래스
 * 
 * @author hgko
 *
 */
public class UsnMessageHelper {

	private static AtomicInteger seq = new AtomicInteger();
	
	private static UsnMessageHeader makeHeader(UsnMessageType type, String macId) {
		int deviceId = Integer.parseInt(macId, 16);

		UsnMessageHeader header = new UsnMessageHeader(type);
		header.setDeviceId(deviceId);
		header.setServerId(0x10);
		header.setSeq(seq.getAndIncrement());
		return header;
	}
	
	public static UsnOutgoingMessage makeAliveRequest(String macId) {
		UsnMessageHeader header = UsnMessageHelper.makeHeader(UsnMessageType.ALIVE_REQUEST, macId);
		return new AliveRequest(header);
	}
	
	public static UsnOutgoingMessage makeSensorValueRequest(String macId) {
		UsnMessageHeader header = UsnMessageHelper.makeHeader(UsnMessageType.SENSOR_VALUE_REQUEST, macId);
		header.setBodyLength(1);
		return new SensorValueRequest(header);
	}
	
	public static UsnOutgoingMessage makeAgitatorControlRequest(String macId, int onControl, int offControl) {
		UsnMessageHeader header = UsnMessageHelper.makeHeader(UsnMessageType.AGITATOR_CONTROL_REQUEST, macId);
		header.setBodyLength(3);
		return new AgitatorControlRequest(header, onControl, offControl);
	}
}
