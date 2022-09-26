package com.evita.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import com.evita.model.UsuarioCategoria.Categoria;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Solicitacao {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@NotNull(message="Endereço do requisitante deve ser informado")
	UsuarioEndereco enderecoRequisitante;
	
	@ManyToOne
	@NotNull(message="Usuário requisitado deve ser informado")
	Usuario userRequisitado;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@Future(message="Data de início deve ser futura")
	@NotNull(message="Data de início deve ser informada")
	Date inicio;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@Future(message="Data fim deve ser futura")
	@NotNull(message="Data fim deve ser informada")
	Date fim;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="Status deve ser informado")
	Status status;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="Categoria do serviço deve ser informada")
	Categoria categoria;
	
	
	public enum Status {
		INICIADO,
		AGENDADO,
		CANCELADO,
		CONCLUIDO;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	


	public UsuarioEndereco getEnderecoRequisitante() {
		return enderecoRequisitante;
	}


	public void setEnderecoRequisitante(UsuarioEndereco enderecoRequisitante) {
		this.enderecoRequisitante = enderecoRequisitante;
	}


	public Usuario getUserRequisitado() {
		return userRequisitado;
	}


	public void setUserRequisitado(Usuario userRequisitado) {
		this.userRequisitado = userRequisitado;
	}


	public Date getInicio() {
		return inicio;
	}
	
	@JsonGetter("inicio")
	public String getInicioFormat() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(inicio);
	}

	public void setInicio(Date horaInicio) {
		this.inicio = horaInicio;
	}
	
	@JsonSetter("inicio")
	public void setInicio(String horaInicio) throws ParseException {
		this.inicio = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(horaInicio);
	}


	public Date getFim() {
		return fim;
	}
	
	@JsonGetter("fim")
	public String getFimFormat() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(fim);
	}


	public void setFim(Date horaFim) {
		this.fim = horaFim;
	}
	
	@JsonSetter("fim")
	public void setFim(String horaFim) throws ParseException {
		this.fim = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(horaFim);;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	
	
	

}
