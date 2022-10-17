package com.tpro.service;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tpro.controller.dto.RegisterRequest;
import com.tpro.domain.Role;
import com.tpro.domain.User;
import com.tpro.domain.enums.UserRole;
import com.tpro.exception.ConflictException;
import com.tpro.exception.ResourceNotFoundException;
import com.tpro.repository.RoleRepository;
import com.tpro.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public void registerUser(RegisterRequest request) {
		//kayit formunda girilen userName unique mi?
		if (userRepository.existsByUserName(request.getUserName())) {
			throw new ConflictException("User already registered");
		}
		//create'de otomatik olarak role kismina Student ekliyorum
		Role role=roleRepository.findByName(UserRole.ROLE_STUDENT).orElseThrow(
				()-> new ResourceNotFoundException("Role not found"));
		Set<Role>roles=new HashSet<>();
		roles.add(role);
		User user=new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUserName(request.getUserName());
		
		//role set ediliyor
		user.setRoles(roles);
		//password
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		userRepository.save(user);

	}

}
