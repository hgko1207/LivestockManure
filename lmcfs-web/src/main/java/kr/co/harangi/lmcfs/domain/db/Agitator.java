package kr.co.harangi.lmcfs.domain.db;

import javax.persistence.Entity;
import javax.persistence.Table;

import kr.co.harangi.lmcfs.domain.AbstractDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 교반기 정보 테이블 도메인
 * 
 * @author hgko
 *
 */
@Entity
@Table(name = "tb_agitator")
@Data
@EqualsAndHashCode(callSuper = false)
public class Agitator extends AbstractDevice {

	/** ON OFF 버튼 상태 */
	private boolean status;
}
