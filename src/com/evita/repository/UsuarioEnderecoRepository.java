package com.evita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evita.model.UsuarioEndereco;

@Repository
public interface UsuarioEnderecoRepository extends JpaRepository<UsuarioEndereco, Long> {}
