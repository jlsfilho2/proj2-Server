package com.oauth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;



public class Term {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Version
	private int version;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@OneToOne
	@JoinColumn(name = "id")
	private Term revoc;
	
	@JoinColumn(name = "client_id")
	private Client client;
	
	@Lob
	private String text;
	
	

}
