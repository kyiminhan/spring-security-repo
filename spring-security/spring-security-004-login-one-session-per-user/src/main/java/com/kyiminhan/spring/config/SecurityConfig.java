package com.kyiminhan.spring.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.NonNull;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.kyiminhan")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	@Qualifier("loginServiceImpl")
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationSuccessHandler successHandlar;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/login").permitAll();
		http.authorizeRequests().antMatchers("/user/**").hasAnyRole("USER");
		http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/manager/**").hasAnyRole("MANAGER");
		http.authorizeRequests().antMatchers("/", "/home").hasAnyRole("USER", "MANAGER", "ADMIN");

		// @formatter:off
		http
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/login")
		.usernameParameter("email")
		.passwordParameter("password")
		.successHandler(this.successHandlar)
		.permitAll();

		http
		.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true);

		http
		.exceptionHandling()
		.accessDeniedPage("/access-denied");

		http.rememberMe()
		.key("uniqueAndSecret")
		.rememberMeParameter("remember-me")
		.rememberMeCookieName("my-remember-me")
		.tokenValiditySeconds(24 * 60 * 60 )// keep for one day
		.tokenRepository(this.tokenRepository())
		.userDetailsService(this.userDetailsService);

		http
		.sessionManagement()
		.maximumSessions(1)
		.maxSessionsPreventsLogin(false)
		.sessionRegistry(this.sessionRegistry())
		;

		// https://mtyurt.net/post/spring-expiring-all-sessions-of-a-user.html

		// @formatter:on

		super.configure(http);
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
			@Override
			public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

				if (StringUtils.isBlank(authentication.getName())) {

					throw new UsernameNotFoundException("Email is required.");

				} else if (!ObjectUtils.anyNotNull(authentication.getCredentials())
						| StringUtils.isBlank(authentication.getCredentials().toString())) {

					throw new BadCredentialsException("Password is required.");

				} else {

					try {

						return super.authenticate(authentication);

					} catch (final BadCredentialsException e) {

						throw new BadCredentialsException("Invalid email and password.");
					}
				}
			}

			@Override
			public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
				super.setPasswordEncoder(SecurityConfig.this.getPasswordEncoder());
			}
		};
		provider.setUserDetailsService((@NonNull final String email) -> {
			final UserDetails userDetails = SecurityConfig.this.userDetailsService.loadUserByUsername(email);
			if (!ObjectUtils.anyNotNull(userDetails)) {
				throw new UsernameNotFoundException("User Not Found");
			}
			return userDetails;
		});
		auth.authenticationProvider(provider);
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/css/**", "/static/js/**");
	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		final JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(this.dataSource);
		return jdbcTokenRepositoryImpl;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		final SessionRegistry sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}

	// public HttpSessionEventPublisher httpSessionEventPublisher() {
	// return new HttpSessionEventPublisher();
	// }

	@Bean
	public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}
}