package com.kyiminhan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.entity.AccountPassword;
import com.kyiminhan.spring.repository.AccountRepository;
import com.kyiminhan.spring.service.AS002Service;
import com.kyiminhan.spring.service.dto.AS002PwdChangeDto;
import com.kyiminhan.spring.types.InitialPwdFg;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class AS002ServiceImpl implements AS002Service {

	private PasswordEncoder encoder;
	private AccountRepository accRepo;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void changeInitPassword(final AS002PwdChangeDto dto) {

		final String email = SecurityContextHolder.getContext().getAuthentication().getName();
		final Account account = this.accRepo.findByEmail(email).orElse(null);

		final String newPassword = this.encoder.encode(dto.getNewPassword());
		account.setPassword(newPassword);
		account.setInitialPwdFg(InitialPwdFg.NON_INITIAL);

		final AccountPassword accPwd = AccountPassword.builder().password(newPassword).account(account).build();
		account.getPasswords().add(accPwd);

		this.accRepo.saveAndFlush(account);
	}
}