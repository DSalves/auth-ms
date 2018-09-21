package br.com.ifc.auth.service;

import org.springframework.stereotype.Service;

import br.com.ifc.auth.model.User;
import br.com.ifc.auth.model.UserPrincipal;
import br.com.ifc.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
		
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
    private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findById(username)
							 .map(user -> new UserPrincipal(user))
							 .orElseThrow(() -> new UsernameNotFoundException("User "+ username + " not found"));
			
	}
	
	/**
	 * 
	 * @param user
	 */
	public void save(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	/**
	 * 
	 * @return
	 */
	public PasswordEncoder getPasswordEncoder() {
		return encoder;
	}
}
