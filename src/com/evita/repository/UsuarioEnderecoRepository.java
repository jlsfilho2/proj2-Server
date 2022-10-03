package com.evita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.evita.model.Usuario;
import com.evita.model.UsuarioEndereco;

@Repository
public interface UsuarioEnderecoRepository extends JpaRepository<UsuarioEndereco, Long> {
	
	@Query("SELECT e FROM UsuarioEndereco e WHERE e.user =:usuario AND e.ativo=true")
	List<UsuarioEndereco> findByUser(Usuario usuario); 	
	
}
