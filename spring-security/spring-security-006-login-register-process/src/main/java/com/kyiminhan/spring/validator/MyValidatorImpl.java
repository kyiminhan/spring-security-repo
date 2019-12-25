package com.kyiminhan.spring.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.EmailValidator;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class MyValidatorImpl implements Validator {

	@Autowired
	private MessageSource msgSource;

	protected abstract void validateRequired(Object target, Errors e);

	protected void validateLength(final Object target, final Errors e) {
	}

	protected void validateCustomize(final Object target, final Errors e) {
	}

	@Override
	final public void validate(final Object target, final Errors e) {

		if (!e.hasErrors()) {
			this.validateRequired(target, e);
		}
		if (!e.hasErrors()) {
			this.validateLength(target, e);
		}
		if (!e.hasErrors()) {
			this.validateCustomize(target, e);
		}
	}

	final protected String getMessage(final String msgCode) {
		final String message = this.msgSource.getMessage(msgCode, null, null);
		return message;
	}

	final protected Object[] getMsgObjArr(final String msgCode) {
		final String message = this.msgSource.getMessage(msgCode, null, null);
		return new Object[] { message };
	}

	final protected boolean isAcutalEmail(final String email) {
		final boolean allowLocal = true;
		boolean valid = true;
		valid = EmailValidator.getInstance(allowLocal).isValid(email);
		return valid;
	}

	final protected boolean isStrongPasswordPattern(final String password) {

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
		        new WhitespaceRule()));

		final RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {

			final List<String> messages = validator.getMessages(result);
			@SuppressWarnings("unused")
			final String messageTemplate = messages.stream().collect(Collectors.joining(","));

			return true;
		}
		return false;
	}
}