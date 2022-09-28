package com.evita.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
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

import com.evita.model.Avaliacao;
import com.evita.model.Solicitacao;
import com.evita.model.Solicitacao.Status;
import com.evita.model.SolicitacaoPagamento;
import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;
import com.evita.model.UsuarioEndereco;
import com.evita.repository.SolicitacaoPagamentoRepository;
import com.evita.repository.SolicitacaoRepository;
import com.evita.repository.UsuarioCategoriaRepository;
import com.evita.repository.UsuarioRepository;

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

	@PostMapping
	@ResponseBody
	Solicitacao criar(@RequestBody Solicitacao solicitacao) {
		logger.log(Level.INFO, "criar solicitação " + solicitacao);
		validate(solicitacao);
		try {
			Solicitacao newSolicitacao = this.solicitacaoRepository.saveAndFlush(solicitacao);

			return newSolicitacao;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new ResponseStatusException(
			           HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

	@PutMapping
	@ResponseBody
	Solicitacao editar(@RequestBody Solicitacao solicitacao) {
		//validate(solicitacao);
		logger.log(Level.INFO, "editar solicitacao");
		try {
			Solicitacao solicitacaoToEdit = this.solicitacaoRepository.getById(solicitacao.getId());
			if (solicitacao.getInicio() != null)
				solicitacaoToEdit.setInicio(solicitacao.getInicio());
			if (solicitacao.getFim() != null)
				solicitacaoToEdit.setFim(solicitacao.getFim());
			if (solicitacao.getStatus() != null) {
				solicitacaoToEdit.setStatus(solicitacao.getStatus());
				if (solicitacao.getStatus() == Status.CONCLUIDO) {
					logger.log(Level.INFO, "estado concluído, solicitação de pagamento");
					UsuarioCategoria userCategoria = null;
					SolicitacaoPagamento solicitacaoPagamento = null;
					List<UsuarioCategoria> usuarioCategoria = usuarioCategoriaRepository
							.findByUser(solicitacaoToEdit.getEnderecoRequisitante().getUser());
					if (usuarioCategoria.
							contains(new UsuarioCategoria(solicitacaoToEdit.getCategoria()))) {
						logger.log(Level.INFO, "encontrada instancia de usuarioCategoria");
					userCategoria = usuarioCategoria.get(usuarioCategoria.indexOf(new UsuarioCategoria(solicitacaoToEdit.getCategoria())));
					logger.log(Level.INFO, "inserindo com " + userCategoria);
					solicitacaoPagamento = new SolicitacaoPagamento(solicitacaoToEdit,userCategoria.getValor());
					} 
					else  {
						logger.log(Level.INFO, "criando uma instância default");
						solicitacaoPagamento = new SolicitacaoPagamento(solicitacaoToEdit,0.0f);
					}
					solicitacaoPagamentoRepository.saveAndFlush(solicitacaoPagamento);

				}
			}
			this.solicitacaoRepository.saveAndFlush(solicitacaoToEdit);
			return solicitacaoToEdit;
		}catch(	Exception ex)
	{
		logger.log(Level.SEVERE, "erro: " + ex.getMessage() + ", " + ex.getClass().getName());
		throw new ResponseStatusException(
		           HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	}

	@GetMapping
	@ResponseBody
	List<Solicitacao> buscar(@RequestParam(required = false) Long userRequisitanteId,
			@RequestParam(required = false) Long userRequisitadoId, @RequestParam(required = false) Status status,
			@RequestParam(required = false) String dataInicio, @RequestParam(required = false) String dataFim) {
		logger.log(Level.INFO, "buscar solicitacões");
		try {
			Solicitacao solicitacaoFind = new Solicitacao();
			List<Solicitacao> solicitacoes = null;
			if (userRequisitanteId != null || userRequisitadoId != null || status != null) {
				if (userRequisitanteId != null) {
					UsuarioEndereco userEndereco = new UsuarioEndereco(new Usuario(userRequisitanteId));
					solicitacaoFind.setEnderecoRequisitante(userEndereco);
				}
				if (userRequisitadoId != null)
					solicitacaoFind.setUserRequisitado(new Usuario(userRequisitadoId));
				if (status != null)
					solicitacaoFind.setStatus(status);
				solicitacoes = solicitacaoRepository.findAll(Example.of(solicitacaoFind));
			} else
				solicitacoes = solicitacaoRepository.findAll();
			Date dtInicio = StringUtils.isEmpty(dataInicio) ? null
					: new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataInicio);
			Date dtFim = StringUtils.isEmpty(dataFim) ? null : new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dataFim);

			if (dtFim != null && dtInicio != null)
				solicitacoes.stream().filter(
						solicitacao -> solicitacao.getInicio().after(dtInicio) && solicitacao.getInicio().before(dtFim))
						.collect(Collectors.toList());
			else if (dtFim != null)
				solicitacoes.stream().filter(solicitacao -> solicitacao.getInicio().before(dtInicio))
						.collect(Collectors.toList());
			else if (dtInicio != null)
				solicitacoes.stream().filter(solicitacao -> solicitacao.getInicio().after(dtInicio))
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

		if (sb.length() > 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString());

	}

}
