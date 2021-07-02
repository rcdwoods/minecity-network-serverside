package com.minecity.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document
@Component
public class Publicacao {

	@Id
	private String id;
	private String autor;
	private String texto;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
	private Date dataPostagem;
	private List<Curtida> curtidas = new ArrayList<Curtida>();
	private List<Comentario> comentarios = new ArrayList<Comentario>();
	private boolean editorIsAberto;

	public Publicacao() {
	}

	public Publicacao(String autor, String texto, Date dataPostagem, List<Curtida> curtidas,
			List<Comentario> comentarios) {
		this.autor = autor;
		this.texto = texto;
		this.dataPostagem = dataPostagem;
		this.curtidas = curtidas;
		this.comentarios = comentarios;
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

	public Date getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(Date dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public List<Curtida> getCurtidas() {
		return curtidas;
	}

	public void setCurtidas(List<Curtida> curtidas) {
		this.curtidas = curtidas;
	}

	public void addCurtida(Curtida curtida) {
		this.curtidas.add(curtida);
	}

	public void removeCurtida(Curtida curtida) {
		this.curtidas.remove(curtida);
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void addComentario(Comentario comentario) {
		this.comentarios.add(comentario);
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public void removeComentario(Comentario comentario) {
		this.comentarios.remove(comentario);
	}

	public boolean isEditorIsAberto() {
		return editorIsAberto;
	}

	public void setEditorIsAberto(boolean editorIsAberto) {
		this.editorIsAberto = editorIsAberto;
	}

}
