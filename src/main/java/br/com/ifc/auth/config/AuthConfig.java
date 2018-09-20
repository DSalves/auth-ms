package br.com.ifc.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@ConfigurationProperties
public class AuthConfig {

	@Value("${auth.secret}")
	private String secret;
	
	@Value("${auth.token.expirationTime}")
	private long expirationTime;
	
	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getSecret() {
		return secret;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}	
}
