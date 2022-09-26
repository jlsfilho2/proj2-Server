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
	Avaliacao criar(@RequestBody Avaliacao avaliacao) {
		logger.log(Level.INFO, "criar avaliação " + avaliacao);
		validate(avaliacao);
		try {
			Avaliacao newAvaliacao = this.avaliacaoRepository.saveAndFlush(avaliacao);

			return newAvaliacao;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@PutMapping
	@ResponseBody
	Avaliacao editar(@RequestBody Avaliacao avaliacao) {
		logger.log(Level.INFO, "editar avaliação");
		validate(avaliacao);
		try {
			Avaliacao avaliacaoToEdit = this.avaliacaoRepository.getById(avaliacao.getId());
			avaliacaoToEdit.setNota(avaliacao.getNota());
			avaliacaoToEdit.setComentario(avaliacao.getComentario());
			this.avaliacaoRepository.saveAndFlush(avaliacaoToEdit);
			return avaliacaoToEdit;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@GetMapping
	@ResponseBody
	List<Avaliacao> buscar(@RequestParam(required = false) Long userRequisitanteId,
			@RequestParam(required = false) Long userRequisitadoId,
			@RequestParam(required = false) String dataInicio, @RequestParam(required = false) String dataFim) {
		logger.log(Level.INFO, "buscar avaliações");
		try {
			Avaliacao avaliacaoFind = new Avaliacao();
			Solicitacao solicitacao = new Solicitacao();
			List<Avaliacao> avaliacoes = null;
			if (userRequisitanteId != null || userRequisitadoId != null) {
				if (userRequisitanteId != null) {
					UsuarioEndereco userEndereco = new UsuarioEndereco(new Usuario(userRequisitanteId));
					solicitacao.setEnderecoRequisitante(userEndereco);
				}
				if (userRequisitadoId != null)
					solicitacao.setUserRequisitado(new Usuario(userRequisitadoId));
				avaliacaoFind.setSolicitacao(solicitacao);
				avaliacoes = avaliacaoRepository.findAll(Example.of(avaliacaoFind));
			} else
				avaliacoes = avaliacaoRepository.findAll();
			Date dtInicio = StringUtils.isEmpty(dataInicio) ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInicio);
			Date dtFim = StringUtils.isEmpty(dataFim) ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataFim);

			if (dtFim != null && dtInicio != null)
				avaliacoes.stream().filter(
						avaliacao -> avaliacao.getSolicitacao().getInicio().after(dtInicio) && avaliacao.getSolicitacao().getInicio().before(dtFim))
						.collect(Collectors.toList());
			else if (dtFim != null)
				avaliacoes.stream().filter(avaliacao -> avaliacao.getSolicitacao().getInicio().before(dtInicio))
						.collect(Collectors.toList());
			else if (dtInicio != null)
				avaliacoes.stream().filter(avaliacao -> avaliacao.getSolicitacao().getInicio().after(dtInicio))
						.collect(Collectors.toList());
			logger.log(Level.INFO, "retornando " + avaliacoes.size() + " avaliações");
			return avaliacoes;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return new ArrayList<Avaliacao>();
		}

	}
	
	private void validate(Avaliacao avaliacao) {
		logger.log(Level.INFO, "validando avaliação " + avaliacao);
		Validator validator = Validation.buildDefaultValidatorFactory()
	            .getValidator();
		StringBuilder sb = new StringBuilder();
		validator.validate(avaliacao)
        .stream()
        .forEach(violation -> sb.append(violation.getMessage()));
		
		if(sb.length() > 0)
			throw new RuntimeException(sb.toString());
		
	}

}
