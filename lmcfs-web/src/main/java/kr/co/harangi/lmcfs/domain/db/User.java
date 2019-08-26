package kr.co.harangi.lmcfs.domain.db;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import kr.co.harangi.lmcfs.domain.Domain;
import lombok.Data;

/**
 * 사용자 정보 테이블 도메인
 * 
 * @author hgko
 *
 */
@Entity
@Table(name = "tb_user")
@Data
public class User implements Domain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@CreationTimestamp
	private LocalDateTime createDate;
	
	@UpdateTimestamp
	private LocalDateTime updateDate;
}
