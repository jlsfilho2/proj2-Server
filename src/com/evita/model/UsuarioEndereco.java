package com.evita.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioEndereco {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	@NotNull(message="Usuário deve ser informado")
	Usuario user;
	
	@Enumerated(EnumType.STRING)
	@Column(length=2,nullable=false)
	@NotNull(message="A UF deve ser informada")
	UF uf;
	
	public enum UF{
	RS,SC,PR,AC,PA,AP,BA,SE,ES,GO,PB,TO,RR,SP,RJ,MA,MG,RO,AM,PE,PI,RN,DF	
	}
	
	@Column(length=8,nullable=false)
	@Size(max=8,min=8,message="O CEP deve ter no máximo 8 caractéres")
	@Pattern(regexp="[0-9]",message="CEP deve conter apenas caractéres numéricos")
	@NotNull(message="O CEP deve ser informado")
	String cep;
	
	@Column(length=60,nullable=true)
	@Size(max=0,min=60,message="O complemento deve ter no máximo 80 caractéres")
	String complemento;
	
	@Column(length=60,nullable=false)
	@Size(max=60,min=0,message="O logradouro deve ter no máximo 60 caractéres")
	@NotNull(message="O logradouro deve ser informado")
	String logradouro;
	
	@Column(length=60,nullable=false)
	@Size(max=60,min=3,message="A Cidade deve ter no máximo 60 caractéres")
	@NotNull(message="A Cidade deve ser informada")
	String cidade;
	
	@Column(length=60,nullable=false)
	@Size(max=60,min=3,message="O Bairro deve ter no máximo 60 caractéres")
	@NotNull(message="O Bairro deve ser informado")
	String bairro;
	
	@Column(nullable=true)
	@Min(value=0,message="O número deve ser maior que 0")
	Integer numero;
	
	UsuarioEndereco() {
		
	}
	
	public UsuarioEndereco(Usuario usuario) {
		this.user = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioEndereco other = (UsuarioEndereco) obj;
		return id == other.id;
	}

	
	
	

}
