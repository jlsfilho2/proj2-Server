/**
 * 
 */
/**
 * @author joao
 *
 */
module ProjetoDesenvolvimento2 {
	exports com.oauth.rest;
	exports com.oatuh.services;
	exports com.oauth.infra;
	exports com.oauth.model;
	exports com.oauth.repository;

	requires javaee.api;
	requires spring.beans;
	requires spring.data.commons;
	requires spring.data.jpa;
}