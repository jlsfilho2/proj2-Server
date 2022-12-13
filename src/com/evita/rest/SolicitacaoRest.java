package com.evita.rest;

import java.text.SimpleDateFormat;
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

import com.evita.model.Solicitacao;
import com.evita.model.Solicitacao.Status;
import com.evita.model.SolicitacaoPagamento;
import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;
import com.evita.model.UsuarioEndereco;
import com.evita.repository.SolicitacaoPagamentoRepository;
import com.evita.repository.SolicitacaoRepository;
import com.evita.repository.UsuarioCategoriaRepository;

@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoRest {

	Logger logger = Logger.getLogger(SolicitacaoRest.class.getName());

	@Autowired
	private SolicitacaoRepository solicitacaoRepository;

	@Autowired
	private UsuarioCategoriaRepository usuarioCategoriaRepository;

	@Autowired
	private SolicitacaoPagamentoRepository solicitacaoPagamentoRepository;

	@PostMapping(consumes = { "application/xml", "application/json" })
	@ResponseBody
	Solicitacao criar(@RequestBody Solicitacao solicitacao) {
		logger.log(Level.INFO, "criar solicitação " + solicitacao);
		validate(solicitacao);
		try {
			Solicitacao newSolicitacao = this.solicitacaoRepository.saveAndFlush(solicitacao);
			logger.log(Level.INFO, "inserindo solicitação de pagamento");
			List<UsuarioCategoria> usuarioCategoria = usuarioCategoriaRepository
					.findByUser(solicitacao.getUserRequisitado());
			
			usuarioCategoria = usuarioCategoria.stream()
					.filter(catFilter -> catFilter.getCategoria() == solicitacao.getCategoria())
					.collect(Collectors.toList());
			
			if (!usuarioCategoria.isEmpty()) {
				logger.log(Level.INFO, "encontrada instancia de usuarioCategoria");
				UsuarioCategoria userCategoria = usuarioCategoria.get(0);
				logger.log(Level.INFO, "inserindo com " + userCategoria);
				SolicitacaoPagamento solicitacaoPagamento = new SolicitacaoPagamento(newSolicitacao, userCategoria.getValor());
				solicitacaoPagamento = this.solicitacaoPagamentoRepository.saveAndFlush(solicitacaoPagamento);
				newSolicitacao.setPagamento(solicitacaoPagamento);
			}
			return newSolicitacao;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@DeleteMapping(consumes = { "application/xml", "application/json" })
	void deletar(@RequestParam(required = true) Long id) {
		logger.log(Level.INFO, "deletar solicitacao " + id);
		try {
			Solicitacao solicitacaoToEdit = this.solicitacaoRepository.getById(id);
			if (solicitacaoToEdit.getStatus() == Status.AGENDADO)
				this.solicitacaoRepository.deleteAllByIdInBatch(Arrays.asList(id));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}

	}

	@PutMapping(consumes = { "application/xml", "application/json" })
	@ResponseBody
	Solicitacao editar(@RequestBody Solicitacao solicitacao) {
		// validate(solicitacao);
		logger.log(Level.INFO, "editar solicitacao");
		try {
			Solicitacao solicitacaoToEdit = this.solicitacaoRepository.getById(solicitacao.getId());
			if (solicitacao.getInicio() != null)
				solicitacaoToEdit.setInicio(solicitacao.getInicio());
			if (solicitacao.getFim() != null)
				solicitacaoToEdit.setFim(solicitacao.getFim());
			if (solicitacao.getStatus() != null) {
				solicitacaoToEdit.setStatus(solicitacao.getStatus());
				if (solicitacao.getStatus() == Status.CANCELADO) {
					logger.log(Level.INFO, "estado cancelado, excluindo solicitação de pagamento");
					List<SolicitacaoPagamento> solicitacoesPagamento = solicitacaoPagamentoRepository.findBySolicitacao(solicitacao);
					if (!solicitacoesPagamento.isEmpty()) {
						logger.log(Level.INFO, "encontrada instancia de pagamento");
						SolicitacaoPagamento solicitacaoPagamento = solicitacoesPagamento.get(0);
						if(solicitacaoPagamento.getStatus() == com.evita.model.SolicitacaoPagamento.Status.CONFIRMADO) {	
							solicitacaoPagamento.setStatus(com.evita.model.SolicitacaoPagamento.Status.ESTORNADO);
							solicitacaoPagamentoRepository.saveAndFlush(solicitacaoPagamento);							
						}
						else solicitacaoPagamentoRepository.deleteById(solicitacaoPagamento.getId());
					}
				}
			}
			this.solicitacaoRepository.saveAndFlush(solicitacaoToEdit);
			return solicitacaoToEdit;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "erro: " + ex.getMessage() + ", " + ex.getClass().getName());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@GetMapping
	@ResponseBody
	List<Solicitacao> buscar(@RequestParam(required = false) Long userRequisitanteId,
			@RequestParam(required = false) Long userRequisitadoId, @RequestParam(required = false) Status status,
			@RequestParam(required = false) String userRequisitadoEmail,@RequestParam(required = false) String userRequisitanteEmail,
			@RequestParam(required = false) String dataInicio, @RequestParam(required = false) String dataFim) {
		logger.log(Level.INFO, "buscar solicitacões");
		try {
			Solicitacao solicitacaoFind = new Solicitacao();
			List<Solicitacao> solicitacoes = null;
			if (!StringUtils.isEmpty(userRequisitadoEmail) || !StringUtils.isEmpty(userRequisitanteEmail) || userRequisitanteId != null || userRequisitadoId != null || status != null) {
				if (userRequisitanteId != null) {
					logger.log(Level.FINE, "userRequisitanteId " + userRequisitanteId);
					UsuarioEndereco userEndereco = new UsuarioEndereco(new Usuario(userRequisitanteId));
					solicitacaoFind.setEnderecoRequisitante(userEndereco);
				}
				if (userRequisitadoId != null) {
					logger.log(Level.FINE, "userRequisitadoId " + userRequisitadoId);
					solicitacaoFind.setUserRequisitado(new Usuario(userRequisitadoId));
				}
				if (!StringUtils.isEmpty(userRequisitadoEmail)) {
					logger.log(Level.FINE, "userRequisitadoEmail " + userRequisitadoEmail);
					solicitacaoFind.setUserRequisitado(new Usuario(userRequisitadoEmail));
				}
				if (!StringUtils.isEmpty(userRequisitanteEmail)) {
					logger.log(Level.FINE, "userRequisitanteEmail " + userRequisitanteEmail);
					UsuarioEndereco userEndereco = new UsuarioEndereco(new Usuario(userRequisitanteEmail));
					solicitacaoFind.setEnderecoRequisitante(userEndereco);
				}
				if (status != null) {
					logger.log(Level.FINE, "status " + status);
					solicitacaoFind.setStatus(status);
				}
				solicitacoes = solicitacaoRepository.findAll(Example.of(solicitacaoFind));
			} else
				solicitacoes = solicitacaoRepository.findAll();
			Date dtInicio = StringUtils.isEmpty(dataInicio) || dataInicio.contentEquals("null") ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInicio);
			Date dtFim = StringUtils.isEmpty(dataFim) || dataFim.contentEquals("null") ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataFim);

			if (dtFim != null && dtInicio != null)
				solicitacoes = solicitacoes.stream().filter(
						solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio))
								&& (solicitacao.getFim().before(dtFim) || solicitacao.getFim().equals(dtFim)))
						.collect(Collectors.toList());
			else if (dtFim != null)
				solicitacoes = solicitacoes.stream().filter(
						solicitacao -> (solicitacao.getFim().before(dtFim)|| solicitacao.getFim().equals(dtFim)))
						.collect(Collectors.toList());
			else if (dtInicio != null)
				solicitacoes = solicitacoes.stream().filter(
						solicitacao -> (solicitacao.getInicio().after(dtInicio)|| solicitacao.getInicio().equals(dtInicio)))
						.collect(Collectors.toList());
			logger.log(Level.INFO, "retornando " + solicitacoes.size() + " solicitações");
			return solicitacoes;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	private void validate(Solicitacao solicitacao) {
		logger.log(Level.INFO, "validando solicitação " + solicitacao);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		StringBuilder sb = new StringBuilder();
		validator.validate(solicitacao).stream().forEach(violation -> sb.append(violation.getMessage()).append(";"));

		if (sb.length() > 0) {
			logger.log(Level.SEVERE, sb.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString());
		}

	}

}
