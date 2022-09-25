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
			userToEdit.setNome(usuario.getNome());
			userToEdit.setPass(usuario.getPass());
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
