package com.kyiminhan.spring.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Where;

import com.kyiminhan.spring.types.DelFg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@Where(clause = "delFg='ACTIVE'")
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	protected Long id;

	@Column(nullable = false)
	protected String uuid;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected DelFg delFg;

	@Column(nullable = false, insertable = true, updatable = false)
	protected String createdBy;

	@Column(nullable = false, insertable = true, updatable = false)
	protected LocalDateTime createdDt;

	@Column(nullable = false, insertable = true, updatable = true)
	protected String lastModifiedBy;

	@Column(nullable = false, insertable = true, updatable = true)
	protected LocalDateTime lastModifiedDt;

	public BaseEntity(Long id) {
		super();
		this.id = id;
	}

	@PrePersist
	private void prePersist() {
		this.uuid = UUID.randomUUID().toString();
		this.delFg = DelFg.ACTIVE;
		this.createdBy = "ADMIN";
		this.createdDt = LocalDateTime.now();
		this.lastModifiedBy = "ADMIN";
		this.lastModifiedDt = LocalDateTime.now();
	}
}