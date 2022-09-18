package com.oauth.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Client {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String clienId;

	@Column(length=100)
	private String name;
	
	@Column(length=120)
	private String callback;
	
	@Column(length=40)
	private String pass;
	
	
	@OneToMany(mappedBy = "client")
	List<Term> terms;
	
	
	
	
	
	
}
