package com.yeong.vege.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeong.vege.model.User;
import com.yeong.vege.repository.UserRepository;

import lombok.AllArgsConstructor;
import static java.util.Collections.singletonList;


@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
	@Override
    @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUsername(username);
		User user =  userOptional.orElseThrow(()-> new UsernameNotFoundException("No User username: " + username));
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(),
				user.isEnabled(), true,true,true, getAuthorities("USER"));
	}
	private Collection<? extends GrantedAuthority> getAuthorities(String role){
		return singletonList(new SimpleGrantedAuthority(role));
	}
	
}
