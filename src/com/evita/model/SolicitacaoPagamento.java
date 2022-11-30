package com.evita.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitacaoPagamento {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@NotNull(message = "Solicitação deve ser informada")
	@JsonIgnore
	Solicitacao solicitacao;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	Date data;

	@Column
	@Min(value = 0, message = "Valor total deve ser maior que 0")
	@NotNull(message = "Valor total deve ser informado")
	Float total;

	@Column
	@Min(value = 0, message = "Valor prestado deve ser maior que 0")
	@NotNull(message = "Valor prestado deve ser informado")
	Float valorPrestado;

	@Column
	@Min(value = 0, message = "Valor do desconto deve ser maior que 0")
	Float valorDesconto;

	@Enumerated(EnumType.STRING)
	Status status;

	@Enumerated(EnumType.STRING)
	Tipo tipo;

	SolicitacaoPagamento() {

	}

	public SolicitacaoPagamento(Solicitacao solicitacao, Float valor) {
		this.solicitacao = solicitacao;
		this.valorPrestado = valor;
		this.total = valor;
		this.status = Status.PENDENTE;
	}



	enum Tipo {
		BANCO, PIX, DINHEIRO, BOLETO
	}

	enum Status {
		CONFIRMADO, PENDENTE
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Float getValorPrestado() {
		return valorPrestado;
	}

	public void setValorPrestado(Float valorPrestado) {
		this.valorPrestado = valorPrestado;
	}

	public Float getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Float valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

}
