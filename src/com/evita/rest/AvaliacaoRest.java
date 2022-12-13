package com.evita.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.evita.model.Avaliacao;
import com.evita.model.Solicitacao;
import com.evita.model.Usuario;
import com.evita.model.UsuarioEndereco;
import com.evita.repository.AvaliacaoRepository;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoRest {

	Logger logger = Logger.getLogger(AvaliacaoRest.class.getName());

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@PostMapping(consumes = {"application/xml","application/json"})
	@ResponseBody
	Avaliacao criar(@RequestBody Avaliacao avaliacao) {
		logger.log(Level.INFO, "criar avaliação " + avaliacao);
		validate(avaliacao);
		try {
			List<Avaliacao> avaliacoesExistentes = this.avaliacaoRepository.findBySolicitacao(
					new Solicitacao (
							avaliacao.getSolicitacao().getId()));
			if(!avaliacoesExistentes.isEmpty()) {
				logger.log(Level.SEVERE, "Solicitacao com id " + avaliacao.getSolicitacao().getId() +" já está avaliada");
				throw new ResponseStatusException(
				           HttpStatus.BAD_REQUEST, "Solicitacao já está avaliada");
			}
			Avaliacao newAvaliacao = this.avaliacaoRepository.saveAndFlush(avaliacao);
			newAvaliacao = this.avaliacaoRepository.getById(newAvaliacao.getId());
			return newAvaliacao;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(
			           HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@PutMapping(consumes = {"application/xml","application/json"})
	@ResponseBody
	Avaliacao editar(@RequestBody Avaliacao avaliacao) {
		logger.log(Level.INFO, "editar avaliação");
		//validate(avaliacao);
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
	
	@DeleteMapping(consumes = {"application/xml","application/json"})
	void deletar(@RequestParam(required = true) Long id) {
		logger.log(Level.INFO, "deletar avaliação " + id);
		//validate(avaliacao);
		try {
			this.avaliacaoRepository.deleteAllByIdInBatch(Arrays.asList(id));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
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
			Date dtInicio = StringUtils.isEmpty(dataInicio) || dataInicio.contentEquals("null") ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInicio);
			Date dtFim = StringUtils.isEmpty(dataFim) || dataFim.contentEquals("null") ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataFim);

			if (dtFim != null && dtInicio != null)
				avaliacoes = avaliacoes.stream().filter(
						avaliacao -> (avaliacao.getSolicitacao().getInicio().after(dtInicio)|| avaliacao.getSolicitacao().getInicio().equals(dtInicio))
								&& (avaliacao.getSolicitacao().getFim().before(dtFim) || avaliacao.getSolicitacao().getFim().equals(dtFim)))
						.collect(Collectors.toList());
			else if (dtFim != null)
				avaliacoes = avaliacoes.stream().filter(avaliacao -> avaliacao.getSolicitacao().getFim().before(dtFim) || avaliacao.getSolicitacao().getFim().equals(dtFim))
						.collect(Collectors.toList());
			else if (dtInicio != null)
				avaliacoes = avaliacoes.stream().filter(avaliacao -> avaliacao.getSolicitacao().getInicio().after(dtInicio) || avaliacao.getSolicitacao().getInicio().equals(dtInicio))
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
        .forEach(violation -> sb.append(violation.getMessage()).append(";"));
		
		if(sb.length() > 0){
			logger.log(Level.SEVERE, sb.toString());
			throw new ResponseStatusException(
			           HttpStatus.BAD_REQUEST, sb.toString());
		}
		
	}

}
