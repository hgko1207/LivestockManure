package kr.co.harangi.lmcfs.service;

import java.io.Serializable;
import java.util.List;

import kr.co.harangi.lmcfs.domain.Domain;

public interface CRUDService<T extends Domain, ID extends Serializable> {
	
	T get(ID id);
	
	List<T> getList();
	
	boolean regist(T domain);

	boolean update(T domain);
	
	boolean delete(ID id);
	
}
