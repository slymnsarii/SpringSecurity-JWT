package com.tpro.controller;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tpro.controller.dto.LoginRequest;
import com.tpro.controller.dto.RegisterRequest;
import com.tpro.repository.RoleRepository;
import com.tpro.security.JwtUtils;
import com.tpro.service.UserService;
import lombok.AllArgsConstructor;
@RestController
@RequestMapping
@AllArgsConstructor
public class UserJWTController {
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	//********* REGISTER***************
	@PostMapping("/register")
	public ResponseEntity<Map<String,String>> registerUser (@Valid @RequestBody RegisterRequest request ) {
		userservice.registerUser(request);
		
		Map<String,String> map = new HashMap<>();
		map.put("message",	" User registered successfuly");
		map.put("status", "true");
		return new ResponseEntity<>(map,HttpStatus.CREATED);
		
	}
//*******************************************
	
// ****************LOGIN********************
@PostMapping("/login")
public ResponseEntity<Map<String,String>> login(@Valid @RequestBody LoginRequest request){
	//kullanıcı kontrol edilecek
	Authentication  authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
	// JWT token üretilecek
	String token = jwtUtils.generateToken(authentication);
	
	Map<String,String> map = new HashMap<>();
	map.put("token",	token);
	map.put("status", "true");
	return new ResponseEntity<>(map,HttpStatus.CREATED);
	
}
}