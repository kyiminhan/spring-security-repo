package com.kyiminhan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kyiminhan.service.LoginService;
import com.kyiminhan.service.dto.User;
import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.repository.AccountRepository;

import lombok.Setter;

@Lazy
@Service
@Setter(onMethod = @__(@Autowired))
public class LoginServiceImpl implements LoginService, UserDetailsService {

	private AccountRepository accRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = this.accRepo.findByEmail().orElse(null);
		if (null == account) {
			throw new UsernameNotFoundException("User'name " + username + " is not found!");
		}
		return User.builder().account(account).build();
	}
}