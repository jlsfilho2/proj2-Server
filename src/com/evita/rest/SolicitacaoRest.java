package com.evita.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

import com.evita.model.Solicitacao;
import com.evita.model.Solicitacao.Status;
import com.evita.model.Usuario;
import com.evita.repository.SolicitacaoRepository;
import com.evita.repository.UsuarioRepository;

@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoRest {

	Logger logger = Logger.getLogger(SolicitacaoRest.class.getName());

	@Autowired
	private SolicitacaoRepository solicitacaoRepository;

	@PostMapping
	@ResponseBody
	Solicitacao criar(@RequestBody Solicitacao solicitacao) {
		logger.log(Level.INFO, "criar solicitação " + solicitacao);
		try {
			Solicitacao newSolicitacao = this.solicitacaoRepository.saveAndFlush(solicitacao);

			return newSolicitacao;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@PutMapping
	@ResponseBody
	Solicitacao editar(@RequestBody Solicitacao solicitacao) {
		logger.log(Level.INFO, "editar solicitacao");
		try {
			Solicitacao solicitacaoToEdit = this.solicitacaoRepository.getById(solicitacao.getId());
			solicitacaoToEdit.setInicio(solicitacao.getInicio());
			solicitacaoToEdit.setFim(solicitacao.getFim());
			solicitacaoToEdit.setStatus(solicitacao.getStatus());
			this.solicitacaoRepository.saveAndFlush(solicitacaoToEdit);
			return solicitacaoToEdit;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null;
		}

	}

	@GetMapping
	@ResponseBody
	List<Solicitacao> buscar(@RequestParam Long userRequisitanteId, @RequestParam Long userRequisitadoId,
			@RequestParam Status status, @RequestParam String dataInicio, @RequestParam String dataFim) {
		logger.log(Level.INFO, "buscar solicitacões");
		try {
			Solicitacao solicitacaoFind = new Solicitacao();
			if(userRequisitanteId != null) 
				solicitacaoFind.setUserRequisitante(new Usuario(userRequisitanteId));
			if(userRequisitadoId != null)				
				solicitacaoFind.setUserRequisitado(  new Usuario(userRequisitadoId));
			if(status != null)
				solicitacaoFind.setStatus(status);
			List<Solicitacao> solicitacoes = solicitacaoRepository.findAll(Example.of(solicitacaoFind));
			Date dtInicio = StringUtils.isEmpty(dataInicio)? null : new SimpleDateFormat("dd-MM-yyyy").parse(dataInicio);
			Date dtFim = StringUtils.isEmpty(dataFim)? null : new SimpleDateFormat("dd-MM-yyyy").parse(dataFim);
			
			if(dtFim != null && dtInicio != null)
				solicitacoes.stream()
			      .filter(solicitacao -> 
			      solicitacao.getInicio().after(dtInicio) && solicitacao.getInicio().before(dtFim))
			      .collect(Collectors.toList());
			else if(dtFim != null)
				solicitacoes.stream()
			      .filter(solicitacao -> 
			      solicitacao.getInicio().before(dtInicio))
			      .collect(Collectors.toList());
			else if(dtInicio != null)
				solicitacoes.stream()
			      .filter(solicitacao -> 
			      solicitacao.getInicio().after(dtInicio))
			      .collect(Collectors.toList());
			logger.log(Level.INFO, "retornando " + solicitacoes.size() + " solicitações");
			return solicitacoes;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return new ArrayList<Solicitacao>();
		}

	}

}
