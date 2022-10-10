package com.evita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.Usuario;
import com.evita.model.UsuarioCategoria;
import com.evita.model.UsuarioCategoria.Categoria;

@Repository
public interface UsuarioCategoriaRepository extends JpaRepository<UsuarioCategoria, Long> {
	
	List<UsuarioCategoria> findByUser(Usuario usuario);
	
	List<UsuarioCategoria> findByCategoria(Categoria categoria);
	
}
