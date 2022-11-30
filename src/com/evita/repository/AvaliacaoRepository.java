package com.evita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.Avaliacao;
import com.evita.model.Solicitacao;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
	List<Avaliacao> findBySolicitacao(Solicitacao solicitacao);
	
}
