package com.tpro.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tpro.domain.User;
import com.tpro.exception.ResourceNotFoundException;
import com.tpro.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private UserRepository userRepository;

	
	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUserName(username).orElseThrow(()-> 
						new ResourceNotFoundException("User not found with username : "+ username));
		return UserDetailsImpl.build(user);
	}

}
