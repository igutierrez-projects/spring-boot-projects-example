package com.org.googleouth2jwtresourseserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("oauth")
public class OAuthProperties {
	
	
	private String clientId;	
	private String clientSecret;	
	private String checkTokenUrl;	
	private String userInfoUrl;	
	private String appTokenURI;	
	private String secretKey;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getCheckTokenUrl() {
		return checkTokenUrl;
	}

	public void setCheckTokenUrl(String checkTockeUrl) {
		this.checkTokenUrl = checkTockeUrl;
	}

	public String getUserInfoUrl() {
		return userInfoUrl;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

	public String getAppTokenURI() {
		return appTokenURI;
	}

	public void setAppTokenURI(String appTokenURI) {
		this.appTokenURI = appTokenURI;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}	
	
}
