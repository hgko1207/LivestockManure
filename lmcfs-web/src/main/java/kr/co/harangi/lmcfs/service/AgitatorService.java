package kr.co.harangi.lmcfs.service;

import java.util.List;

import kr.co.harangi.lmcfs.domain.db.Agitator;

public interface AgitatorService extends CRUDService<Agitator, Integer> {

	List<Agitator> getList(boolean alive);

	Agitator get(String macId);

}
