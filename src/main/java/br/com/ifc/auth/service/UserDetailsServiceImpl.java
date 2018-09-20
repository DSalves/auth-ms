package br.com.ifc.auth.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		return new UserDetails() {			

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public String getUsername() {
				// TODO Auto-generated method stub
				return "admin";
			}
			
			@Override
			public String getPassword() {
				// TODO Auto-generated method stub
				return "$2a$10$48YgvczxEt1TVxaThV2s/e878DMPycw/esiGR4KhAUXMP69itq32i";
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				ArrayList<GrantedAuthority> list = new ArrayList<>();
				list.add(new SimpleGrantedAuthority("ADMIN"));
				list.add(new SimpleGrantedAuthority("STANDARD_USER"));
				return list;
			}
		};
	}
}
