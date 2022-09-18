package com.oauth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class HistoryAcess {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	User user;
	
	Client client;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date emit;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date lastRefresh;
	
	@Column
	long timeToLive;
	
	@Column
	String codeChalange;
	
	@Column
	String access;
	
	@Column
	String refresh;
	
	

}
