package com.oauth.repository;

import org.springframework.data.repository.CrudRepository;

import com.oauth.model.Client;;

public interface ClientRepository extends CrudRepository<Client, Long> {}
