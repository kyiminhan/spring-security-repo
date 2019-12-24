package com.kyiminhan.spring.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.kyiminhan.spring.service.dto.Mail;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendSimpleMessage(final Mail mail) throws MessagingException, IOException {
		final MimeMessage message = this.emailSender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
		        StandardCharsets.UTF_8.name());

		// helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));

		final Context context = new Context();
		context.setVariables(mail.getModel());
		final String html = this.templateEngine.process("email-template", context);

		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		this.emailSender.send(message);
	}
}
