package com.minecity.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

		return this.publicacaoRepository.insert(publicacao);
	}

	public List<Publicacao> obterPublicacoes() {
		return this.publicacaoRepository.findAll();
	}

	public Comentario novoComentario(Comentario comentario, String publicacaoId) throws Exception {
		Publicacao publicacao = this.publicacaoRepository.findById(publicacaoId)
				.orElseThrow(() -> new IllegalArgumentException("Publicação não encontrada."));
		if (!publicacao.equals(null)) {
			UUID idGerado = UUID.randomUUID();
			Comentario novoComentario = new Comentario(idGerado.toString(), comentario.getAutor(),
					comentario.getTexto());
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
		return this.publicacaoRepository.existsById(publicacaoId);
	}

	public Iterable<Publicacao> obterPublicacoesDoUsuario(String autor) {
		Iterable<Publicacao> publicacoesObtidas = this.publicacaoRepository.findAllByAutor(autor);
		return publicacoesObtidas;
	}

	public void excluirComentario(String publicacaoId, String comentarioId) throws Exception {
		Publicacao publicacao = this.publicacaoRepository.findById(publicacaoId)
				.orElseThrow(() -> new IllegalArgumentException());

		List<Comentario> novosComentarios = publicacao.getComentarios().stream()
				.filter(comentario -> !comentario.getId().equals(comentarioId)).collect(Collectors.toList());

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
		List<Curtida> novasCurtidas = publicacao.getCurtidas().stream()
				.filter(curtida -> !curtida.getAutor().equalsIgnoreCase(autor)).collect(Collectors.toList());

		publicacao.setCurtidas(novasCurtidas);
		this.publicacaoRepository.save(publicacao);
	}

	public Publicacao editarPublicacao(Publicacao publicacao) {
		if (this.publicacaoRepository.existsById(publicacao.getId())) {
			this.publicacaoRepository.save(publicacao);
			return publicacao;
		}
		return null;
	}
}
