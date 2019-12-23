package com.kyiminhan.spring.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class AppConfig {

	@Bean
	public MessageSource messageSource() {
		final ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
		msgSource.addBasenames(new String[] { "i18n/messages" });
		msgSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		msgSource.setCacheSeconds(3600);
		return msgSource;
	}
}
