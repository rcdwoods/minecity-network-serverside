package com.minecity.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minecity.api.model.Comentario;
import com.minecity.api.model.Curtida;
import com.minecity.api.model.Publicacao;
import com.minecity.api.service.impl.PublicacaoServiceImpl;

@RestController
@RequestMapping("/publicacoes")
public class PublicacaoResource {

	@Autowired
	private PublicacaoServiceImpl publicacaoServiceImpl;

	@PostMapping
	public ResponseEntity<?> novaPublicacao(@RequestBody Publicacao publicacao) {
		Publicacao publicacaoCriada = this.publicacaoServiceImpl.novaPublicacao(publicacao);
		return publicacaoCriada.equals(null) ? ResponseEntity.badRequest().build()
				: ResponseEntity.status(HttpStatus.CREATED).body(publicacaoCriada);
	}

	@GetMapping
	public ResponseEntity<?> obterPublicacoes() {
		List<Publicacao> publicacoes = this.publicacaoServiceImpl.obterPublicacoes();
		return ResponseEntity.ok().body(publicacoes);
	}

	@PostMapping("/comentario/{publicacaoId}")
	public ResponseEntity<?> novoComentario(@RequestBody Comentario comentario, @PathVariable String publicacaoId)
			throws Exception {
		Comentario comentarioAdicionado = this.publicacaoServiceImpl.novoComentario(comentario, publicacaoId);
		return comentarioAdicionado.equals(null) ? ResponseEntity.badRequest().build()
				: ResponseEntity.status(HttpStatus.CREATED).body(comentarioAdicionado);
	}

	@DeleteMapping("/{publicacaoId}")
	public ResponseEntity<?> excluirPublicacao(@PathVariable String publicacaoId) {
		boolean publicacaoIsExistente = this.publicacaoServiceImpl.publicacaoisExistente(publicacaoId);
		if (publicacaoIsExistente) {
			this.publicacaoServiceImpl.excluirPublicacao(publicacaoId);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{autor}")
	public Iterable<Publicacao> obterPublicacoesDoUsuario(@PathVariable String autor) {
		return this.publicacaoServiceImpl.obterPublicacoesDoUsuario(autor);
	}
	
	@DeleteMapping("/comentario/{publicacaoId}/{comentarioId}")
	public ResponseEntity<?> removerComentario(@PathVariable String publicacaoId, @PathVariable String comentarioId) throws Exception {
		this.publicacaoServiceImpl.excluirComentario(publicacaoId, comentarioId);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/curtida/{publicacaoId}")
	public ResponseEntity<?> adicionarCurtida(@PathVariable String publicacaoId, @RequestBody Curtida curtida) {
		this.publicacaoServiceImpl.novaCurtida(curtida, publicacaoId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/curtida/{publicacaoId}/{autor}")
	public ResponseEntity<?> removerCurtida(@PathVariable String publicacaoId, @PathVariable String autor) {
		this.publicacaoServiceImpl.removerCurtida(autor, publicacaoId);
		return ResponseEntity.ok().build();
	}

}
