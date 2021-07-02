package com.minecity.api.model;

import org.springframework.data.annotation.Id; 
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document
@Component
public class Comentario {

	@Id
	private String id;
	private String autor;
	private String texto;
	
	public Comentario() {
	}
	
	public Comentario(String id, String autor, String texto) {
		this.id = id;
		this.autor = autor;	
		this.texto = texto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	

}
