package com.evita.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class SolicitacaoPagamento {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	Solicitacao solicitacao;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date hora;
	
	@Column
	Float total;
	
	
	

}
