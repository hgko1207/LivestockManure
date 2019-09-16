package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.GatewayLog;
import kr.co.harangi.lmcfs.repository.GatewayLogRepository;
import kr.co.harangi.lmcfs.service.GatewayLogService;

@Transactional
@Service
public class GatewayLogServiceImpl implements GatewayLogService {
	
	@Autowired
	private GatewayLogRepository gatewayLogRepository;

	@Override
	public GatewayLog get(Integer id) {
		return gatewayLogRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GatewayLog> getList() {
		return gatewayLogRepository.findAll();
	}

	@Override
	public boolean regist(GatewayLog domain) {
		if (isNew(domain)) {
			return gatewayLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(GatewayLog domain) {
		if (!isNew(domain)) {
			return gatewayLogRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		gatewayLogRepository.deleteById(id);
		return true;
	}

	private boolean isNew(GatewayLog domain) {
		return !gatewayLogRepository.existsById(domain.getId());
	}
}
