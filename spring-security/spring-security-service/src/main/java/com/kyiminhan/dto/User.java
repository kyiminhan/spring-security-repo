package com.kyiminhan.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.types.AccountLock;
import com.kyiminhan.spring.types.DelFg;

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
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Account account;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Collection<GrantedAuthority> authorities = new HashSet<>();
		this.account.getAuthorities().forEach(accountAuth -> authorities
				.add(new SimpleGrantedAuthority(accountAuth.getAuthority().getGrantAuthRole())));
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.account.getPassword();
	}

	@Override
	public String getUsername() {
		return this.account.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return (AccountLock.UNLOCKED.equals(this.account.getAccountLock())) ? true : false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return (LocalDateTime.now().isBefore(this.account.getPasswordExpiredDt())) ? true : false;
	}

	@Override
	public boolean isEnabled() {
		return (DelFg.ACTIVE.equals(this.account.getDelFg())) ? true : false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.account.getEmail() == null) ? 0 : this.account.getEmail().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (this.account.getEmail() == null) {
			if (other.getAccount().getEmail() != null) {
				return false;
			} else if (!this.account.getEmail().equals(other.getAccount().getEmail())) {
				return false;
			}
		}
		return true;
	}
}