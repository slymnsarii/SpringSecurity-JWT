package com.tpro.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginRequest {
	
	@NotBlank
	@NotNull
	private String userName;
	
	@NotBlank
	@NotNull
	private String password;


}
