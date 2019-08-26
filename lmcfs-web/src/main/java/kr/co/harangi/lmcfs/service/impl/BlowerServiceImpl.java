package kr.co.harangi.lmcfs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.harangi.lmcfs.domain.db.Blower;
import kr.co.harangi.lmcfs.repository.BlowerRepository;
import kr.co.harangi.lmcfs.service.BlowerService;

@Transactional
@Service
public class BlowerServiceImpl implements BlowerService {
	
	@Autowired
	private BlowerRepository blowerRepository;

	@Override
	public Blower get(Integer id) {
		return blowerRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Blower> getList() {
		return blowerRepository.findAll();
	}

	@Override
	public boolean regist(Blower domain) {
		if (isNew(domain)) {
			return blowerRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean update(Blower domain) {
		if (!isNew(domain)) {
			return blowerRepository.save(domain) != null;
		} else {
			return false;
		}	
	}

	@Override
	public boolean delete(Integer id) {
		blowerRepository.deleteById(id);
		return true;
	}

	private boolean isNew(Blower domain) {
		return !blowerRepository.existsById(domain.getId());
	}

	@Override
	public List<Blower> getList(boolean alive) {
		return blowerRepository.findByAlive(alive);
	}

}
