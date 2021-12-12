package com.nkg.pmbot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class AppProperties {

	@Value("${mail.from.id}")
	public String mailFromId;

	@Value("${mail.from.id.pwd}")
	public String mailFromIdPwd;

	@Value("${mail.smtp.host}")
	public String HOST;

	@Value("${mail.smtp.port}")
	public String PORT;

	@Value("${mail.smtp.ssl.enable}")
	public String sslEnable;

	@Value("${mail.smtp.auth}")
	public String smtpAuth;

	@Autowired
	public Environment appProperties;

	public String getMailFromId() {
		return mailFromId;
	}

	public void setMailFromId(String mailFromId) {
		this.mailFromId = mailFromId;
	}

	public String getMailFromIdPwd() {
		return mailFromIdPwd;
	}

	public void setMailFromIdPwd(String mailFromIdPwd) {
		this.mailFromIdPwd = mailFromIdPwd;
	}

	public String getHOST() {
		return HOST;
	}

	public void setHOST(String hOST) {
		HOST = hOST;
	}

	public String getPORT() {
		return PORT;
	}

	public void setPORT(String pORT) {
		PORT = pORT;
	}

	public String getSslEnable() {
		return sslEnable;
	}

	public void setSslEnable(String sslEnable) {
		this.sslEnable = sslEnable;
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public Environment getAppProperties() {
		return appProperties;
	}

	public void setAppProperties(Environment appProperties) {
		this.appProperties = appProperties;
	}
}
