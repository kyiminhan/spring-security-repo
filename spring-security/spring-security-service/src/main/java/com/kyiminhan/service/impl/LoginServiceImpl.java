package com.kyiminhan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.dto.User;
import com.kyiminhan.service.LoginService;
import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.repository.AccountRepository;

import lombok.NonNull;
import lombok.Setter;

@Service
@Setter(onMethod = @__(@Autowired))
public class LoginServiceImpl implements LoginService {

	private AccountRepository accRepo;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(@NonNull final String email) throws UsernameNotFoundException {
		final Account account = this.accRepo.findByEmail(email).orElse(null);
		if (null == account) {
			throw new UsernameNotFoundException("User'name " + email + " is not found!");
		}
		return User.builder().account(account).build();
	}

}