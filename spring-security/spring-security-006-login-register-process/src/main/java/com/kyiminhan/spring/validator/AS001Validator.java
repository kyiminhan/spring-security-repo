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
	protected void validateRequired(final Object target, final Errors e) {

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "firstName", "required", this.getMsgObjArr("firstName"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "lastName", "required", this.getMsgObjArr("lastName"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "email", "required", this.getMsgObjArr("email"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "password", "required", this.getMsgObjArr("password"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "confirmPassword", "required",
		        this.getMsgObjArr("confirmPassword"));

	}

	@Override
	protected void validateCustomize(final Object target, final Errors e) {

		final AS001RegistrationDto dto = (AS001RegistrationDto) target;

		if (!e.hasErrors()) {
			if (!this.isAcutalEmail(dto.getEmail())) {
				e.rejectValue("email", "email.invalid");
			} else if (this.service.hasRegisteredEmail(dto.getEmail())) {
				e.rejectValue("email", "email.already.registered");
			} else if (!this.isStrongPasswordPattern(dto.getPassword())) {
				e.rejectValue("password", "password.invalid");
			} else if (!StringUtils.equals(dto.getPassword(), dto.getConfirmPassword())) {
				e.rejectValue("confirmPassword", "confirmPassword.notMismatch");
			}
		}

	}

}