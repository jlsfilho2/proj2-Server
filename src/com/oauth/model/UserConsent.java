package com.oauth.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

public class UserConsent {
	
	@JoinColumn(name = "user_id")
	User user;
	
	@JoinColumn(name = "client_id")
	Client client;
	
	@Column
	String clain;

}
