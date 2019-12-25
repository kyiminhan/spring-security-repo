package com.kyiminhan.spring.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kyiminhan.spring.service.LoginService;
import com.kyiminhan.spring.types.Authority;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private LoginService loginService;

	@Override
	protected void handle(final HttpServletRequest request, final HttpServletResponse response,
	        final Authentication authentication) throws IOException, ServletException {
		final String targetUrl = this.determineTargetUrl(authentication);
		if (response.isCommitted()) {
			log.info("login successful.");
			return;
		}
		this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(final Authentication authentication) {
		final List<String> roles = new ArrayList<>();
		authentication.getAuthorities().forEach(auth -> roles.add(auth.getAuthority()));
		if (authentication.isAuthenticated()) {

			if (this.loginService.isInitialPassword(authentication.getName())) {
				return "/init-password-change";
			}

			if (roles.contains(Authority.ADMIN.getGrantAuthRole())) {
				return "/admin/home";
			}
			if (roles.contains(Authority.MANAGER.getGrantAuthRole())) {
				return "/manager/home";
			}
			if (roles.contains(Authority.USER.getGrantAuthRole())) {
				return "/user/home";
			}
		} else {
			return "/accessDenied";
		}
		return null;
	}
}