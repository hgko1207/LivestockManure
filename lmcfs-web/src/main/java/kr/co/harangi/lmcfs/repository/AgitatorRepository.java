package kr.co.harangi.lmcfs.repository;

import java.util.List;

import kr.co.harangi.lmcfs.domain.db.Agitator;

public interface AgitatorRepository extends DefaultRepository<Agitator, Integer> {

	List<Agitator> findByAlive(boolean alive);

	Agitator findByMacId(String macId);

}
