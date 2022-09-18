package com.evita.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Solicitacao {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	Usuario userRequisitante;
	
	@ManyToOne
	Usuario userRequisitado;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date horaInicio;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date horaFim;
	
	@Enumerated(EnumType.STRING)
	Status status;
	
	
	enum Status {
		INICIADO,
		AGENDADO,
		CONCLUIDO;
	}
	
	
	

}
