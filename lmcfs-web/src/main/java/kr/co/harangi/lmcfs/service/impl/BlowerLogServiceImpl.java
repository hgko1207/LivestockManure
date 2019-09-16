package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.BlowerLog;
import kr.co.harangi.lmcfs.repository.BlowerLogRepository;
import kr.co.harangi.lmcfs.service.BlowerLogService;

@Transactional
@Service
public class BlowerLogServiceImpl implements BlowerLogService {
	
	@Autowired
	private BlowerLogRepository blowerLogRepository;

	@Override
	public BlowerLog get(Integer id) {
		return blowerLogRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<BlowerLog> getList() {
		return blowerLogRepository.findAll();
	}

	@Override
	public boolean regist(BlowerLog domain) {
		if (isNew(domain)) {
			return blowerLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(BlowerLog domain) {
		if (!isNew(domain)) {
			return blowerLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		blowerLogRepository.deleteById(id);
		return true;
	}

	private boolean isNew(BlowerLog domain) {
		return !blowerLogRepository.existsById(domain.getId());
	}

}
