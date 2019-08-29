package kr.co.harangi.lmcfs.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastPresenceTime;
	
	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}
	
	private void updateLastPresenceTime() {
		lastPresenceTime = new Date();
	}
	
	public boolean setActive() {
		updateLastPresenceTime();
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
		if (alive && lastPresenceTime != null) {
			if (System.currentTimeMillis() - lastPresenceTime.getTime() >= ALIVE_TIMEOUT_MILLISECONDS) {
				alive = false;
				return true;
			}
		}
		return false;
	}
}
