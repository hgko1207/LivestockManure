package kr.co.harangi.lmcfs.service;

import java.time.LocalDateTime;

import kr.co.harangi.lmcfs.domain.db.SensorLog;

public interface SensorLogService extends CRUDService<SensorLog, Integer> {
	
	SensorLog get(String macId, LocalDateTime createDate);
}
