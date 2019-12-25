package com.kyiminhan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
@ComponentScan(basePackages = "com.kyiminhan")
@EntityScan(basePackages = { "com.kyiminhan.spring.entity" })
@EnableTransactionManagement
public class App implements CommandLineRunner {

	public static void main(final String... args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {

	}
}