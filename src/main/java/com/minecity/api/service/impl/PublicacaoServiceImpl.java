package com.minecity.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minecity.api.model.Comentario;
import com.minecity.api.model.Curtida;
import com.minecity.api.model.Publicacao;
import com.minecity.api.repository.PublicacaoRepository;

@Service
public class PublicacaoServiceImpl {

	@Autowired
	private PublicacaoRepository publicacaoRepository;
	@Autowired
	private Publicacao publicacao;

	public Publicacao novaPublicacao(Publicacao publicacao) {
		if (publicacao.getTexto().isEmpty()) {
			return null;
		}
		Date date = new Date();
		publicacao.setDataPostagem(date);
		Publicacao novaPublicacao = this.publicacaoRepository.insert(publicacao);
		return novaPublicacao;
	}

	public List<Publicacao> obterPublicacoes() {
		return this.publicacaoRepository.findAll();
	}

	public Comentario novoComentario(Comentario comentario, String publicacaoId) throws Exception {
		Publicacao publicacao = this.publicacaoRepository.findById(publicacaoId)
				.orElseThrow(() -> new IllegalArgumentException());
		if (!publicacao.equals(null)) {
			UUID uuid = UUID.randomUUID();
			Comentario novoComentario = new Comentario();
			novoComentario.setAutor(comentario.getAutor());
			novoComentario.setTexto(comentario.getTexto());
			novoComentario.setId(uuid.toString());
			publicacao.addComentario(novoComentario);
			this.publicacaoRepository.save(publicacao);
			return novoComentario;
		}
		return null;
	}

	public void excluirPublicacao(String publicacaoId) {
		this.publicacaoRepository.deleteById(publicacaoId);
	}

	public boolean publicacaoisExistente(String publicacaoId) {
		boolean publicacaoIsExistente = this.publicacaoRepository.existsById(publicacaoId);
		return publicacaoIsExistente;
	}

	public Iterable<Publicacao> obterPublicacoesDoUsuario(String autor) {
		Iterable<Publicacao> publicacoesObtidas = this.publicacaoRepository.findAllByAutor(autor);
		List<Publicacao> publicacoesOrganizadas;
		return publicacoesObtidas;
	}

	public void excluirComentario(String publicacaoId, String comentarioId) throws Exception {
		Publicacao publicacao = this.publicacaoRepository.findById(publicacaoId)
				.orElseThrow(() -> new IllegalArgumentException());
		List<Comentario> novosComentarios = new ArrayList<Comentario>();
		publicacao.getComentarios().forEach(comentario -> {
			if (!comentario.getId().equals(comentarioId)) {
				novosComentarios.add(comentario);
				System.out.println(comentario.getId());
			}
		});
		publicacao.setComentarios(novosComentarios);
		this.publicacaoRepository.save(publicacao);
	}

	public Curtida novaCurtida(Curtida curtida, String publicacaoId) {
		Publicacao publicacao = this.publicacaoRepository.findById(publicacaoId)
				.orElseThrow(() -> new IllegalArgumentException());
		publicacao.addCurtida(curtida);
		this.publicacaoRepository.save(publicacao);
		return curtida;
	}

	public void removerCurtida(String autor, String publicacaoId) {
		Publicacao publicacao = this.publicacaoRepository.findById(publicacaoId)
				.orElseThrow(() -> new IllegalArgumentException());
		List<Curtida> novasCurtidas = new ArrayList<Curtida>();
		publicacao.getCurtidas().forEach(curtidaSalva -> {
			if (!curtidaSalva.getAutor().equals(autor)) {
				novasCurtidas.add(curtidaSalva);
			}
		});
		publicacao.setCurtidas(novasCurtidas);
		this.publicacaoRepository.save(publicacao);
	}
}
