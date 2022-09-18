package com.evita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.SolicitacaoAvaliacao;

@Repository
public interface SolicitacaoPagamentoRepository extends JpaRepository<SolicitacaoAvaliacao, Long> {}
