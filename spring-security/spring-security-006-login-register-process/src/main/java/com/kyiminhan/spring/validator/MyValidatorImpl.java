package com.kyiminhan.spring.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

}
