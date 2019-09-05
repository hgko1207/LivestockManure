package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.Agitator;
import kr.co.harangi.lmcfs.repository.AgitatorRepository;
import kr.co.harangi.lmcfs.service.AgitatorService;

@Transactional
@Service
public class AgitatorServiceImpl implements AgitatorService {
	
	@Autowired
	private AgitatorRepository agitatorRepository;

	@Override
	public Agitator get(Integer id) {
		return agitatorRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Agitator> getList() {
		return agitatorRepository.findAll();
	}

	@Override
	public boolean regist(Agitator domain) {
		if (isNew(domain)) {
			return agitatorRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(Agitator domain) {
		if (!isNew(domain)) {
			return agitatorRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		agitatorRepository.deleteById(id);
		return true;
	}

	private boolean isNew(Agitator domain) {
		return !agitatorRepository.existsById(domain.getId());
	}

	@Override
	public List<Agitator> getList(boolean alive) {
		return agitatorRepository.findByAlive(alive);
	}

	@Override
	public Agitator get(String macId) {
		return agitatorRepository.findByMacId(macId);
	}

}
