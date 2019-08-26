package kr.co.harangi.lmcfs.service.usn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.netty.annotation.Usn;
import kr.co.harangi.lmcfs.netty.group.UsnMessageSenderGroup;
import kr.co.harangi.lmcfs.netty.listener.MessageListener;
import kr.co.harangi.lmcfs.netty.msg.AgitatorValueReport;
import kr.co.harangi.lmcfs.netty.msg.AliveResponse;
import kr.co.harangi.lmcfs.netty.msg.BlowerValueReport;
import kr.co.harangi.lmcfs.netty.msg.TempValueResponse;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHelper;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;
import lombok.extern.slf4j.Slf4j;

@Usn
@Slf4j
@Service
@Transactional
public class UsnMessageProcessor implements MessageListener {
	
	private static final int SENSOR_VALUE_TIME_MILLISECONDS = 60 * 1000;
	
	@Autowired
	private UsnMessageSenderGroup messageSenderGroup;

	@Override
	public void connectionStateChanged(boolean isConnected) {
	}

	@Override
	public void messageReceived(UsnIncomingMessage in) {
		switch (in.getMessageType()) {
		
		case ALIVE_RESPONSE:
			processAliveResponse(in);
			break;
			
		case TEMP_VALUE_RESPONSE:
			processTempSensorValueResponse(in);
			break;
			
		case GAS_VALUE_RESPONSE:
			processGasSensorValueResponse(in);
			break;
			
		case AGITATOR_VALUE_REPORT:
			processAgitatorValueReport(in);
			break;
			
		case BLOWER_VALUE_REPORT:
			processBlowerValueReport(in);
			break;
			
		default:
			break;
		}
	}

	private void processAliveResponse(UsnIncomingMessage in) {
		AliveResponse response = (AliveResponse) in;
		
		String macId = response.getMacId();
		
		log.debug("AliveResponse -> MacId : {}", macId);
	}

	private void processTempSensorValueResponse(UsnIncomingMessage in) {
		TempValueResponse response = (TempValueResponse) in;
		String macId = response.getMacId();
		
		System.err.println(response);
		
		log.debug("TempSensorValueResponse -> MacId : {}", macId);
	}
	
	private void processGasSensorValueResponse(UsnIncomingMessage in) {
		TempValueResponse response = (TempValueResponse) in;
		String macId = response.getMacId();
		
		System.err.println(response);
		
		log.debug("TempSensorValueResponse -> MacId : {}", macId);
	}

	private void processAgitatorValueReport(UsnIncomingMessage in) {
		AgitatorValueReport report = (AgitatorValueReport) in;
		
		String macId = report.getMacId();
		
		log.debug("AgitatorValueReport -> MacId : {}", macId);
	}
	
	private void processBlowerValueReport(UsnIncomingMessage in) {
		BlowerValueReport report = (BlowerValueReport) in;
		
		String macId = report.getMacId();
		
		log.debug("BlowerValueReport -> MacId : {}", macId);
	}
	
	public void aliveRequest() {
		
	}
	
	//@Scheduled(fixedDelay = SENSOR_VALUE_TIME_MILLISECONDS, initialDelay = SENSOR_VALUE_TIME_MILLISECONDS)
	public void sensorValueRequest() {
		log.debug("Sensor Value Request");
		
		String macId = "30";
		
		UsnOutgoingMessage out = UsnMessageHelper.makeSensorValueRequest(macId);
		messageSenderGroup.writeAsync(macId, out);
	}
	
	public void agitatorControlRequest() {
		log.debug("Agitator Control Request");
		
		String macId = "30";
		int onControl = 0x01;
		int offControl = 0x00;
		
		UsnOutgoingMessage out = UsnMessageHelper.makeAgitatorControlRequest(macId, onControl, offControl);
		messageSenderGroup.writeAsync(macId, out);
	}
}
