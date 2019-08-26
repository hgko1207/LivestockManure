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
import kr.co.harangi.lmcfs.service.AgitatorService;
import kr.co.harangi.lmcfs.service.BlowerService;
import kr.co.harangi.lmcfs.service.SensorNodeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeviceAliveChecker {
	
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
	 * 센서노드의 동작 상태를 활성화로 변경하고, 값을 변경한다.
	 * @param macId
	 */
	public boolean updateSensorNodeValue(String macId) {
		SensorNode sensorNode = sensorNodeService.get(macId);
		if (sensorNode == null) {
			log.warn("sensorNode {} not found", macId);
			return false;
		}
		
		if (sensorNode.setActive()) {
			log.info("sensorNode {} is active", macId);
		}
		
		return true;
	}
}
