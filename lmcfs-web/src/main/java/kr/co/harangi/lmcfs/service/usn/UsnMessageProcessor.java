package kr.co.harangi.lmcfs.service.usn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.SensorLog;
import kr.co.harangi.lmcfs.domain.db.SensorNode;
import kr.co.harangi.lmcfs.netty.annotation.Usn;
import kr.co.harangi.lmcfs.netty.group.UsnMessageSenderGroup;
import kr.co.harangi.lmcfs.netty.listener.MessageListener;
import kr.co.harangi.lmcfs.netty.msg.AgitatorValueReport;
import kr.co.harangi.lmcfs.netty.msg.AliveResponse;
import kr.co.harangi.lmcfs.netty.msg.BlowerValueReport;
import kr.co.harangi.lmcfs.netty.msg.GasValueResponse;
import kr.co.harangi.lmcfs.netty.msg.TempValueResponse;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHelper;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;
import kr.co.harangi.lmcfs.service.SensorLogService;
import kr.co.harangi.lmcfs.service.SensorNodeService;
import lombok.extern.slf4j.Slf4j;

@Usn
@Slf4j
@Service
@Transactional
public class UsnMessageProcessor implements MessageListener {
	
	private static final int ALIVE_TIME_MILLISECONDS = 30 * 1000;
	private static final int SENSOR_VALUE_TIME_MILLISECONDS = 60 * 1000;
	private static final int DELAY_TIME_MILLISECONDS = 2 * 1000;
	
	@Autowired
	private UsnMessageSenderGroup messageSenderGroup;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private SensorNodeService sensorNodeService;
	
	@Autowired
	private SensorLogService sensorLogService;
	
	private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
	
	@Autowired
	private TaskScheduler taskScheduler;

	@Override
	public void connectionStateChanged(boolean isConnected) {
		log.info("isConnected : " + isConnected);
		
		if (isConnected) {
			log.info("Sensor Value Request");
			
			ScheduledFuture<?> task = taskScheduler.scheduleAtFixedRate(() -> {
				sensorNodeService.getList().forEach(data -> {
					try {
						Thread.sleep(DELAY_TIME_MILLISECONDS);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					String macId = data.getMacId();
					log.info("Sensor Value Request : " + macId);
					UsnOutgoingMessage out = UsnMessageHelper.makeSensorValueRequest(macId);
					messageSenderGroup.writeAsync(macId, out);
				});
				
			}, SENSOR_VALUE_TIME_MILLISECONDS); 
			scheduledTasks.put("sensorValueScheduler", task);
		} else {
			scheduledTasks.get("sensorValueScheduler").cancel(true);
		}
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
		SensorNode sensorNode = sensorNodeService.get(macId);
		sensorNode.setActive();
		
		log.info("AliveResponse -> MacId : {}", macId);
	}

	private void processTempSensorValueResponse(UsnIncomingMessage in) {
		TempValueResponse response = (TempValueResponse) in;
		String macId = response.getMacId();
		
		log.info("TempSensorValueResponse -> MacId : {}", macId);
		
		SensorNode sensorNode = deviceService.updateTempSensorNodeValue(macId, response);
		if (sensorNode != null) {
			SensorLog sensorLog = new SensorLog();
			sensorLog.setMacId(macId);
			sensorLog.setSensorType(sensorNode.getSensorType());
			sensorLog.setTemp(sensorNode.getTemp());
			
			sensorLogService.regist(sensorLog);
		}
	}
	
	private void processGasSensorValueResponse(UsnIncomingMessage in) {
		GasValueResponse response = (GasValueResponse) in;
		String macId = response.getMacId();
		
		log.info("GasSensorValueResponse -> MacId : {}", macId);
		
		SensorNode sensorNode = deviceService.updateGasSensorNodeValue(macId, response);
		if (sensorNode != null) {
			SensorLog sensorLog = new SensorLog();
			sensorLog.setMacId(macId);
			sensorLog.setSensorType(sensorNode.getSensorType());
			sensorLog.setTemp(sensorNode.getTemp());
			sensorLog.setHum(sensorNode.getHum());
			sensorLog.setNh3(sensorNode.getNh3());
			sensorLog.setH2s(sensorNode.getH2s());
			sensorLog.setCo2(sensorNode.getCo2());
			sensorLog.setO2(sensorNode.getO2());
			
			sensorLogService.regist(sensorLog);
		}
	}

	private void processAgitatorValueReport(UsnIncomingMessage in) {
		AgitatorValueReport report = (AgitatorValueReport) in;
		
		String macId = report.getMacId();
		
		log.info("AgitatorValueReport -> MacId : {}", macId);
	}
	
	private void processBlowerValueReport(UsnIncomingMessage in) {
		BlowerValueReport report = (BlowerValueReport) in;
		
		String macId = report.getMacId();
		
		log.info("BlowerValueReport -> MacId : {}", macId);
	}
	
	//@Scheduled(fixedDelay = ALIVE_TIME_MILLISECONDS, initialDelay = ALIVE_TIME_MILLISECONDS)
	public void aliveRequest() {
		log.info("Alive Request");
		
		sensorNodeService.getList().forEach(data -> {
			String macId = data.getMacId();
			UsnOutgoingMessage out = UsnMessageHelper.makeAliveRequest(macId);
			messageSenderGroup.writeAsync(macId, out);
		});
	}
	
	public void agitatorControlRequest() {
		log.info("Agitator Control Request");
		
		String macId = "30";
		int onControl = 0x01;
		int offControl = 0x00;
		
		UsnOutgoingMessage out = UsnMessageHelper.makeAgitatorControlRequest(macId, onControl, offControl);
		messageSenderGroup.writeAsync(macId, out);
	}
}
