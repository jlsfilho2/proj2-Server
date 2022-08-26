package com.oauth.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.oauth.repository.UserRepository;

@Path("/")
public class ServicesRest {
	
	
	@Autowired
	  private UserRepository userRepository;
	
	@GET
	Response makeRefreshToken() {
		return null;
		
		
		
	}

}
