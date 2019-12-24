package com.kyiminhan.spring.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class AppConfig {

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(this.messageSource());
		return bean;
	}

	@Bean
	public MessageSource messageSource() {
		final ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
		msgSource.addBasenames(new String[] { "application", "i18n/messages" });
		msgSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		msgSource.setCacheSeconds(3600);
		return msgSource;
	}
}