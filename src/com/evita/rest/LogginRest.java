package com.evita.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.evita.model.Avaliacao;
import com.evita.model.Solicitacao;
import com.evita.model.Usuario;
import com.evita.model.UsuarioEndereco;
import com.evita.repository.AvaliacaoRepository;
import com.evita.repository.UsuarioRepository;

@RestController
@RequestMapping("/loggin")
public class LogginRest {

	Logger logger = Logger.getLogger(LogginRest.class.getName());

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping
	@ResponseBody
	Usuario logar(@RequestBody LogginEntity loggin) {
		logger.log(Level.INFO, "Logar usuario " + loggin);
		Usuario usuario = null;
		try {
			if (!StringUtils.isEmpty(loggin.getEmail()))
				usuario = this.usuarioRepository.findByEmail(loggin.getEmail());
			else if (!StringUtils.isEmpty(loggin.getUserId()))
				usuario = this.usuarioRepository.findByUserId(loggin.getEmail());
			else
				return null;
			if(usuario.getPass().contentEquals(loggin.getSenha()))
				return usuario;
			else return null;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	class LogginEntity {

		String email;

		String senha;

		String userId;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		@Override
		public String toString() {
			return "LogginEntity [email=" + email + ", senha=" + senha + ", userId=" + userId + "]";
		}

	}

}
