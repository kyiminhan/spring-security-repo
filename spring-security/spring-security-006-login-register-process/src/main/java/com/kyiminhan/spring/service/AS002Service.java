package com.kyiminhan.spring.service;

import com.kyiminhan.spring.service.dto.AS002PwdChangeDto;

public interface AS002Service {

	void changeInitPassword(AS002PwdChangeDto dto);

	void createPasswordRequest(String email) throws Exception;

	boolean hasPasswordRequest(String uuid);

	void doPasswordReset(AS002PwdChangeDto dto, String uuid);
}