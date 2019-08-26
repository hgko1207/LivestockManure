package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.SensorNode;
import kr.co.harangi.lmcfs.repository.SensorNodeRepository;
import kr.co.harangi.lmcfs.service.SensorNodeService;

@Transactional
@Service
public class SensorNodeServiceImpl implements SensorNodeService {
	
	@Autowired
	private SensorNodeRepository sensorRepository;

	@Override
	public SensorNode get(Integer id) {
		return sensorRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<SensorNode> getList() {
		return sensorRepository.findAll();
	}

	@Override
	public boolean regist(SensorNode domain) {
		if (isNew(domain)) {
			return sensorRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(SensorNode domain) {
		if (!isNew(domain)) {
			return sensorRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		sensorRepository.deleteById(id);
		return true;
	}

	private boolean isNew(SensorNode domain) {
		return !sensorRepository.existsById(domain.getId());
	}

	@Override
	public SensorNode get(String macId) {
		return sensorRepository.findByMacId(macId);
	}

	@Override
	public List<SensorNode> getList(boolean alive) {
		return sensorRepository.findByAlive(alive);
	}
}
