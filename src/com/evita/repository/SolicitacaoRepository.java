package com.evita.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.Solicitacao;
import com.evita.model.Solicitacao.Status;
import com.evita.model.Usuario;
import com.evita.model.UsuarioEndereco;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
	
	
	List<Solicitacao> findByEnderecoRequisitante(UsuarioEndereco endereco); 
	
	List<Solicitacao> findByUserRequisitado(Usuario usuario); 
	
	List<Solicitacao> findByInicio(Date date); 
	
	List<Solicitacao> findByFim(Date date); 
	
	List<Solicitacao> findByStatus(Status status); 
	
	
	
}
