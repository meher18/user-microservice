package com.epam.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	public Optional<User> findByUsername(String userName);
	
	public boolean existsByUsername(String userName);

	@Transactional
	public void deleteByUsername(String userName);
}
