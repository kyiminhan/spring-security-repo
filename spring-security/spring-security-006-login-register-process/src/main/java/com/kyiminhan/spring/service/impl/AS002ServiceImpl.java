package com.kyiminhan.spring.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.entity.AccountPassword;
import com.kyiminhan.spring.entity.PasswordRequest;
import com.kyiminhan.spring.repository.AccountRepository;
import com.kyiminhan.spring.repository.PasswordRequestRepository;
import com.kyiminhan.spring.service.AS002Service;
import com.kyiminhan.spring.service.dto.AS002PwdChangeDto;
import com.kyiminhan.spring.types.InitialPwdFg;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class AS002ServiceImpl implements AS002Service {

	private PasswordEncoder encoder;
	private EmailService emailService;
	private AccountRepository accRepo;
	private PasswordRequestRepository pwdReqRepo;

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

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void createPasswordRequest(final String email) {

		this.pwdReqRepo.deleteAllByEmail(email);

		if (null != this.accRepo.findByEmail(email).orElse(null)) {

			final LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
			final PasswordRequest pwdRequest = PasswordRequest.builder().email(email).expiry(expiryDate).build();

			this.pwdReqRepo.saveAndFlush(pwdRequest);

			this.emailService.sendForgotPasswordResponseEmail(pwdRequest);
		}
	}

	@Override
	public boolean hasPasswordRequest(final String uuid) {
		final PasswordRequest request = this.pwdReqRepo.findByUuid(uuid).orElse(null);
		return (null != request) ? (LocalDateTime.now().isBefore(request.getExpiry())) ? true : false : false;
	}

	@Override
	public void doPasswordReset(final AS002PwdChangeDto dto, final String uuid) {
		final PasswordRequest request = this.pwdReqRepo.findByUuid(uuid).orElse(null);
		final Account account = this.accRepo.findByEmail(request.getEmail()).orElse(null);

		final String newPassword = this.encoder.encode(dto.getNewPassword());
		account.setPassword(newPassword);

		final AccountPassword accPwd = AccountPassword.builder().password(newPassword).account(account).build();
		account.getPasswords().add(accPwd);

		this.accRepo.saveAndFlush(account);
	}
}