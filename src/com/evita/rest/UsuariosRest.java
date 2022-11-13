package com.evita.rest;

import java.util.ArrayList;
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
			@RequestParam(required = false) String cidade, @RequestParam(required = false) String bairro
			,@RequestParam(required = false, defaultValue="CLIENTE_PRESTADOR") TIPO tipo) {
		logger.log(Level.INFO, "buscar usuários " + tipo);
		boolean comCategoria = categoria != null;
		boolean comEndereco = uf != null
				|| (!StringUtils.isEmpty(cidade) && !cidade.contentEquals("null"))
				|| (!StringUtils.isEmpty(bairro) && !bairro.contentEquals("null"));
		try {
			Set<Long> ids = new HashSet<>();
			Set<Long> idsEndereco = new HashSet<>();
			if(tipo== TIPO.CLIENTE) {
				logger.log(Level.INFO,"somente Clientes");
				List<UsuarioCategoria> usuariosCategoria = userCategoriaRepository.findAll();
				for (UsuarioCategoria cat : usuariosCategoria)
					ids.add(cat.getUser().getId());
			}
			if(tipo== TIPO.PRESTADOR) {
				logger.log(Level.INFO,"somente Prestadores");
				List<UsuarioCategoria> usuariosCategoria = userCategoriaRepository.findAll();
				for (UsuarioCategoria cat : usuariosCategoria)
					ids.add(cat.getUser().getId());
			}
			if (tipo!= TIPO.CLIENTE && comCategoria) {
				logger.log(Level.INFO,"com categoria");
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
				if (!StringUtils.isEmpty(cidade)&& !cidade.contentEquals("null"))
					usuarioEndereco.setCidade(cidade);
				if (!StringUtils.isEmpty(bairro)&& !bairro.contentEquals("null"))
					usuarioEndereco.setBairro(bairro);
				List<UsuarioEndereco> usuariosEndereco = userEnderecoRepository.findAll(Example.of(usuarioEndereco));
				logger.log(Level.INFO,
						"encontrados " + usuariosEndereco.size() + " usuários com a endereco " + usuarioEndereco);
				for (UsuarioEndereco end : usuariosEndereco)
					idsEndereco.add(end.getUser().getId());
				
			}
			List<Usuario> usuarios = new ArrayList<>();
			if(tipo== TIPO.CLIENTE) {
				logger.log(Level.INFO,"listando clientes fora da lista");
				usuarios = userRepository.findAllByIdNotIn(ids);
			} else if(tipo== TIPO.PRESTADOR && !comCategoria && !comEndereco) {
				logger.log(Level.INFO,"listando prestadores na lista");
				usuarios = userRepository.findAllByIdIn(ids);
			}else if(tipo!= TIPO.CLIENTE && comCategoria && comEndereco) {
				logger.log(Level.INFO,"com categoria e com endereço");
				ids = ids.stream().filter(element -> idsEndereco.contains(element)).collect(Collectors.toSet());
				logger.log(Level.INFO,"restam ids " + ids.size());
				usuarios = userRepository.findAllByIdIn(ids);
			}
			else if(comEndereco) {
				ids = idsEndereco;
				logger.log(Level.INFO,"com endereço");
				usuarios = userRepository.findAllByIdIn(ids);
			}
			else if(tipo!= TIPO.CLIENTE && comCategoria)
			{
				logger.log(Level.INFO,"com categoria");
				usuarios = userRepository.findAllByIdIn(ids);
			} else 
				usuarios = userRepository.findAll();
			logger.log(Level.INFO, "retornando " + usuarios.size() + " usuários");
			return usuarios;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(),ex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}
	
	enum TIPO {
		CLIENTE,CLIENTE_PRESTADOR,PRESTADOR
	}

}
