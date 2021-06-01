package com.minecity.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document
@Component
public class Usuario {

	@Id
	private String id;

	private String username;
	private String email;
	private String senha;
	private float money;
	private int residencias;

	private List<String> amigos = new ArrayList<String>();
	private List<String> solicitacoesDeAmizade = new ArrayList<String>();
	private List<Permissao> permissoes = new ArrayList<Permissao>();
	private List<Notificacao> notificacoes = new ArrayList<Notificacao>(); 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String nome) {
		this.username = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public int getResidencias() {
		return residencias;
	}

	public void setResidencias(int residencias) {
		this.residencias = residencias;
	}

	public List<String> getAmigos() {
		return this.amigos;
	}

	public void addAmigo(String username) {
		this.amigos.add(username);
	}
	
	public List<String> getSolicitacoesDeAmizade() {
		return solicitacoesDeAmizade;
	}

	public void setSolicitacoesDeAmizade(List<String> solicitacoesDeAmizade) {
		this.solicitacoesDeAmizade = solicitacoesDeAmizade;
	}
	
	public void addSolicitacaoDeAmizade(String username) {
		this.solicitacoesDeAmizade.add(username);
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public void addPermissao(Permissao permissao) {
		this.permissoes.add(permissao);
	}

	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public void setNotificacoes(List<Notificacao> notificacoes) {
		this.notificacoes = notificacoes;
	}
	
	public void addNotificacao(Notificacao notificacao) {
		this.notificacoes.add(notificacao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
