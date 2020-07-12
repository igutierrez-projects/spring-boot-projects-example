package com.org.googleouth2jwtresourseserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.org.googleouth2jwtresourseserver.entity.User;

public interface UserRepository  extends CrudRepository<User, Integer>{
	
	Optional<User> findById(Integer id);

}
