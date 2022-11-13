package com.evita.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByUserId(String userId); 	
	
	Usuario findByEmail(String email);
	
	List<Usuario> findAllByIdIn(Set<Long> ids);
	
	List<Usuario> findAllByIdNotIn(Set<Long> ids);

	
}
