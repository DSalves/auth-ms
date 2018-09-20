package br.com.ifc.auth.service;

import org.springframework.stereotype.Service;

import br.com.ifc.auth.model.UserPrincipal;
import br.com.ifc.auth.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {
	
	@Autowired
    private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findById(username)
							 .map(user -> new UserPrincipal(user))
							 .orElseThrow(() -> new UsernameNotFoundException("User "+ username + " not found"));
			
	}
}
