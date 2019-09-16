package kr.co.harangi.lmcfs.repository;

import java.util.List;

import kr.co.harangi.lmcfs.domain.db.Blower;

public interface BlowerRepository extends DefaultRepository<Blower, Integer> {

	List<Blower> findByAlive(boolean alive);

	Blower findByMacId(String macId);

}
