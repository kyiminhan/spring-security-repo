package com.kyiminhan.spring.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
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

	private boolean isAcutalEmail(final String email) {
		final boolean allowLocal = true;
		boolean valid = true;
		valid = EmailValidator.getInstance(allowLocal).isValid(email);
		return valid;
	}

	private boolean isStrongPasswordPattern(final String password) {

		final LengthRule lengthRule = new LengthRule();
		lengthRule.setMinimumLength(8);

		final PasswordValidator validator = new PasswordValidator(Arrays.asList(
		        // at least 8 characters
		        lengthRule,
		        // at least one upper-case character
		        new CharacterRule(EnglishCharacterData.UpperCase, 1),
		        // at least one lower-case character
		        new CharacterRule(EnglishCharacterData.LowerCase, 1),
		        // at least one digit character
		        new CharacterRule(EnglishCharacterData.Digit, 1),
		        // at least one symbol (special character)
		        new CharacterRule(EnglishCharacterData.Special, 1),
		        // no whitespace
		        new WhitespaceRule()

		));
		final RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		final List<String> messages = validator.getMessages(result);

		final String messageTemplate = messages.stream().collect(Collectors.joining(","));

		return false;
	}

}