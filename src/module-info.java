/**
 * 
 */
/**
 * @author joao
 *
 */
module ProjetoDesenvolvimento2 {
	exports com.evita.rest;
	exports com.evita.services;
	exports com.evita.infra;
	exports com.evita.model;
	exports com.evita.repository;

	requires javaee.api;
	requires spring.beans;
	requires spring.data.commons;
	requires spring.data.jpa;
}