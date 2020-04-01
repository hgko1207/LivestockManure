package kr.co.harangi.lmcfs.service.usn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import kr.co.harangi.lmcfs.domain.db.Agitator;
import kr.co.harangi.lmcfs.domain.db.Blower;
import kr.co.harangi.lmcfs.domain.db.SensorNode;
import kr.co.harangi.lmcfs.netty.msg.AgitatorValueReport;
import kr.co.harangi.lmcfs.netty.msg.BlowerValueReport;
import kr.co.harangi.lmcfs.netty.msg.GasValueResponse;
import kr.co.harangi.lmcfs.netty.msg.TempValueResponse;
import kr.co.harangi.lmcfs.service.AgitatorService;
import kr.co.harangi.lmcfs.service.BlowerService;
import kr.co.harangi.lmcfs.service.SensorNodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeviceService {
	
	private static final int ALIVE_CHECK_TIME_MILLISECONDS = 60 * 60 * 1000;
	
	@Autowired
	private SensorNodeService sensorNodeService;
	
	@Autowired
	private AgitatorService agitatorService;
	
	@Autowired
	private BlowerService blowerService;
	
	@Autowired
	private TransactionTemplate txTemplate;
	
	@Scheduled(fixedDelay = ALIVE_CHECK_TIME_MILLISECONDS, initialDelay = ALIVE_CHECK_TIME_MILLISECONDS)
	public void deviceAliveCheck() {
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				log.debug("alive checker!!!");
				
				for (SensorNode sensorNode : sensorNodeService.getList(true)) {
					sensorNode.setInactiveIfTimeout();
				}
				
				for (Agitator agitator : agitatorService.getList(true)) {
					agitator.setInactiveIfTimeout();
				}
				
				for (Blower blower : blowerService.getList(true)) {
					blower.setInactiveIfTimeout();
				}
			}
		});
	}
	
	/**
	 * 센서노드의 동작 상태를 활성화로 변경한다.
	 * @param macId
	 */
	public void updateAliveSensorNode(String macId) {
		SensorNode sensorNode = sensorNodeService.get(macId);
		if (sensorNode == null) {
			log.warn("sensorNode {} not found", macId);
			return;
		}
		
		if (sensorNode.setActive()) {
			log.info("sensorNode {} is active", macId);
		}
	}

	/**
	 * 온도 센서노드의 동작 상태를 활성화로 변경하고, 값을 변경한다.
	 * @param macId
	 */
	public SensorNode updateTempSensorNodeValue(String macId, TempValueResponse result) {
		SensorNode sensorNode = sensorNodeService.get(macId);
		if (sensorNode == null) {
			log.warn("sensorNode {} not found", macId);
			return null;
		}
		
		sensorNode.setTemp(result.getTemp());
		
		if (sensorNode.setActive()) {
			log.info("sensorNode {} is active", macId);
		}
		
		return sensorNode;
	}
	
	/**
	 * 가스 센서노드의 동작 상태를 활성화로 변경하고, 값을 변경한다.
	 * @param macId
	 */
	public SensorNode updateGasSensorNodeValue(String macId, GasValueResponse result) {
		SensorNode sensorNode = sensorNodeService.get(macId);
		if (sensorNode == null) {
			log.warn("sensorNode {} not found", macId);
			return null;
		}
		
		sensorNode.setCo2(result.getCo2());
		sensorNode.setTemp(result.getTemp() / 100);
		sensorNode.setHum(result.getHum());
		sensorNode.setOriginalO2(result.getO2());
		sensorNode.setOriginalNh3(result.getNh3());
		sensorNode.setOriginalH2s(result.getH2s());
		
		float o2 = result.getO2() / 100;
		o2 = o2 - 0.176f;
		if (o2 <= 0) {
			o2 = 0;
		} else {
			o2 = (20.9f - o2) * 193.5186f;
		}
		sensorNode.setO2(round(o2));
		sensorNode.setNh3(round(nh3AndH2s(result.getNh3())));
		sensorNode.setH2s(round(nh3AndH2s(result.getH2s())));
		
		if (sensorNode.setActive()) {
			log.info("sensorNode {} is active", macId);
		}
		
		return sensorNode;
	}
	
	private float nh3AndH2s(float value) {
		float result = value / 100;
		result = result - 0.5f;
		if (result <= 0) {
			result = 0;
		} else {
			result = result / 0.012f;
		}
		
		return result;
	}
	
	private float round(float value) {
		return Math.round(value * 1000) / 1000.0f;
	}
	
	public Agitator updateAgitatorValue(String macId, AgitatorValueReport report) {
		Agitator agitator = agitatorService.get(macId);
		if (agitator == null) {
			log.warn("agitator {} not found", macId);
			return null;
		}
		
		agitator.setStatus(report.getStatus() == 0? false : true);
		
		if (agitator.setActive()) {
			log.info("agitator {} is active", macId);
		}
		
		return agitator;
	}
	
	public Blower updateBlowerValue(String macId, BlowerValueReport report) {
		Blower blower = blowerService.get(macId);
		if (blower == null) {
			log.warn("blower {} not found", macId);
			return null;
		}
		
		blower.setBtn1Status(report.getBtn1Status() == 0? false : true);
		blower.setBtn2Status(report.getBtn2Status() == 0? false : true);
		blower.setBtn3Status(report.getBtn3Status() == 0? false : true);
		blower.setBtn4Status(report.getBtn4Status() == 0? false : true);
		
		if (blower.setActive()) {
			log.info("blower {} is active", macId);
		}
		
		return blower;
	}
}
