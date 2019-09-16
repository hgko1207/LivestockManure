package kr.co.harangi.lmcfs.domain.db;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import kr.co.harangi.lmcfs.domain.Domain;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GatewayLog implements Domain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConnectionStatus status;
	
	@CreationTimestamp
	private LocalDateTime createDate;
	
	public GatewayLog(ConnectionStatus status) {
		this.status = status;
	}
	
	public enum ConnectionStatus {
		ON, OFF
	}
}
