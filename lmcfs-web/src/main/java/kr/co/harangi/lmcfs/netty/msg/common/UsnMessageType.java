package kr.co.harangi.lmcfs.netty.msg.common;

import kr.co.harangi.lmcfs.netty.exception.NotSupprtedMessageIdException;
import kr.co.harangi.lmcfs.netty.msg.AgitatorValueReport;
import kr.co.harangi.lmcfs.netty.msg.AliveResponse;
import kr.co.harangi.lmcfs.netty.msg.BlowerValueReport;
import kr.co.harangi.lmcfs.netty.msg.GasValueResponse;
import kr.co.harangi.lmcfs.netty.msg.TempValueResponse;
import lombok.Getter;

@Getter
public enum UsnMessageType {
	ALIVE_REQUEST(0X10, 0),
	ALIVE_RESPONSE(0X20, AliveResponse.class),
	SENSOR_VALUE_REQUEST(0X11, 1),
	TEMP_VALUE_RESPONSE(0X21, TempValueResponse.class),
	GAS_VALUE_RESPONSE(0X22, GasValueResponse.class),
	AGITATOR_CONTROL_REQUEST(0X30, 3),
	AGITATOR_VALUE_REPORT(0X31, AgitatorValueReport.class),
	BLOWER_CONTROL_REQUEST(0X32, 9),
	BLOWER_VALUE_REPORT(0X33, BlowerValueReport.class),
	SENSOR_SAMPLING_START(0Xff, 0),
	ALL_SENSOR_VALUE_REQUEST(0Xff, 0);

	private Class<? extends UsnIncomingMessage> incomingClass;
	
	private int id;
	
	private int requireBodySize;
	
	private UsnMessageType(int id, int size) {
		this(id, size, null);
	}
	
	private UsnMessageType(int id, Class<? extends UsnIncomingMessage> incomingClass) {
		this(id, 0, incomingClass);
	}
	
	private UsnMessageType(int id, int size, Class<? extends UsnIncomingMessage> incomingClass) {
		this.id = id;
		this.requireBodySize = size;
		this.incomingClass = incomingClass;
	}
	
	public static UsnMessageType valueOf(int id) throws NotSupprtedMessageIdException {
		for (UsnMessageType msg : UsnMessageType.values()) {
			if (msg.id == id && msg.incomingClass != null) {
				return msg;
			}
		}
		throw new NotSupprtedMessageIdException(id);
	}
	
	@Override
	public String toString() {
		return name() + String.format("{ id:%02X, size:%d, class:%s }", id, 
				requireBodySize, (incomingClass == null ? null : incomingClass.getSimpleName()));
	}
}
