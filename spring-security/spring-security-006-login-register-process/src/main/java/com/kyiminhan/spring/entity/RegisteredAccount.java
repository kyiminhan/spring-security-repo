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
public class RegisteredAccount extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String firstName;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String lastName;

	@NotNull
	@NotEmpty
	@Column(nullable = false, unique = true)
	private String email;

	@NotNull
	@NotEmpty
	@Column(nullable = false)
	private String password;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime expiry;

	@Builder
	public RegisteredAccount(final Long id, @NotNull @NotEmpty final String firstName,
	        @NotNull @NotEmpty final String lastName, @NotNull @NotEmpty final String email,
	        @NotNull @NotEmpty final String password, @NotNull final LocalDateTime expiry) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.expiry = expiry;
	}

}