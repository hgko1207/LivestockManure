package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.SensorLog;
import kr.co.harangi.lmcfs.repository.SensorLogRepository;
import kr.co.harangi.lmcfs.service.SensorLogService;

@Transactional
@Service
public class SensorLogServiceImpl implements SensorLogService {
	
	@Autowired
	private SensorLogRepository sensorLogRepository;

	@Override
	public SensorLog get(Integer id) {
		return sensorLogRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<SensorLog> getList() {
		return sensorLogRepository.findAll();
	}

	@Override
	public boolean regist(SensorLog domain) {
		if (isNew(domain)) {
			return sensorLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(SensorLog domain) {
		if (!isNew(domain)) {
			return sensorLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		sensorLogRepository.deleteById(id);
		return true;
	}

	private boolean isNew(SensorLog domain) {
		return !sensorLogRepository.existsById(domain.getId());
	}
}
