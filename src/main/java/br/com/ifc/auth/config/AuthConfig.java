package br.com.ifc.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@ConfigurationProperties("auth")
public class AuthConfig {

	private String secret;
	
	private final UserConfig user = new UserConfig();

	public String getSecret() {
		return secret;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public UserConfig getUser() {
		return user;
	}

	/**
	 * User
	 */
	@ConfigurationProperties("user")
	public static class UserConfig {
		
		private String name;
		
		private String password;
				
		private String roles;
		
		public String getName() {
			return name;
		}

		public String getPassword() {
			return password;
		}
		
		public String getRoles() {
			return roles;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setRoles(String roles) {
			this.roles = roles;
		}
	}
}
