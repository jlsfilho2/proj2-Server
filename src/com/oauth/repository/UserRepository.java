package com.oauth.repository;

import org.springframework.data.repository.CrudRepository;

import com.oauth.model.User;

public interface UserRepository extends CrudRepository<User, Long> {}
