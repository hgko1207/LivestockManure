package kr.co.harangi.lmcfs.repository;

import java.time.LocalDateTime;

import kr.co.harangi.lmcfs.domain.db.SensorLog;

public interface SensorLogRepository extends DefaultRepository<SensorLog, Integer> {

	SensorLog findByMacIdAndCreateDate(String macId, LocalDateTime createDate);

}
