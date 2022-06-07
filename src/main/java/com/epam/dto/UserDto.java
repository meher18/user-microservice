package com.epam.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto {

	int id;

	String username;

	@NotEmpty(message = "name cannot be empty")
	String name;

	@Email(message = "please enter valid email")
	@NotEmpty
	String email;

}
