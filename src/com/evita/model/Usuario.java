package com.evita.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="Usuario")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Usuario {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length=100)
	String nome;
	
	@Column(length=100)
	String userId;
	
	@Column(length=40)
	String pass;
	
	@Enumerated(EnumType.STRING)
	Tipo tipo;
	
	
	
	enum Tipo {
		PRESTADOR,
		CLIENTE,
		PRESTADOR_CLIENTE;
	}


	Usuario(){
		
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



	public Tipo getTipo() {
		return tipo;
	}



	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", userId=" + userId + ", pass=" + pass + ", tipo=" + tipo
				+ "]";
	}
	
	
	

}
