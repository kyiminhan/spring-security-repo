package com.kyiminhan.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.entity.AccountAuthority;
import com.kyiminhan.spring.entity.AccountPassword;
import com.kyiminhan.spring.repository.AccountRepository;
import com.kyiminhan.spring.types.AccountLock;
import com.kyiminhan.spring.types.Authority;

import lombok.Setter;

@Service
@Setter(onMethod = @__(@Autowired))
public class InitLoadData {

	private AccountRepository accRepo;

	private PasswordEncoder encoder;

	@Transactional(rollbackFor = Exception.class)
	@PostConstruct
	private void initLoadData() {
		if (0 == this.accRepo.count()) {
			AccountAuthority authUser = AccountAuthority.builder().authority(Authority.USER).build();

			String userPwd = this.encoder.encode("user");
		// @formatter:off
		Account accUser = Account.builder()
				.firstName("FirstName")
				.lastName("LastName")
				.email("user@gmail.com")
				.password(userPwd)
				.loginDt(LocalDateTime.now())
				.passwordExpiredDt(LocalDateTime.now().plusDays(90))
				.accountLock(AccountLock.UNLOCKED)
				.build();
		// @formatter:on
			Set<AccountAuthority> authorities = new HashSet<AccountAuthority>();
			authUser.setAccount(accUser);
			authorities.add(authUser);
			accUser.setAuthorities(authorities);

			AccountPassword userAccPwd = AccountPassword.builder().password(userPwd).account(accUser).build();
			Set<AccountPassword> passwords = new HashSet<AccountPassword>();
			passwords.add(userAccPwd);
			accUser.setPasswords(passwords);

			this.accRepo.saveAndFlush(accUser);

			String managerPwd = this.encoder.encode("manager");
			// @formatter:off
			Account accManager = Account.builder()
					.firstName("FirstName")
					.lastName("LastName")
					.email("manager@gmail.com")
					.password(managerPwd)
					.loginDt(LocalDateTime.now())
					.passwordExpiredDt(LocalDateTime.now().plusDays(90))
					.accountLock(AccountLock.UNLOCKED)
					.build();
			// @formatter:on
			authorities = new HashSet<AccountAuthority>();
			authUser = AccountAuthority.builder().authority(Authority.USER).build();
			AccountAuthority authManager = AccountAuthority.builder().authority(Authority.MANAGER).build();

			authUser.setAccount(accManager);
			authManager.setAccount(accManager);
			authorities.add(authUser);
			authorities.add(authManager);
			accManager.setAuthorities(authorities);

			AccountPassword managerAccPwd = AccountPassword.builder().password(managerPwd).account(accManager).build();
			passwords = new HashSet<AccountPassword>();
			passwords.add(managerAccPwd);
			accManager.setPasswords(passwords);

			this.accRepo.saveAndFlush(accManager);

			String adminPwd = this.encoder.encode("admin");
			// @formatter:off
				Account accAdmin = Account.builder()
						.firstName("FirstName")
						.lastName("LastName")
						.email("admin@gmail.com")
						.password(adminPwd)
						.loginDt(LocalDateTime.now())
						.passwordExpiredDt(LocalDateTime.now().plusDays(90))
						.accountLock(AccountLock.UNLOCKED)
						.build();
				// @formatter:on
			authorities = new HashSet<AccountAuthority>();
			authUser = AccountAuthority.builder().authority(Authority.USER).build();
			authManager = AccountAuthority.builder().authority(Authority.MANAGER).build();
			AccountAuthority authAdmin = AccountAuthority.builder().authority(Authority.ADMIN).build();

			authAdmin.setAccount(accAdmin);
			authManager.setAccount(accAdmin);
			authUser.setAccount(accAdmin);
			authorities.add(authAdmin);
			authorities.add(authManager);
			authorities.add(authUser);
			accAdmin.setAuthorities(authorities);

			AccountPassword adminAccPwd = AccountPassword.builder().password(adminPwd).account(accAdmin).build();
			passwords = new HashSet<AccountPassword>();
			passwords.add(adminAccPwd);
			accAdmin.setPasswords(passwords);

			this.accRepo.saveAndFlush(accAdmin);
		}
	}
}