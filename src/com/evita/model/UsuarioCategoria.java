package com.evita.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class UsuarioCategoria {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	Usuario user;
	
	@Enumerated(EnumType.STRING)
	Categoria categoria;
	
	@Column
	Float valor;
	
	UsuarioCategoria() {
		
	}
	
	enum Categoria {
		MANUTENCAO_ELETRICA("Manutenção Elétrica"),
		MANUTENCAO_HIDRAULICA("Manutenção Hidráulica"),
		HIGIENE_PESSOAL("Higiene Pessoal"),
		LIMPEZA("Limpeza"),
		CUIDADOS("Cuidados");
		
		
		String descricao;
		Categoria(String descricao) {
			this.descricao = descricao;
		}
		
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categorias) {
		this.categoria = categorias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoria, id, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioCategoria other = (UsuarioCategoria) obj;
		return categoria == other.categoria && id == other.id && Objects.equals(user, other.user);
	}
	
	
	
	
	

}
