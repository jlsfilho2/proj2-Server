package com.evita.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="Usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Usuario {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length=100,nullable=false)
	String nome;
	
	@Column(length=100, unique=true,nullable=false)
	String userId;
	
	@Column(length=40,nullable=false)
	String pass;
	
	@JsonProperty(required=false)
	@OneToMany(mappedBy="user")
	List<UsuarioEndereco> enderecos;
	
	@Enumerated(EnumType.STRING)
	Tipo tipo;
	
	@JsonProperty(required=false)
	@OneToMany(mappedBy="user",fetch=FetchType.EAGER)
	List<UsuarioCategoria> categorias;
	
	
	
	enum Tipo {
		PRESTADOR,
		CLIENTE,
		PRESTADOR_CLIENTE;
	}

	public Usuario() {}
	
	public Usuario(Long id) {
		this.id = id;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(userId, other.userId);
	}
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public List<UsuarioEndereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<UsuarioEndereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	
	
	public List<UsuarioCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<UsuarioCategoria> categorias) {
		this.categorias = categorias;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", userId=" + userId + ", pass=" + pass + ", tipo=" + tipo
				+ "]";
	}
	
	
	

}
