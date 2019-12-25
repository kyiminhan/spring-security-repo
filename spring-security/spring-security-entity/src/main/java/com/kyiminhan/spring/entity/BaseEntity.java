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
import javax.persistence.PreUpdate;

import org.hibernate.annotations.Where;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

	public BaseEntity(final Long id) {
		super();
		this.id = id;
	}

	@PrePersist
	private void prePersist() {

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final String name = (null == auth) ? "SYSTEM" : auth.getName();

		this.uuid = UUID.randomUUID().toString();
		this.delFg = DelFg.ACTIVE;
		this.createdBy = name;
		this.createdDt = LocalDateTime.now();
		this.lastModifiedBy = name;
		this.lastModifiedDt = LocalDateTime.now();
	}

	@PreUpdate
	private void preUpdate() {

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final String name = (null == auth) ? this.createdBy : auth.getName();

		this.uuid = UUID.randomUUID().toString();
		this.lastModifiedBy = name;
		this.lastModifiedDt = LocalDateTime.now();
	}

}