package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.epam.dto.UserDto;
import com.epam.entity.User;
import com.epam.exceptions.UserNotFoundException;
import com.epam.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService userService;

	@Mock
	UserRepository repository;

	@Autowired
	ModelMapper mapper;

	@BeforeEach
	void setUp() {
		userService.mapper = mapper;
		userService.repository = repository;
	}

	@Test
	void get() {
		Optional<User> user = Optional.ofNullable(new User());
		when(repository.findByUsername(anyString())).thenReturn(user);
		assertNotNull(userService.get("user1"));
	}

	@Test
	void getAllTest() {
		List<User> users = new ArrayList<>();
		users.add(new User());
		when(repository.findAll()).thenReturn(users);
		assertEquals(1, userService.getAll().size());
	}

	@Test
	void addNewUserTest() {

		UserDto userDto = new UserDto();
		Optional<User> user = Optional.ofNullable(new User());
		when(repository.save(any())).thenReturn(user.get());
		assertNotNull(userService.addNewUser(userDto));
	}

	@Test
	void updateUserTest() {
		UserDto userDto = new UserDto();
		Optional<User> user = Optional.ofNullable(new User());
		when(repository.findByUsername(anyString())).thenReturn(user);
		when(repository.save(any())).thenReturn(user.get());
		assertNotNull(userService.updateUser("user1", userDto));
	}

	@Test
	void deleteTest() {
		when(repository.existsByUsername(anyString())).thenReturn(true);
		userService.delete("user1");
		verify(repository, times(1)).deleteByUsername(anyString());
	}

	@Test
	void deleteUserForInvalidUsernameTest() {

		when(repository.existsByUsername(anyString())).thenReturn(false);

		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete("user1"),
				"User with user1 not found");
		assertEquals("User with user1 not found", exception.getMessage());
	}

	@Test
	void getUserForInvalidUserNameCheck() {

		when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		try {
			userService.getUserFromRepo("user1");
		} catch (Exception e) {
			assertEquals("User with user1 not found", e.getMessage());
		}
		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> userService.getUserFromRepo("user1"), "User with user1 found");
		assertEquals("User with user1 not found", exception.getMessage());
	}
}
