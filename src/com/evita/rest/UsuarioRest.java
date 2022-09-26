package com.evita.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.evita.model.Solicitacao;
import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;
import com.evita.model.UsuarioEndereco;
import com.evita.repository.UsuarioCategoriaRepository;
import com.evita.repository.UsuarioEnderecoRepository;
import com.evita.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuario")
public class UsuarioRest {

	Logger logger = Logger.getLogger(UsuarioRest.class.getName());

	@Autowired
	private UsuarioRepository userRepository;

	@Autowired
	private UsuarioEnderecoRepository userEnderecoRepository;
	
	@Autowired
	private UsuarioCategoriaRepository userCategoriaRepository;

	@PostMapping
	@ResponseBody
	Usuario criar(@RequestBody Usuario usuario) {
		logger.log(Level.INFO, "criar usuário " + usuario);
		try {
			Usuario newUser = this.userRepository.saveAndFlush(usuario);
			if (usuario.getEnderecos() != null && !usuario.getEnderecos().isEmpty()) {
				List<UsuarioEndereco> enderecosUsuario = userEnderecoRepository.findByUser(usuario);
				for (UsuarioEndereco endereco : usuario.getEnderecos()) {
					if (!enderecosUsuario.contains(endereco)) {
						endereco.setUser(newUser);
						userEnderecoRepository.saveAndFlush(endereco);
					}
				}
			}
			if (usuario.getCategorias() != null && !usuario.getCategorias().isEmpty()) {
				List<UsuarioCategoria> categoriasUsuario = userCategoriaRepository.findByUser(usuario);
				for (UsuarioCategoria categoria : usuario.getCategorias()) {
					if (!categoriasUsuario.contains(categoria)) {
						categoria.setUser(newUser);
						userCategoriaRepository.saveAndFlush(categoria);
					}
				}
			}
			
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
			if(!StringUtils.isEmpty(usuario.getNome()))
			userToEdit.setNome(usuario.getNome());
			if(!StringUtils.isEmpty(usuario.getEmail()))
			userToEdit.setEmail(usuario.getEmail());
			if(!StringUtils.isEmpty(usuario.getPass()))
			userToEdit.setPass(usuario.getPass());
			if(!StringUtils.isEmpty(usuario.getTipo()))
			userToEdit.setTipo(usuario.getTipo());
			this.userRepository.saveAndFlush(userToEdit);
			if(usuario.getEnderecos() != null && !usuario.getEnderecos().isEmpty()) {
				List<UsuarioEndereco> enderecosUsuario = userEnderecoRepository.findByUser(usuario);
				for(UsuarioEndereco endereco : usuario.getEnderecos()) {
					if(!enderecosUsuario.contains(endereco))
						userEnderecoRepository.saveAndFlush(endereco);
					else if(endereco.getId() != null)
					{
						UsuarioEndereco enderecoEdit = enderecosUsuario.get(enderecosUsuario.indexOf(endereco));
						enderecoEdit.setNumero(endereco.getNumero());
						enderecoEdit.setBairro(endereco.getBairro());
						enderecoEdit.setComplemento(endereco.getComplemento());
						enderecoEdit.setCep(endereco.getCep());
						enderecoEdit.setCidade(endereco.getCidade());
						enderecoEdit.setUf(endereco.getUf());
						userEnderecoRepository.saveAndFlush(enderecoEdit);
					}
				}
			}
			
			if(usuario.getCategorias() != null && !usuario.getCategorias().isEmpty()) {
				List<UsuarioCategoria> categoriasUsuario = userCategoriaRepository.findByUser(usuario);
				for(UsuarioCategoria categoria : usuario.getCategorias()) {
					if(!categoriasUsuario.contains(categoria))
						userCategoriaRepository.saveAndFlush(categoria);
					else if(categoria.getId() != null)
					{
						UsuarioCategoria categoriaEdit = categoriasUsuario.get(categoriasUsuario.indexOf(categoria));
						categoriaEdit.setValor(categoria.getValor());
						userCategoriaRepository.saveAndFlush(categoriaEdit);
					}
				}
			}
			return userToEdit;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@GetMapping
	@ResponseBody
	Usuario buscar(@RequestParam String userId, @RequestParam String email) {
		logger.log(Level.INFO, "buscar usuários");
		Usuario usuario = null;
		try {
			if(!StringUtils.isEmpty(userId))
			usuario = this.userRepository.findByUserId(userId);
			else if(!StringUtils.isEmpty(email))
				usuario = this.userRepository.findByEmail(email);
			else return null;
			logger.log(Level.INFO, "retornando usuário");
			return usuario;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}
	
	private void validate(Solicitacao solicitacao) {
		logger.log(Level.INFO, "validando solicitação " + solicitacao);
		Validator validator = Validation.buildDefaultValidatorFactory()
	            .getValidator();
		StringBuilder sb = new StringBuilder();
		validator.validate(solicitacao)
        .stream()
        .forEach(violation -> sb.append(violation.getMessage()).append(";"));
		
		if(sb.length() > 0)
			throw new RuntimeException(sb.toString());
		
	}

}
