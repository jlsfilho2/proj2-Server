package com.oauth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class User extends AbstractPersistable<Long> {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	String name;
	
	@Column
	String userId;
	
	@Column
	String pass;
	
	

}
