package com.kyiminhan.spring.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.kyiminhan.service.LoginService;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.kyiminhan")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginService loginService;

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
		.rememberMeCookieName("remember-me")
		.tokenValiditySeconds(24 * 60 * 60 )// keep for one day
		.tokenRepository(this.tokenRepository())
		.userDetailsService(this.loginService);

		// @formatter:on

		super.configure(http);
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider() {
			@Override
			public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

				if (StringUtils.isBlank(authentication.getName())) {

					throw new BadCredentialsException("Email is required.");

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
		provider.setUserDetailsService(this.loginService);
		auth.authenticationProvider(provider);
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/css/**", "/static/js/**");
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		final JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(this.dataSource);
		return jdbcTokenRepositoryImpl;
	}
}