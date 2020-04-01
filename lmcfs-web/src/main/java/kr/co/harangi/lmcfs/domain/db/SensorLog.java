package kr.co.harangi.lmcfs.domain.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import kr.co.harangi.lmcfs.domain.AbstractLogDomain;
import kr.co.harangi.lmcfs.domain.db.SensorNode.SensorType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 센서 로그 정보 테이블 도메인
 * 
 * @author hgko
 *
 */
@Entity
@Table(name = "tb_sensor_log")
@Data
@EqualsAndHashCode(callSuper = false)
public class SensorLog extends AbstractLogDomain {
	
	/** 센서유형 */
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private SensorType sensorType;
	
	/** 온도 */
	private float temp;
	
	/** 습도 */
	private float hum;
	
	private float nh3;
	
	private float h2s;
	
	private float co2;
	
	private float o2;
	
	@Column(name = "original_h2s")
	private float originalH2s;
	
	@Column(name = "original_nh3")
	private float originalNh3;
	
	@Column(name = "original_o2")
	private float originalO2;
}
