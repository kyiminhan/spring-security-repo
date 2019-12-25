package com.kyiminhan.spring.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.spring.dto.User;
import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.repository.AccountRepository;
import com.kyiminhan.spring.service.LoginService;
import com.kyiminhan.spring.types.AccountLock;
import com.kyiminhan.spring.types.InitialPwdFg;

import lombok.NonNull;
import lombok.Setter;

@Service
@Setter(onMethod = @__(@Autowired))
public class LoginServiceImpl implements LoginService {

	private AccountRepository accRepo;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(@NonNull final String email) throws UsernameNotFoundException {
		final Account account = this.accRepo.findByEmail(email).orElse(null);
		if (null == account) {
			throw new UsernameNotFoundException("User'name " + email + " is not found!");
		}
		return User.builder().account(account).build();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void doLoginFailureProcess(final String email) {
		final Account account = this.accRepo.findByEmail(email).orElse(null);
		if (null != account) {
			final int i = account.getLoginAttempt() + 1;
			final AccountLock accountLock = (LoginService.LIMIT_ATTEMPT <= i) ? AccountLock.LOCKED
			        : AccountLock.UN_LOCKED;
			account.setLoginAttempt(i);
			account.setAccountLock(accountLock);
			this.accRepo.saveAndFlush(account);
			if (AccountLock.LOCKED.equals(accountLock)) {
				throw new LockedException("Oh! Your account was locked. Contact the admin.");
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void doLoginSuccessProcess(final String email) {
		final Account account = this.accRepo.findByEmail(email).orElse(null);
		account.setLoginAttempt(0);
		account.setLoginDt(LocalDateTime.now());
		this.accRepo.saveAndFlush(account);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean isInitialPassword(final String email) {
		return (InitialPwdFg.INITIAL.equals(this.accRepo.findByEmail(email).orElse(null).getInitialPwdFg())) ? true
		        : false;
	}

}