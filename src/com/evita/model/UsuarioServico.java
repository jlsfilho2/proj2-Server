package com.evita.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Usuario")
public class UsuarioServico {
	
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
	
	Tipo tipo;
	
	
	
	enum Tipo {
		PRESTADOR("prestador"),
		CLIENTE("cliente");
		
		String descricao;
		
		Tipo(String descricao) {
			this.descricao = descricao;
		}
	}
	
	

}
