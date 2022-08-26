package com.oauth.model;

import javax.persistence.Column;

public class UserConsent {
	
	User user;
	
	Client client;
	
	@Column
	String clain;

}
