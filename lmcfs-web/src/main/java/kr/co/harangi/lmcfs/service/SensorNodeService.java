package kr.co.harangi.lmcfs.service;

import java.util.List;

import kr.co.harangi.lmcfs.domain.db.SensorNode;

public interface SensorNodeService extends CRUDService<SensorNode, Integer> {

	SensorNode get(String macId);
	
	List<SensorNode> getList(boolean alive);
}
