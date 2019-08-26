package kr.co.harangi.lmcfs.domain.db;

import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.harangi.lmcfs.domain.AbstractLogDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 브로워 로그 정보 테이블 도메인
 * 
 * @author hgko
 *
 */
@Entity
@Table(name = "tb_blower_log")
@Data
@EqualsAndHashCode(callSuper = false)
public class BlowerLog extends AbstractLogDomain {

	/** 버튼 1 ON OFF 상태 */
	private boolean btn1Status;
	
	/** 버튼 2 ON OFF 상태 */
	private boolean btn2Status;
	
	/** 버튼 3 ON OFF 상태 */
	private boolean btn3Status;
	
	/** 버튼 4 ON OFF 상태 */
	private boolean btn4Status;
}
