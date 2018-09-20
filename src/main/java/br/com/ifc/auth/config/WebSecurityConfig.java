package br.com.ifc.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.ifc.auth.filter.JWTAuthenticationFilter;
import br.com.ifc.auth.filter.JWTTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static PasswordEncoder encoder;

	@Autowired
	private JWTAuthenticationFilter authenticationFilter;

	@Autowired
	private AuthConfig config;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests().anyRequest().authenticated().and()
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(getAbstractAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(getPasswordEncoder()).withUser(config.getUser().getName())
				.password(config.getUser().getPassword()).roles(config.getUser().getRoles());
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		if (encoder == null) {
			encoder = new BCryptPasswordEncoder();
		}
		return encoder;
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Bean
	public AbstractAuthenticationProcessingFilter getAbstractAuthenticationProcessingFilter() throws Exception {
		return new JWTTokenFilter("/token", authenticationManager());
	}
}