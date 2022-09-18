package com.evita.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.evita.repository.UsuarioRepository;

@Path("/")
public class ServicesRest {
	
	
	@Autowired
	  private UsuarioRepository userRepository;
	
	@GET
	Response makeRefreshToken() {
		return null;
		
		
		
	}

}
