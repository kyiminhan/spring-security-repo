package com.kyiminhan.spring.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kyiminhan.spring.types.Authority;

@Lazy
@Component
public class SuccessHandlar extends SimpleUrlAuthenticationSuccessHandler {
	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String targetUrl = determineTargetUrl(authentication);
		if (response.isCommitted()) {
			return;
		}
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(Authentication authentication) {
		List<String> roles = new ArrayList<String>();
		authentication.getAuthorities().forEach(auth -> roles.add(auth.getAuthority()));
		if (authentication.isAuthenticated()) {
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