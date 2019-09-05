package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.AgitatorLog;
import kr.co.harangi.lmcfs.repository.AgitatorLogRepository;
import kr.co.harangi.lmcfs.service.AgitatorLogService;

@Transactional
@Service
public class AgitatorLogServiceImpl implements AgitatorLogService {
	
	@Autowired
	private AgitatorLogRepository agitatorLogRepository;

	@Override
	public AgitatorLog get(Integer id) {
		return agitatorLogRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<AgitatorLog> getList() {
		return agitatorLogRepository.findAll();
	}

	@Override
	public boolean regist(AgitatorLog domain) {
		if (isNew(domain)) {
			return agitatorLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(AgitatorLog domain) {
		if (!isNew(domain)) {
			return agitatorLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		agitatorLogRepository.deleteById(id);
		return true;
	}

	private boolean isNew(AgitatorLog domain) {
		return !agitatorLogRepository.existsById(domain.getId());
	}

}
