package com.evita.infra;

import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.evita.repository.UsuarioRepository;

@Configuration
class LoadDatabase {

	private static Logger logger = Logger.getLogger(LoadDatabase.class.getSimpleName());

  @Bean
  CommandLineRunner initDatabase(UsuarioRepository repository) {

    return args -> {
    	logger.info("iniciando Repository ");
    };
  }
}