package kr.co.harangi.lmcfs.repository;

import java.util.List;

import kr.co.harangi.lmcfs.domain.db.SensorNode;

public interface SensorNodeRepository extends DefaultRepository<SensorNode, Integer> {

	SensorNode findByMacId(String macId);

	List<SensorNode> findByAlive(boolean alive);

}
