package com.kyiminhan.spring.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.spring.entity.RegisteredAccount;
import com.kyiminhan.spring.repository.RegisteredAccountRepository;
import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegistrationDto;
import com.kyiminhan.spring.types.Authority;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class AS001ServiceImpl implements AS001Service {

	private PasswordEncoder encoder;
	private EmailService emailService;
	private RegisteredAccountRepository regRepo;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void userAccountRegistration(final AS001RegistrationDto dto) throws Exception {

		final RegisteredAccount registeredAccount = RegisteredAccount.builder().build();

		BeanUtils.copyProperties(dto, registeredAccount);
		registeredAccount.setPassword(this.encoder.encode(dto.getPassword()));
		registeredAccount.setExpiry(LocalDateTime.now().plusDays(1));
		registeredAccount.setAuthority(Authority.USER);

		this.regRepo.saveAndFlush(registeredAccount);

		this.emailService.sendRegisterationEmail(registeredAccount);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean hasRegisteredEmail(final String email) {
		return (this.regRepo.findByEmail(email).orElse(null) != null) ? true : false;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteAll() {
		this.regRepo.deleteAll();
	}

	@Override
	public void userAccountConfirmation(final String uuid) {
		final RegisteredAccount registeredAccount = this.regRepo.findByUuid(uuid).orElse(null);
		System.out.println(registeredAccount);
	}
}