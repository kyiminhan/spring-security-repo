package com.kyiminhan.spring.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kyiminhan.spring.entity.RegisteredAccount;
import com.kyiminhan.spring.repository.RegisteredAccountRepository;
import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegistrationDto;
import com.kyiminhan.spring.service.dto.Mail;
import com.kyiminhan.spring.types.Authority;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
@Slf4j
public class AS001ServiceImpl implements AS001Service {

	private PasswordEncoder encoder;
	private RegisteredAccountRepository regRepo;

	private EmailService emailService;

	@Override
	public void userAccountRegistration(final AS001RegistrationDto dto) {

		final RegisteredAccount registeredAccount = RegisteredAccount.builder().build();

		BeanUtils.copyProperties(dto, registeredAccount);
		registeredAccount.setPassword(this.encoder.encode(dto.getPassword()));
		registeredAccount.setExpiry(LocalDateTime.now().plusDays(1));
		registeredAccount.setAuthority(Authority.USER);

		this.regRepo.saveAndFlush(registeredAccount);

		AS001ServiceImpl.log.info("Sending Email with Thymeleaf HTML Template Example");

		final Mail mail = new Mail();
		// add my mail
		mail.setFrom("XXX@gmail.com");
		mail.setTo(registeredAccount.getEmail());
		mail.setSubject("Sending Email with Thymeleaf HTML Template Example");

		final Map<String, Object> model = new HashMap<>();
		model.put("name", "Memorynotfound.com");
		model.put("location", "Belgium");
		model.put("signature", "https://memorynotfound.com");
		mail.setModel(model);

		try {

			this.emailService.sendSimpleMessage(mail);

		} catch (final MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasRegisteredEmail(final String email) {
		return (this.regRepo.findByEmail(email).orElse(null) != null) ? true : false;
	}
}