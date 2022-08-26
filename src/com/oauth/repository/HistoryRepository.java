package com.oauth.repository;

import org.springframework.data.repository.CrudRepository;

import com.oauth.model.HistoryAcess;

public interface HistoryRepository extends CrudRepository<HistoryAcess, Long> {}
