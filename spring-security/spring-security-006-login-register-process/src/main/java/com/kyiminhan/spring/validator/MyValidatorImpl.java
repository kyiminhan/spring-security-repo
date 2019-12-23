package com.kyiminhan.spring.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class MyValidatorImpl implements Validator {

	@Autowired
	private MessageSource msgSource;

	protected String getMessage(final String msgCode) {
		final String message = this.msgSource.getMessage(msgCode, null, null);
		return message;
	}

	protected Object[] getMsgObjArr(final String msgCode) {
		final String message = this.msgSource.getMessage(msgCode, null, null);
		return new Object[] { message };
	}

	@Override
	public void validate(final Object target, final Errors errors) {

		if (!errors.hasErrors()) {
			this.validateRequired(target, errors);
		}

		if (!errors.hasErrors()) {
			this.validateLength(target, errors);
		}
		if (!errors.hasErrors()) {
			this.validateCustomize(target, errors);
		}

	}

	protected abstract void validateRequired(Object target, Errors errors);

	protected void validateLength(final Object target, final Errors errors) {
	}

	protected void validateCustomize(final Object target, final Errors errors) {
	}

}
