package com.evita.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.evita.model.Usuario;
import com.evita.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioRest {

	Logger logger = Logger.getLogger(UsuarioRest.class.getName());

	@Autowired
	private UsuarioRepository userRepository;

	@PostMapping
	@ResponseBody
	Usuario criar(  @RequestBody Usuario usuario) {
		logger.log(Level.INFO, "criar usuário " + usuario);
		try {
			Usuario newUser = this.userRepository.saveAndFlush(usuario);
			return newUser;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@PutMapping
	@ResponseBody
	Usuario editar(@RequestBody Usuario usuario) {
		logger.log(Level.INFO, "editar usuário");
		try {
			Usuario userToEdit = this.userRepository.getById(usuario.getId());
			userToEdit.setNome(usuario.getNome());
			userToEdit.setPass(usuario.getPass());
			userToEdit.setTipo(usuario.getTipo());
			this.userRepository.saveAndFlush(userToEdit);
			return userToEdit;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@GetMapping
	@ResponseBody
	List<Usuario> buscar(@RequestParam String userId) {
		logger.log(Level.INFO, "buscar usuários");
		try {
			List<Usuario> usuarios = this.userRepository.findByUserId(userId);
			logger.log(Level.INFO, "retornando " + usuarios.size() + " usuários");
			return usuarios;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return new ArrayList<Usuario>();
		}

	}

}
