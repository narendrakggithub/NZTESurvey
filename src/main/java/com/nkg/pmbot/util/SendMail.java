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
import org.springframework.context.annotation.Configuration;

import com.nkg.pmbot.controller.AuthController;
import com.nkg.pmbot.exception.EmailException;


@Configuration
public class SendMail {
	
	@Autowired
	public AppProperties appProperties;

	private final Logger logger = LoggerFactory.getLogger(SendMail.class);

	public void sendEmail(String to, String subject, String messageBody, boolean isHtml, File file)
			throws EmailException {
		
		Properties properties = System.getProperties();
		
		logger.debug("Email sending..HOST:" + appProperties.getHOST());

		logger.debug("Email sending.." + appProperties.getHOST());

		// Setup mail server
		properties.put("mail.smtp.host", appProperties.getHOST());
		properties.put("mail.smtp.port", appProperties.getPORT());
		properties.put("mail.smtp.ssl.enable", appProperties.getSslEnable());
		properties.put("mail.smtp.auth", appProperties.getSmtpAuth());

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(appProperties.getMailFromId(), appProperties.getMailFromIdPwd());

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(appProperties.getMailFromId()));

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
	
	public void sendEmail1(String to, String subject, String messageBody, boolean isHtml, File file)
			throws EmailException {
		
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("narendrakgpipra@gmail.com", "India@135");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress("narendrakgpipra@gmail.com"));

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

	public  void setMessage(MimeMessage message, String messageBody) throws MessagingException {
		// Now set the actual message
		message.setText(messageBody);
	}

	public  void setHtmlMessage(MimeMessage message, String messageBody) throws MessagingException {
		// Now set the actual message
		message.setContent("<h1>" + messageBody + "</h1>", "text/html");
	}

	public  void setMessageWithAttachment(MimeMessage message, String messageBody, File file) throws Exception {
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
	
//	public static void main(String args []) {
//		SendMail sendMail = new SendMail();
//		String validateLink = "https://localhost:3030/validate?id=" + "test";
//		sendMail.sendEmail1("narendrakg@gmail.com", "PMBot: email validation",
//				"please click link below to validate email <BR><BR> {0} <BR><BR> Thanks, <BR> PMBot Team".replace("{0}", validateLink), true, null);
//		
//	}

}
