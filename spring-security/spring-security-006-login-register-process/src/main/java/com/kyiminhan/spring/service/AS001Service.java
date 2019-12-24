package com.kyiminhan.spring.service;

import com.kyiminhan.spring.service.dto.AS001RegistrationDto;

public interface AS001Service {

	void userAccountRegistration(AS001RegistrationDto registerationDto) throws Exception;

	void userAccountConfirmation(String uuid);

	boolean hasRegisteredEmail(String email);

	void deleteAll();
}
