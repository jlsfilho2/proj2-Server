package com.evita.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UsuarioSolicitacao {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	UsuarioServico userRequisitante;
	
	@ManyToOne
	UsuarioServico userRequisitado;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date horaInicio;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date horaFim;
	
	
	enum Status {
		INICIADO("iniciado"),
		AGENDADO("agendado"),
		CONCLUIDO("concluido");
		
		String descricao;
		
		Status(String descricao) {
			this.descricao = descricao;
		}
		
	}
	
	
	

}
