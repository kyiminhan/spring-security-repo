package com.kyiminhan.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyiminhan.service.LoginService;
import com.kyiminhan.service.dto.User;
import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.entity.AccountAuthority;
import com.kyiminhan.spring.repository.AccountRepository;
import com.kyiminhan.spring.types.AccountLock;
import com.kyiminhan.spring.types.Authority;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class LoginServiceImpl implements LoginService, UserDetailsService {

	private AccountRepository accRepo;

	private PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = this.accRepo.findByEmail(email).orElse(null);
		if (null == account) {
			throw new UsernameNotFoundException("User'name " + email + " is not found!");
		}
		return User.builder().account(account).build();
	}

	@PostConstruct
	private void initLoadData() {
		if (0 == this.accRepo.count()) {
			AccountAuthority authUser = AccountAuthority.builder().authority(Authority.USER).build();
			AccountAuthority authManager = AccountAuthority.builder().authority(Authority.MANAGER).build();
			AccountAuthority authAdmin = AccountAuthority.builder().authority(Authority.ADMIN).build();
		// @formatter:off
		Account accUser = Account.builder()
				.firstName("FirstName")
				.lastName("LastName")
				.email("user@gmail.com")
				.password(encoder.encode("user"))
				.loginDt(LocalDateTime.now())
				.passwordExpiredDt(LocalDateTime.now().plusDays(90))
				.accountLock(AccountLock.UNLOCKED)
				.build();
		// @formatter:on
			Set<AccountAuthority> authorities = new HashSet<AccountAuthority>();
			authUser.setAccount(accUser);
			authorities.add(authUser);
			accUser.setAuthorities(authorities);
			accRepo.saveAndFlush(accUser);

			// @formatter:off
			Account accManager = Account.builder()
					.firstName("FirstName")
					.lastName("LastName")
					.email("manager@gmail.com")
					.password(encoder.encode("manager"))
					.loginDt(LocalDateTime.now())
					.passwordExpiredDt(LocalDateTime.now().plusDays(90))
					.accountLock(AccountLock.UNLOCKED)
					.build();
			// @formatter:on
			authorities = new HashSet<AccountAuthority>();
			authManager.setAccount(accManager);
			authorities.add(authManager);
			accManager.setAuthorities(authorities);
			accRepo.saveAndFlush(accManager);

			// @formatter:off
				Account accAdmin = Account.builder()
						.firstName("FirstName")
						.lastName("LastName")
						.email("admin@gmail.com")
						.password(encoder.encode("admin"))
						.loginDt(LocalDateTime.now())
						.passwordExpiredDt(LocalDateTime.now().plusDays(90))
						.accountLock(AccountLock.UNLOCKED)
						.build();
				// @formatter:on
			authorities = new HashSet<AccountAuthority>();
			authAdmin.setAccount(accAdmin);
			authorities.add(authAdmin);
			accAdmin.setAuthorities(authorities);
			accRepo.saveAndFlush(accAdmin);
		}
	}
}