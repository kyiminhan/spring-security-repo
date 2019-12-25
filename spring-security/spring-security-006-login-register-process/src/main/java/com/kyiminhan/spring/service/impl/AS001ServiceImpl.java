package com.kyiminhan.spring.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.entity.AccountAuthority;
import com.kyiminhan.spring.entity.AccountPassword;
import com.kyiminhan.spring.entity.RegisteredAccount;
import com.kyiminhan.spring.repository.AccountRepository;
import com.kyiminhan.spring.repository.RegisteredAccountRepository;
import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegistrationDto;
import com.kyiminhan.spring.types.AccountLock;
import com.kyiminhan.spring.types.Authority;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class AS001ServiceImpl implements AS001Service {

	private PasswordEncoder encoder;
	private EmailService emailService;
	private AccountRepository accRepo;
	private RegisteredAccountRepository regRepo;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void userAccountRegistration(final AS001RegistrationDto dto) throws Exception {

		final RegisteredAccount registeredAccount = RegisteredAccount.builder().build();

		BeanUtils.copyProperties(dto, registeredAccount);
		registeredAccount.setPassword(this.encoder.encode(dto.getPassword()));
		registeredAccount.setExpiry(LocalDateTime.now().plusDays(1));

		this.regRepo.saveAndFlush(registeredAccount);

		this.emailService.sendRegisterationEmail(registeredAccount);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean hasRegisteredEmail(final String email) {
		return (this.accRepo.findByEmail(email).orElse(null) != null) ? true
		        : (this.regRepo.findByEmail(email).orElse(null) != null) ? true : false;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteAll() {
		this.regRepo.deleteAll();
	}

	@Override
	public void userAccountConfirmation(final String uuid) {
		final RegisteredAccount regAccount = this.regRepo.findByUuid(uuid).orElse(null);

		final Account account = Account.builder().build();
		BeanUtils.copyProperties(regAccount, account, "id");

		final AccountAuthority authUser = AccountAuthority.builder().authority(Authority.USER).build();

		account.setLoginDt(LocalDateTime.now());
		account.setPasswordExpiredDt(LocalDateTime.now().plusDays(90));
		account.setAccountLock(AccountLock.UN_LOCKED);

		final Set<AccountAuthority> authorities = new HashSet<>();
		authUser.setAccount(account);
		authorities.add(authUser);
		account.setAuthorities(authorities);

		// @formatter:off
		final AccountPassword userAccPwd = AccountPassword
				.builder()
				.account(account)
				.password(account.getPassword())
				.build();
		// @formatter:on

		final Set<AccountPassword> passwords = new HashSet<>();
		passwords.add(userAccPwd);
		account.setPasswords(passwords);

		this.accRepo.saveAndFlush(account);

		this.regRepo.delete(regAccount);

	}

	@Override
	public boolean isRegisterExpired(final String uuid) {
		final RegisteredAccount acc = this.regRepo.findByUuid(uuid).orElse(null);
		return (null != acc) ? LocalDateTime.now().isAfter(acc.getExpiry()) ? true : false : true;
	}
}