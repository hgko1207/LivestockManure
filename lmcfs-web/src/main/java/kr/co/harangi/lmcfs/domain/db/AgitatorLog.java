package kr.co.harangi.lmcfs.domain.db;

import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.harangi.lmcfs.domain.AbstractLogDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 교반기 로그 정보 테이블 도메인
 * 
 * @author hgko
 *
 */
@Entity
@Table(name = "tb_agitator_log")
@Data
@EqualsAndHashCode(callSuper = false)
public class AgitatorLog extends AbstractLogDomain {

	/** ON OFF 버튼 상태 */
	private boolean status;
}
