package com.kyiminhan.spring.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Override
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

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
}