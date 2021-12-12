package com.nkg.pmbot.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import com.nkg.pmbot.controller.AuthController;
import com.nkg.pmbot.exception.EmailException;

public class SendMail {

	@Value("${mail.from.id}")
	private static String mailFromId;

	@Value("${mail.from.id.pwd}")
	private static String mailFromIdPwd;

	@Value("${mail.smtp.host}")
	private static String HOST;

	@Value("${mail.smtp.port}")
	private static String PORT;

	@Value("${mail.smtp.ssl.enable}")
	private static String sslEnable;

	@Value("${mail.smtp.auth}")
	private static String smtpAuth;

	@Autowired
	private static Environment appProperties;

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	public static void sendEmail(String to, String subject, String messageBody, boolean isHtml, File file)
			throws EmailException {
		Properties properties = System.getProperties();

		logger.debug("Email sending..HOST:" + HOST);

		logger.debug("Email sending.." + appProperties.getProperty("mail.smtp.host"));

		// Setup mail server
		properties.put("mail.smtp.host", appProperties.getProperty("mail.smtp.host"));
		properties.put("mail.smtp.port", appProperties.getProperty("mail.smtp.port"));
		properties.put("mail.smtp.ssl.enable", appProperties.getProperty("mail.smtp.ssl.enable"));
		properties.put("mail.smtp.auth", appProperties.getProperty("mail.smtp.auth"));

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(mailFromId, mailFromIdPwd);

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(mailFromId));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(subject);

			if (file == null) {
				if (isHtml) {
					setHtmlMessage(message, messageBody);
				} else {
					setMessage(message, messageBody);
				}
			} else {
				setMessageWithAttachment(message, messageBody, file);
			}

			logger.debug("Email sending..");
			// Send message
			Transport.send(message);
			logger.debug("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			throw new EmailException("Exception while sending email.", mex);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EmailException("Exception while sending email.", ex);
		}

	}

	public static void setMessage(MimeMessage message, String messageBody) throws MessagingException {
		// Now set the actual message
		message.setText(messageBody);
	}

	public static void setHtmlMessage(MimeMessage message, String messageBody) throws MessagingException {
		// Now set the actual message
		message.setContent("<h1>" + messageBody + "</h1>", "text/html");
	}

	public static void setMessageWithAttachment(MimeMessage message, String messageBody, File file) throws Exception {
		Multipart multipart = new MimeMultipart();

		MimeBodyPart attachmentPart = new MimeBodyPart();

		MimeBodyPart textPart = new MimeBodyPart();

		try {
			attachmentPart.attachFile(file);
			textPart.setText(messageBody);
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(attachmentPart);

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		message.setContent(multipart);
	}

}
