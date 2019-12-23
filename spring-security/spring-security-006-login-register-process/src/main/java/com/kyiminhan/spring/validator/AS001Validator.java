package com.kyiminhan.spring.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.kyiminhan.spring.service.AS001Service;
import com.kyiminhan.spring.service.dto.AS001RegistrationDto;

@Component
public class AS001Validator extends MyValidatorImpl {

	@Autowired
	private AS001Service service;

	@Override
	public boolean supports(final Class<?> clazz) {
		return AS001RegistrationDto.class.equals(clazz);
	}

	@Override
	public void validate(final Object targetObj, final Errors e) {

		final AS001RegistrationDto dto = (AS001RegistrationDto) targetObj;

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "firstName", "required", this.getMsgObjArr("firstName"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "lastName", "required", this.getMsgObjArr("lastName"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "email", "required", this.getMsgObjArr("email"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "password", "required", this.getMsgObjArr("password"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "confirmPassword", "required",
		        this.getMsgObjArr("confirmPassword"));

		if (!e.hasErrors()) {
			if (!StringUtils.equals(dto.getPassword(), dto.getConfirmPassword())) {
				e.rejectValue("confirmPassword", "confirmPassword.notMismatch");
			} else if (this.service.hasRegisteredEmail(dto.getEmail())) {
				e.rejectValue("email", "email.already.registered");
			}
		}

	}

}