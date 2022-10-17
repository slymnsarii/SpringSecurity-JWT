package com.tpro.controller.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Setter;

@Data //icinde getter, setter, toString vs hepsi var
public class RegisterRequest {

	@NotNull
	@NotBlank
	@Size(min = 1, max = 15, message = "First name '${validatedValue}' must be between {min} and {max} chars long")
	private String firstName;
	
	@NotNull
	@NotBlank
	@Size(min = 1, max = 15, message = "Last name '${validatedValue}' must be between {min} and {max} chars long")
	private String lastName;

	@NotNull
	@NotBlank
	@Size(min = 1, max = 20, message = "User name '${validatedValue}' must be between {min} and {max} chars long")
	private String userName;
	
	@NotNull
	@NotBlank
	@Size(min = 5, max = 20, message = "Password '${validatedValue}' must be between {min} and {max} chars long")
	private String password;
	
	private Set<String> roles;


}
