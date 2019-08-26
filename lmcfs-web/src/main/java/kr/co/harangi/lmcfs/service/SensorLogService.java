package kr.co.harangi.lmcfs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.harangi.lmcfs.domain.db.SensorLog;
import kr.co.harangi.lmcfs.repository.SensorLogRepository;

@Service
public class SensorLogService {
	
	@Autowired
	private SensorLogRepository sensorLogRepository;

	public boolean regist(SensorLog domain) {
		return sensorLogRepository.save(domain) != null;
	}
}
