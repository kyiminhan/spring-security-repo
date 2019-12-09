package com.kyiminhan.spring.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class AccountPassword extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(insertable = true, updatable = false, nullable = false)
	private String password;

	@ManyToOne(cascade = { CascadeType.DETACH,
			CascadeType.REFRESH }, fetch = FetchType.EAGER, targetEntity = Account.class, optional = true)
	@JoinColumn(name = "account_id")
	private Account account;

	@Builder
	public AccountPassword(Long id, String password, Account account) {
		super(id);
		this.password = password;
		this.account = account;
	}
}