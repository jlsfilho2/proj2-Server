package com.evita.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.evita.model.UsuarioCategoria.Categoria;
import com.evita.model.UsuarioEndereco;
import com.evita.model.UsuarioEndereco.UF;
import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;
import com.evita.repository.UsuarioCategoriaRepository;
import com.evita.repository.UsuarioEnderecoRepository;
import com.evita.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuariosRest {

	Logger logger = Logger.getLogger(UsuariosRest.class.getName());

	@Autowired
	private UsuarioCategoriaRepository userCategoriaRepository;

	@Autowired
	private UsuarioEnderecoRepository userEnderecoRepository;

	@Autowired
	private UsuarioRepository userRepository;

	@GetMapping
	@ResponseBody
	List<Usuario> buscar(@RequestParam(required = false) Categoria categoria, @RequestParam(required = false) UF uf,
			@RequestParam(required = false) String cidade, @RequestParam(required = false) String bairro) {
		logger.log(Level.INFO, "buscar usuários");
		boolean comCategoria = categoria != null;
		boolean comEndereco = uf != null
				|| (StringUtils.isEmpty(cidade) && !cidade.contentEquals("null"))
				|| (StringUtils.isEmpty(bairro) && !cidade.contentEquals("null"));
		try {
			Set<Long> ids = new HashSet<>();
			Set<Long> idsEndereco = new HashSet<>();
			if (comCategoria) {
				List<UsuarioCategoria> usuariosCategoria = userCategoriaRepository.findByCategoria(categoria);
				logger.log(Level.INFO,
						"encontrados " + usuariosCategoria.size() + " usuários com a categoria " + categoria);
				for (UsuarioCategoria cat : usuariosCategoria)
					ids.add(cat.getUser().getId());
			}
			if (comEndereco) {
				UsuarioEndereco usuarioEndereco = new UsuarioEndereco();
				
				if (uf != null)
					usuarioEndereco.setUf(uf);
				if (!StringUtils.isEmpty(cidade))
					usuarioEndereco.setCidade(cidade);
				if (!StringUtils.isEmpty(bairro))
					usuarioEndereco.setBairro(bairro);
				List<UsuarioEndereco> usuariosEndereco = userEnderecoRepository.findAll(Example.of(usuarioEndereco));
				logger.log(Level.INFO,
						"encontrados " + usuariosEndereco.size() + " usuários com a endereco " + usuarioEndereco);
				for (UsuarioEndereco end : usuariosEndereco)
					idsEndereco.add(end.getUser().getId());
				
			}
			if(comCategoria && comEndereco)
				ids = ids.stream().filter(element -> idsEndereco.contains(element)).collect(Collectors.toSet());
			else ids = idsEndereco;
			List<Usuario> usuarios = userRepository.findAllByIdIn(ids);
			logger.log(Level.INFO, "retornando " + usuarios.size() + " usuários");
			return usuarios;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

}
