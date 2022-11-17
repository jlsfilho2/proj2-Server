package com.evita.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.evita.model.UsuarioCategoria.Categoria;
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

	@PostMapping(consumes = {"application/xml","application/json"})
	@ResponseBody
	Usuario criar(@RequestBody Usuario usuario) {
		logger.log(Level.INFO, "criar usuário " + usuario);
		validate(usuario);
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
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@PutMapping(consumes = {"application/xml","application/json"})
	@ResponseBody
	Usuario editar(@RequestBody Usuario usuario) {
		logger.log(Level.INFO, "editar usuário");
		try {
			Usuario userToEdit = this.userRepository.getById(usuario.getId());
			logger.log(Level.INFO, "usuario " + userToEdit);
			if (!StringUtils.isEmpty(usuario.getNome()))
				userToEdit.setNome(usuario.getNome());
			if (!StringUtils.isEmpty(usuario.getEmail()))
				userToEdit.setEmail(usuario.getEmail());
			if (!StringUtils.isEmpty(usuario.getPass()))
				userToEdit.setPass(usuario.getPass());
			if (!StringUtils.isEmpty(usuario.getCliente()))
				userToEdit.setCliente(usuario.getCliente());
			if (!StringUtils.isEmpty(usuario.getTelefone()))
				userToEdit.setTelefone(usuario.getTelefone());
			this.userRepository.saveAndFlush(userToEdit);
			if (usuario.getEnderecos() != null && !usuario.getEnderecos().isEmpty()) {
				logger.log(Level.FINE, "Editando endereço usuário com endereços");
				List<UsuarioEndereco> enderecosUsuario = userEnderecoRepository.findByUser(usuario);
				for (UsuarioEndereco endereco : usuario.getEnderecos()) {
					if (!enderecosUsuario.contains(endereco)) {
						endereco.setUser(userToEdit);
						endereco.setAtivo(false);
						logger.log(Level.FINE, "Salvando endereço " + endereco);
						userEnderecoRepository.saveAndFlush(endereco);
					}
					else if (endereco.getId() != null) {
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
			} else if (usuario.getEnderecos() != null && usuario.getEnderecos().isEmpty()) {
				logger.log(Level.FINE, "Editando endereço usuário sem endereços");
				List<UsuarioEndereco> enderecosUsuario = userEnderecoRepository.findByUser(usuario);
				for (UsuarioEndereco endereco : enderecosUsuario) {
					endereco.setUser(userToEdit);
					endereco.setAtivo(false);
					logger.log(Level.INFO, "Salvando endereço " + endereco);
					userEnderecoRepository.saveAndFlush(endereco);
				}

				if (usuario.getCategorias() != null && !usuario.getCategorias().isEmpty()) {
					List<UsuarioCategoria> categoriasUsuario = userCategoriaRepository.findByUser(usuario);
					for (UsuarioCategoria categoria : usuario.getCategorias()) {
						if (!categoriasUsuario.contains(categoria)) {
							categoria.setUser(userToEdit);
							userCategoriaRepository.saveAndFlush(categoria);
						} else if (categoria.getId() != null) {
							UsuarioCategoria categoriaEdit = categoriasUsuario
									.get(categoriasUsuario.indexOf(categoria));
							categoriaEdit.setValor(categoria.getValor());
							userCategoriaRepository.saveAndFlush(categoriaEdit);
						}
					}
				} else if (usuario.getCategorias() != null && usuario.getCategorias().isEmpty()) {
					List<UsuarioCategoria> categoriasUsuario = userCategoriaRepository.findByUser(usuario);
					userCategoriaRepository.deleteAll(categoriasUsuario);
				}
			}
			return userToEdit;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@GetMapping
	@ResponseBody
	Usuario buscar(@RequestParam(required = false) String id, @RequestParam(required = false) String userId, @RequestParam(required = false) String email) {
		logger.log(Level.INFO, "buscar usuários userId:" + userId + ", email:" + email + ", id: " + id);
		Usuario usuario = null;
		try {
			if (!StringUtils.isEmpty(userId) &&!userId.contentEquals("null"))
				usuario = this.userRepository.findByUserId(userId);
			else if (!StringUtils.isEmpty(email) && !email.contentEquals("null"))
				usuario = this.userRepository.findByEmail(email);
			else if (!StringUtils.isEmpty(id)&& !id.contentEquals("null"))
				usuario = this.userRepository.getById(Long.valueOf(id));
			else 
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sem parâmetros para busca");
			if (usuario == null)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nenhum usuário encontrado");
			logger.log(Level.INFO, "retornando usuário");
			return usuario;
		} catch (ResponseStatusException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}

	private void validate(Usuario usuario) {
		logger.log(Level.INFO, "validando solicitação " + usuario);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		StringBuilder sb = new StringBuilder();
		validator.validate(usuario).stream().forEach(violation -> sb.append(violation.getMessage()).append(";"));

		if (sb.length() > 0) {
			logger.log(Level.SEVERE, sb.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString());
		}

	}

}
