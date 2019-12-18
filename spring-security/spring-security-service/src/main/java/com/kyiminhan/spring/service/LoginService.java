package com.kyiminhan.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginService extends UserDetailsService {

	Integer LIMIT_ATTEMPT = 3;

	void doLoginFailureProcess(String email);

	void doLoginSuccessProcess(String email);
}