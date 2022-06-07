package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.UserDto;
import com.epam.exceptions.UserNotFoundException;
import com.epam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = UserRestController.class)
public class UserRestControllerTest {
	@MockBean
	UserService userService;

	@Autowired
	MockMvc mvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void usersTest() throws Exception {
		List<UserDto> list = new ArrayList<>();
		when(userService.getAll()).thenReturn(list);
		mvc.perform(get("/users")).andExpect(status().isOk());
	}

	@Test
	void oneUserTest() throws Exception {
		when(userService.get(anyString())).thenReturn(new UserDto());
		mvc.perform(get("/users/user1")).andExpect(status().isOk());
	}

	@Test
	void newBookTest() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setId(1);
		userDto.setName("name");
		userDto.setEmail("msr@gmail.com");
		userDto.setUsername("name1");
		String userJson = new Gson().toJson(userDto);
		when(userService.addNewUser(any())).thenReturn(new UserDto());
		mvc.perform(post("/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(userJson)).andExpect(status().isCreated());
	}

	@Test
	void updateBookTest() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setId(1);
		userDto.setName("user");
		userDto.setEmail("msr@gmail.com");
		userDto.setUsername("user1");
		String userJson = new Gson().toJson(userDto);
		when(userService.updateUser(anyString(), any())).thenReturn(new UserDto());
		mvc.perform(put("/users/user1").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(userJson)).andExpect(status().isCreated());
	}

	@Test
	void deleteBookTest() throws Exception {

		mvc.perform(delete("/users/user1")).andExpect(status().isNoContent());
		verify(userService).delete(anyString());
	}
	
	
	@Test
	void testInsertInvalidUser() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setName("");

		when(userService.addNewUser(any())).thenReturn(userDto);
		String userInJson = mapper.writeValueAsString(userDto);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userInJson))
				.andExpect(status().isBadRequest());
	}

	
	
	@Test
	void userNotfound() throws Exception {
	doThrow(UserNotFoundException.class).when(userService).get(anyString());
	mvc.perform(get("/users/{username}", "name")).andExpect(status().isBadRequest());
	}
	
	
	@Test
	void globalExceptionTest() throws Exception {
	doThrow(RuntimeException.class).when(userService).get(anyString());
	mvc.perform(get("/users/{username}", "name")).andExpect(status().isBadRequest());
	}
}
