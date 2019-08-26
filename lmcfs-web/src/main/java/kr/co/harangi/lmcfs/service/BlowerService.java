package kr.co.harangi.lmcfs.service;

import java.util.List;

import kr.co.harangi.lmcfs.domain.db.Blower;

public interface BlowerService extends CRUDService<Blower, Integer> {

	List<Blower> getList(boolean alive);

}
