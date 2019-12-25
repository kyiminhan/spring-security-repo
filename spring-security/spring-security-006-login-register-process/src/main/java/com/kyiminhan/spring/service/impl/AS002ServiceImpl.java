package com.kyiminhan.spring.service.impl;

import org.springframework.stereotype.Service;

import com.kyiminhan.spring.service.AS002Service;
import com.kyiminhan.spring.service.dto.AS002PwdChangeDto;

import lombok.Setter;

@Service
@Setter(onMethod = @__(@Autowired))
public class AS002ServiceImpl implements AS002Service {

	@Override
	public void changeInitPassword(final AS002PwdChangeDto dto) {
		// final String loginEmail = this.loginService.getLoginUserEmail();
	}
}