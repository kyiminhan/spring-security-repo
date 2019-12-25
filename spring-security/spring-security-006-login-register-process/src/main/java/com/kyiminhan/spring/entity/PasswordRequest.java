package com.kyiminhan.spring.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PasswordRequest extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@NotEmpty
	@Column(nullable = false, unique = true, insertable = true, updatable = false)
	private String email;

	@NotNull
	@Column(nullable = false, insertable = true, updatable = false)
	private LocalDateTime expiry;

	@Builder
	public PasswordRequest(final Long id, @NotNull @NotEmpty final String email, @NotNull final LocalDateTime expiry) {
		super(id);
		this.email = email;
		this.expiry = expiry;
	}
}