package com.kyiminhan.spring.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.kyiminhan.spring.service.dto.AS001RegistrationDto;

@Component
public class AS001Validator extends MyValidatorImpl {

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

	}

}