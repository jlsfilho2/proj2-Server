package com.evita.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.Solicitacao;
import com.evita.model.SolicitacaoPagamento;
import com.evita.model.Usuario;
import com.evita.model.SolicitacaoPagamento.Status;

@Repository
public interface SolicitacaoPagamentoRepository extends JpaRepository<SolicitacaoPagamento, Long> {
	
	List<SolicitacaoPagamento> findBySolicitacao(Solicitacao solicitacao);
	
	List<SolicitacaoPagamento> findByStatus(Status status);
	
	List<SolicitacaoPagamento> findAllBySolicitacaoIn(Set<Solicitacao> solicitacoes);
}
