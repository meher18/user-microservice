package com.epam.restcontroller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.UserDto;
import com.epam.service.UserService;


@RestController
public class UserRestController {

	@Autowired
	UserService service;

	@GetMapping("/users/{username}")
	public ResponseEntity<UserDto> getUser(@PathVariable(value = "username") String username) {
		return new ResponseEntity<>(service.get(username), HttpStatus.OK);
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getAll() {
		return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<UserDto> newUser(@RequestBody @Valid UserDto userDto) {
		return new ResponseEntity<>(service.addNewUser(userDto), HttpStatus.CREATED);
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<UserDto> update(@PathVariable(value = "username") String username, @RequestBody @Valid UserDto userDTO) {
		return new ResponseEntity<>(service.updateUser(username, userDTO), HttpStatus.CREATED);
	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<String> delete(@PathVariable String username) {
		service.delete(username);
		return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
	}
}
