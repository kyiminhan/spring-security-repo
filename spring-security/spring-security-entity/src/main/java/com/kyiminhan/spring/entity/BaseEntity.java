package com.kyiminhan.spring.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.kyiminhan.spring.types.DelFg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
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
}