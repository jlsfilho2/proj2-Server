package com.evita.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Usuario getUserRequisitante() {
		return userRequisitante;
	}


	public void setUserRequisitante(Usuario userRequisitante) {
		this.userRequisitante = userRequisitante;
	}


	public Usuario getUserRequisitado() {
		return userRequisitado;
	}


	public void setUserRequisitado(Usuario userRequisitado) {
		this.userRequisitado = userRequisitado;
	}


	public Date getHoraInicio() {
		return horaInicio;
	}


	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}


	public Date getHoraFim() {
		return horaFim;
	}


	public void setHoraFim(Date horaFim) {
		this.horaFim = horaFim;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	
	
	
	

}
