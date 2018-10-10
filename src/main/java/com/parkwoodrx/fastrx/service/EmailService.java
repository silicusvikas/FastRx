package com.parkwoodrx.fastrx.service;

import java.io.File;
import java.text.MessageFormat;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@PropertySource("file:email.properties")
public class EmailService {
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Value("${email.fromAddress}") 
	private String fromAddress;

	@Value("${email.user.content}")
	private String userContent;
	
	@Value("${email.resetPassword.content}")
	private String pwdContent;
	
	@Value("${login.url}")
	private String loginUrl;
	
	@Value("${fastrx.logo}")
	private String fastrxLogo;
	
	
	@Value("${email.billing.content}")
	private String billingContent;

	@Value("${email.subject}")
	private String subject;
	
	@Value("${email.bcc.Address}")
	private String adminEmailAddress;
	
	@Value("${email.cc.Address}")
	private String fastrxEmailAddress;
	
	@Value("${user.registration-content}")
	private String userRegistration;
	

	public void sendUserEmail(final String msg, String username) throws Exception {
		logger.info("Inside EmailService :: sendEmail method");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);
				message.setTo(username);
				message.setFrom(fromAddress);
				message.setSubject(subject);
				String result = MessageFormat.format(userContent, msg,loginUrl,fastrxLogo);
				message.setText(result, true);
				// add logo
				File bannerImage = new File("logo.png");
				FileSystemResource banner = new FileSystemResource(bannerImage);
			    message.addInline("banner", banner);
				
			}
		};
		mailSender.send(preparator);
		logger.info("Sending Email Success");
	}
	
	public void sendPwdResetEmail(final String msg, String username) throws Exception {
		logger.info("Inside EmailService :: sendEmail method");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);
				message.setTo(username);
				message.setFrom(fromAddress);
				message.setSubject(subject);
				String result = MessageFormat.format(pwdContent, msg,loginUrl);
				message.setText(result, true);
				// add logo
				String bannerImage = "logo.png";
				FileSystemResource banner = new FileSystemResource(bannerImage);
			    message.addInline("banner", banner);
				
			}
		};
		mailSender.send(preparator);
		logger.info("Sending Email Success");
	}
	
	public void sendEmailForBiliingReport(final String msg, String username ,String subject) throws Exception {
		logger.info("Inside EmailService :: sendEmailForBiliingReport method");
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);
				message.setTo(username);
				message.setBcc(adminEmailAddress);
				message.setFrom(fromAddress);
				message.setSubject(subject);
				String result = MessageFormat.format(billingContent, msg);
				message.setText(result, true);
				// add logo			
				File bannerImage = new File("logo.png");
				FileSystemResource banner = new FileSystemResource(bannerImage);
			    message.addInline("banner", banner);
			}
		};
		mailSender.send(preparator);
		logger.info("Sending Email Success to "+username);
	}
	
	public void sendUserRegistrationEmail(String username,String corporationname) throws Exception {
		logger.info(" Sending email for user registration ::"+username);
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED);
				message.setTo(fastrxEmailAddress);
				message.setBcc(adminEmailAddress);
				message.setFrom(fromAddress);
				message.setSubject(subject);
				String result = MessageFormat.format(userRegistration, username,corporationname);
				message.setText(result, true);
				// add logo			
					File bannerImage = new File("logo.png");
					FileSystemResource banner = new FileSystemResource(bannerImage);
				    message.addInline("banner", banner);
			}
		};
		mailSender.send(preparator);
		logger.info("User registration email sent successfully to super admin");
	}

}
