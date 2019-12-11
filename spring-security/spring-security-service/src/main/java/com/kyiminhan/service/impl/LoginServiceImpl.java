package com.kyiminhan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kyiminhan.dto.User;
import com.kyiminhan.service.LoginService;
import com.kyiminhan.spring.entity.Account;
import com.kyiminhan.spring.repository.AccountRepository;

import lombok.Setter;

@Service("loginServiceImpl")
@Setter(onMethod = @__(@Autowired))
public class LoginServiceImpl implements LoginService, UserDetailsService {

	private AccountRepository accRepo;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = this.accRepo.findByEmail(email).orElse(null);
		if (null == account) {
			throw new UsernameNotFoundException("User'name " + email + " is not found!");
		}
		return User.builder().account(account).build();
	}

}