package com.kyiminhan.spring.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.kyiminhan.spring.service.dto.AS002PwdChangeDto;

@Component
public class AS002Validator extends MyValidatorImpl {

	@Override
	public boolean supports(final Class<?> clazz) {
		return AS002PwdChangeDto.class.equals(clazz);

	}

	@Override
	protected void validateRequired(final Object target, final Errors e) {
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "newPassword", "required", this.getMsgObjArr("newPassword"));
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "confirmNewPassword", "required",
				this.getMsgObjArr("confirmNewPassword"));
	}

	@Override
	protected void validateCustomize(final Object target, final Errors e) {

		final AS002PwdChangeDto dto = (AS002PwdChangeDto) target;

		if (!e.hasErrors()) {
			if (!this.isStrongPasswordPattern(dto.getNewPassword())) {
				e.rejectValue("newPassword", "password.invalid");
			} else if (!StringUtils.equals(dto.getNewPassword(), dto.getConfirmNewPassword())) {
				e.rejectValue("confirmNewPassword", "confirmNewPassword.notMismatch");
			}
		}
	}

}