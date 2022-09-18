package com.evita.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UsuarioEndereco {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	Usuario user;
	
	@Column
	String uf;
	
	@Column
	String cep;
	
	@Column
	String complemento;
	
	@Column
	String logradouro;
	
	@Column
	String cidade;
	
	@Column
	String bairro;
	
	@Column
	Integer numero;
	
	

}
