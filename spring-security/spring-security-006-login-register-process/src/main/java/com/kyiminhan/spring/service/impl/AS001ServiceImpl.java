package com.kyiminhan.spring.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyiminhan.spring.entity.RegisteredAccount;
import com.kyiminhan.spring.repository.RegisteredAccountRepository;
import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegisterationDto;
import com.kyiminhan.spring.types.Authority;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class AS001ServiceImpl implements AS001Service {

	private PasswordEncoder encoder;
	private RegisteredAccountRepository registerationRepository;

	@Override
	public void userAccountRegistration(final AS001RegisterationDto dto) {

		final RegisteredAccount registeredAccount = RegisteredAccount.builder().build();

		BeanUtils.copyProperties(dto, registeredAccount);
		registeredAccount.setPassword(this.encoder.encode(dto.getPassword()));
		registeredAccount.setExpiry(LocalDateTime.now().plusDays(1));
		registeredAccount.setAuthority(Authority.USER);

		this.registerationRepository.saveAndFlush(registeredAccount);
	}
}