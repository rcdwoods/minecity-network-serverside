package com.minecity.api.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document
@Component
public class Curtida {

	private String autor;
	
	public String getAutor() {
		return autor;
	}
	
	public void setAutor(String autor) {
		this.autor = autor;
	} 
	
	
}
