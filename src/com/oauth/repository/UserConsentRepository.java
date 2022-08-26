package com.oauth.repository;

import org.springframework.data.repository.CrudRepository;

import com.oauth.model.UserConsent;

public interface UserConsentRepository extends CrudRepository<UserConsent, Long> {}
