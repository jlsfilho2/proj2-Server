package com.evita.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import com.evita.model.SolicitacaoPagamento.Status;
import com.evita.model.SolicitacaoPagamento;
import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;
import com.evita.model.UsuarioEndereco;
import com.evita.repository.SolicitacaoPagamentoRepository;
import com.evita.repository.SolicitacaoRepository;
import com.evita.repository.UsuarioCategoriaRepository;
import com.evita.repository.UsuarioRepository;

@RestController
@RequestMapping("/pagamento")
public class PagamentoRest {

	Logger logger = Logger.getLogger(PagamentoRest.class.getName());

	@Autowired
	private SolicitacaoRepository solicitacaoRepository;

	@Autowired
	private SolicitacaoPagamentoRepository solicitacaoPagamentoRepository;

	@PutMapping(consumes = { "application/xml", "application/json" })
	@ResponseBody
	SolicitacaoPagamento editar(@RequestBody SolicitacaoPagamento solicitacaoPagamento) {
		logger.log(Level.INFO, "editar solicitação de pagamento");
		boolean confirmado = false;
		try {
			SolicitacaoPagamento solicitacaoToEdit = this.solicitacaoPagamentoRepository.getById(solicitacaoPagamento.getId());
			if (solicitacaoPagamento.getStatus() != null) {
				solicitacaoToEdit.setStatus(solicitacaoPagamento.getStatus());
				confirmado =  solicitacaoPagamento.getStatus() ==  Status.CONFIRMADO;
				solicitacaoToEdit.setData(confirmado? new Date() : null);
			}
			if (solicitacaoPagamento.getTipo() != null) {
				solicitacaoToEdit.setTipo(solicitacaoPagamento.getTipo());
			}
			if (solicitacaoPagamento.getValorDesconto() != null) {
				solicitacaoToEdit.setValorDesconto(solicitacaoPagamento.getValorDesconto());
				Float valorTotal = solicitacaoToEdit.getValorPrestado();
				valorTotal-=solicitacaoPagamento.getValorDesconto();
				solicitacaoToEdit.setTotal(valorTotal);
			}
			if(confirmado) {
				logger.log(Level.INFO, "editar solicitação");
				Solicitacao sol = solicitacaoToEdit.getSolicitacao();
				sol.setStatus(com.evita.model.Solicitacao.Status.PAGO);
				solicitacaoRepository.saveAndFlush(sol);
			}
			//validate(solicitacaoToEdit);
			return solicitacaoPagamentoRepository.saveAndFlush(solicitacaoToEdit);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "erro: " + ex.getMessage() + ", " + ex.getClass().getName());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@GetMapping
	@ResponseBody
	List<SolicitacaoPagamento> buscar(@RequestParam(required = false) Long solicitacaoId,
			@RequestParam(required = false) Long userRequisitanteId,
			@RequestParam(required = false) Long userRequisitadoId, @RequestParam(required = false) Status status,
			@RequestParam(required = false) String dataInicio, @RequestParam(required = false) String dataFim) {
		logger.log(Level.INFO, "buscar solicitacões");
		try {
			Solicitacao solicitacaoFind = new Solicitacao();
			Set<Solicitacao> solicitacoes = null;
			if (solicitacaoId != null) {
				logger.log(Level.FINE, "solicitacaoId " + solicitacaoId);
				solicitacaoFind.setId(solicitacaoId);
			}
			if (userRequisitanteId != null || userRequisitadoId != null) {
				if (userRequisitanteId != null) {
					logger.log(Level.FINE, "userRequisitanteId " + userRequisitanteId);
					UsuarioEndereco userEndereco = new UsuarioEndereco(new Usuario(userRequisitanteId));
					solicitacaoFind.setEnderecoRequisitante(userEndereco);
				}
				if (userRequisitadoId != null) {
					logger.log(Level.FINE, "userRequisitadoId " + userRequisitadoId);
					solicitacaoFind.setUserRequisitado(new Usuario(userRequisitadoId));
				}
			}
			solicitacoes = new HashSet<>(solicitacaoRepository.findAll(Example.of(solicitacaoFind)));
			Date dtInicio = StringUtils.isEmpty(dataInicio) || dataInicio.contentEquals("null") ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInicio);
			Date dtFim = StringUtils.isEmpty(dataFim) || dataFim.contentEquals("null") ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataFim);

			if (dtFim != null && dtInicio != null)
				solicitacoes = solicitacoes.stream().filter(
						solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio))
								&& (solicitacao.getFim().before(dtFim) || solicitacao.getFim().equals(dtFim)))
						.collect(Collectors.toSet());
			else if (dtFim != null)
				solicitacoes = solicitacoes.stream().filter(
						solicitacao -> (solicitacao.getFim().before(dtFim)|| solicitacao.getFim().equals(dtFim)))
						.collect(Collectors.toSet());
			else if (dtInicio != null)
				solicitacoes = solicitacoes.stream().filter(
						solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio)))
						.collect(Collectors.toSet());
			logger.log(Level.INFO, "encontrados " + solicitacoes.size() + " solicitações concluídas");
			List<SolicitacaoPagamento> solicitacaoPagamentos = solicitacaoPagamentoRepository
					.findAllBySolicitacaoIn(solicitacoes);
			if (status != null)
				solicitacaoPagamentos.stream().filter(pagFilter -> pagFilter.getStatus() == status)
						.collect(Collectors.toList());
			return solicitacaoPagamentos;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	private void validate(SolicitacaoPagamento solicitacaoPagamento) {
		logger.log(Level.INFO, "validando solicitação de pagamento " + solicitacaoPagamento);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		StringBuilder sb = new StringBuilder();
		validator.validate(solicitacaoPagamento).stream()
				.forEach(violation -> sb.append(violation.getMessage()).append(";"));

		if (sb.length() > 0) {
			logger.log(Level.SEVERE, sb.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString());
		}

	}

}
