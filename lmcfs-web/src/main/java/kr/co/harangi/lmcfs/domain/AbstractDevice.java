package kr.co.harangi.lmcfs.domain;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * 장치 공통 정보
 * 
 * @author hgko
 *
 */
@MappedSuperclass
@Data
public abstract class AbstractDevice implements Domain {
	
	private static final int ALIVE_TIMEOUT_MILLISECONDS = 10 * 60 * 1000;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	@Column(nullable = false, length = 2)
	private String macId;
	
	/** 동작 상태 - 이상/정상 */
	private boolean alive;
	
	@CreationTimestamp
	protected LocalDateTime createDate;
	
	@UpdateTimestamp
	protected LocalDateTime updateDate;
	
	public boolean setActive() {
		if (!alive) {
			alive = true;
			return true;
		}
		return false;
	}
	
	/**
	 * @return 상태 변화 여부
	 */
	public boolean setInactiveIfTimeout() {
		if (alive && updateDate != null) {
			if (System.currentTimeMillis() - updateDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() >= ALIVE_TIMEOUT_MILLISECONDS) {
				alive = false;
				return true;
			}
		}
		return false;
	}
	
	public enum DeviceType {
		
	}
}
