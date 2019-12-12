package com.kyiminhan.spring.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persistent_logins")
public class PersistentLogins implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false, length = 64)
	private String series;
	@Column(nullable = false, length = 255)
	private String username;
	@Column(nullable = false, length = 255)
	private String token;
	@Column(nullable = false)
	private LocalDateTime last_used;
}