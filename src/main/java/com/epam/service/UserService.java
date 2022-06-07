package com.epam.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.dto.UserDto;
import com.epam.entity.User;
import com.epam.exceptions.UserAlreadyPresent;
import com.epam.exceptions.UserNotFoundException;
import com.epam.repository.UserRepository;
import com.epam.util.Constants;

@Component
public class UserService {

	@Autowired
	UserRepository repository;

	@Autowired
	ModelMapper mapper;

	public UserDto get(String username) {

		return mapper.map(getUserFromRepo(username), UserDto.class);
	}

	public List<UserDto> getAll() {
		List<User> users = (List<User>) repository.findAll();

		return mapper.map(users, new TypeToken<List<UserDto>>() {
		}.getType());
	}

	public UserDto addNewUser(UserDto userDto) {

		if (repository.existsById(userDto.getId())) {
			throw new UserAlreadyPresent(Constants.USER_ALREADY_PRESENT);
		} else {
			User userToBeSaved = mapper.map(userDto, User.class);
			return mapper.map(repository.save(userToBeSaved), UserDto.class);
		}
	}

	public UserDto updateUser(String username, UserDto userDto) {
		User userToBeSaved = getUserFromRepo(username);
		userToBeSaved.setName(userDto.getName());
		userToBeSaved.setEmail(userDto.getEmail());
		return mapper.map(repository.save(userToBeSaved), UserDto.class);
	}

	public void delete(String username) {
		getUserFromRepo(username); // or exist by username
		repository.deleteByUsername(username);
	}

	public User getUserFromRepo(String username) {

		return repository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(Constants.INVALID_USER + username));
	}
}
