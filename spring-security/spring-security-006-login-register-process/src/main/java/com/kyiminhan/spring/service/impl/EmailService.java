package com.kyiminhan.spring.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.kyiminhan.spring.entity.PasswordRequest;
import com.kyiminhan.spring.entity.RegisteredAccount;
import com.kyiminhan.spring.service.dto.Mail;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Value("${spring.mail.username}")
	private String senderEmil;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	private void sendSimpleMessage(final Mail mail, final String template) throws MessagingException, IOException {

		final MimeMessage message = this.emailSender.createMimeMessage();
		final MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
		        StandardCharsets.UTF_8.name());

		// helper.addAttachment("logo.png", new
		// ClassPathResource("memorynotfound-logo.png"));

		final Context context = new Context();
		context.setVariables(mail.getModel());
		final String emailTemplate = String.join("/", "email", template);
		final String html = this.templateEngine.process(emailTemplate, context);

		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		this.emailSender.send(message);
	}

	private String getServerNameAndPort() {
		final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		final HttpServletRequest req = ((ServletRequestAttributes) attributes).getRequest();
		final String scheme = req.getScheme();
		final String serverName = req.getServerName();
		final String port = String.valueOf(req.getServerPort());
		return String.join("", scheme, "://", serverName, ":", port);
	}

	public void sendRegisterationEmail(final RegisteredAccount regAcc) throws MessagingException, IOException {

		EmailService.log.info("Sending Registration Email.......");

		final Mail mail = new Mail();
		mail.setFrom(this.senderEmil);
		mail.setTo(regAcc.getEmail());
		mail.setSubject("Registered confirmation and activation");

		final Map<String, Object> model = new HashMap<>();
		model.put("name", String.join(" ", regAcc.getFirstName(), regAcc.getLastName()));

		final String activationURL = String.join("", this.getServerNameAndPort(), "/confirm-registration", "/",
		        regAcc.getUuid());

		model.put("activationURL", activationURL);

		model.put("signature", "Best Regards");
		model.put("location", "Law Consulting Co., Ltd..");
		mail.setModel(model);

		this.sendSimpleMessage(mail, "email-registration");
	}

	public void sendForgotPasswordResponseEmail(final PasswordRequest pwdRequest)
	        throws MessagingException, IOException {

		final Mail mail = new Mail();
		mail.setFrom(this.senderEmil);
		mail.setTo(pwdRequest.getEmail());
		mail.setSubject("About Forgot password reset");

		final Map<String, Object> model = new HashMap<>();

		final String resetPasswordURL = String.join("", this.getServerNameAndPort(), "/forgot-pwd-reset", "/",
		        pwdRequest.getUuid());

		model.put("resetPasswordURL", resetPasswordURL);

		model.put("signature", "Best Regards");
		model.put("location", "Law Consulting Co., Ltd..");
		mail.setModel(model);

		this.sendSimpleMessage(mail, "email-fogot-pwd-request");

	}
}