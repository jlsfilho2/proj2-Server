package com.oauth.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Client {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String clienId;

	@Column
	private String name;
	
	@Column
	private String callback;
	
	@Column
	private String pass;
	
	
	@OneToMany
	List<Term> terms;
	
	
	
	
	
	
}
