package kr.co.harangi.lmcfs.domain.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import kr.co.harangi.lmcfs.domain.AbstractDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 센서 정보 테이블 도메인
 * 
 * @author hgko
 *
 */
@Entity
@Table(name = "tb_sensor_node")
@Data
@EqualsAndHashCode(callSuper = false)
public class SensorNode extends AbstractDevice {
	
	/** 센서유형 */
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private SensorType sensorType;
	
	/** 온도  */
	private float temp;
	
	/** 습도 */
	private float hum;
	
	private float nh3;
	
	private float h2s;
	
	private float co2;
	
	private float o2;
	
	public enum SensorType {
		온도, 가스
	}
	
}
