package com.minecity.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.minecity.api.model.Usuario;
import com.minecity.api.repository.PermissaoRepository;
import com.minecity.api.repository.UsuarioRepository;
import com.minecity.api.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PermissaoRepository permissaoRepository;

	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public Usuario obterUsuarioPorNome(String username) {
		return this.usuarioRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
	}

	public boolean usernameIsExistente(String username) {
		return this.usuarioRepository.existsByUsernameIgnoreCase(username);
	}

	public boolean emailIsExistente(String email) {
		return this.usuarioRepository.existsByEmailIgnoreCase(email);
	}

	public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (!this.usernameIsExistente(usuario.getUsername()) && !this.emailIsExistente(usuario.getUsername())) {
			usuario.addPermissao(permissaoRepository.findById((long) 1).orElseThrow(() -> new Exception()));
			usuario.addPermissao(permissaoRepository.findById((long) 2).orElseThrow(() -> new Exception()));
			usuario.setSenha(encoder.encode(usuario.getSenha()));
			return this.usuarioRepository.insert(usuario);
		}
		return null;
	}

	public List<String> pesquisaUsuarios(String startsWith) {
		List<Usuario> usuarios = this.usuarioRepository.findByUsernameStartsWithIgnoreCase(startsWith);
		List<String> usernames = usuarios.stream().map(usuario -> usuario.getUsername()).collect(Collectors.toList());
		return usernames;
	}

	// usernameAlvo é o nome de usuário para quem a solicitação de amizade é
	// direcionada.
	// autor é o nome de usuário do gerador da solicitação.
	public boolean addSolicitacaoDeAmizade(String autor, String usernameAlvo) throws Exception {
		Usuario usuario = this.usuarioRepository.findByUsernameIgnoreCase(usernameAlvo)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

		if (!usuario.getSolicitacoesDeAmizade().contains(autor) && !this.usuariosAreAmigos(autor, usernameAlvo)) {
			usuario.addSolicitacaoDeAmizade(autor);
			this.usuarioRepository.save(usuario);
			return true;
		}
		return false;
	}

	// usernameAlvo é o nome de usuário para quem a solicitação de amizade é
	// direcionada.
	// autor é o nome de usuário do gerador da solicitação.
	public boolean removerSolicitacaoDeAmizade(String usernameAlvo, String autor) throws Exception {
		Usuario usuario = this.usuarioRepository.findByUsernameIgnoreCase(usernameAlvo)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

		if (usuario.getSolicitacoesDeAmizade().contains(autor)) {
			List<String> novasSolicitacoesDeAmizade = this.removeItemDeUmaLista(usuario.getSolicitacoesDeAmizade(),
					autor);
			usuario.setSolicitacoesDeAmizade(novasSolicitacoesDeAmizade);
			this.usuarioRepository.save(usuario);
			return true;
		}
		return false;
	}

	// Adiciona um novo amigo à lista de amigos do usernameAlvo.
	public boolean novaAmizade(String primeiroUsername, String segundoUsername) throws Exception {
		Usuario usuarioUm = this.usuarioRepository.findByUsernameIgnoreCase(primeiroUsername)
				.orElseThrow(() -> new IllegalArgumentException("Um dos usuários é inexistente."));
		Usuario usuarioDois = this.usuarioRepository.findByUsernameIgnoreCase(segundoUsername)
				.orElseThrow(() -> new IllegalArgumentException("Um dos usuários é inexistente."));

		if (!this.usuariosAreAmigos(primeiroUsername, segundoUsername)) {
			usuarioUm.addAmigo(segundoUsername);
			usuarioDois.addAmigo(primeiroUsername);
			this.usuarioRepository.save(usuarioUm);
			this.usuarioRepository.save(usuarioDois);
			this.removerSolicitacaoDeAmizade(primeiroUsername, segundoUsername);
			this.removerSolicitacaoDeAmizade(segundoUsername, primeiroUsername);
			return true;
		}
		return false;
	}

	public void desfazerAmizade(String primeiroUsername, String segundoUsername) {
		Usuario primeiroUsuario = this.usuarioRepository.findByUsernameIgnoreCase(primeiroUsername)
				.orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
		Usuario segundoUsuario = this.usuarioRepository.findByUsernameIgnoreCase(segundoUsername)
				.orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));

		primeiroUsuario.setAmigos(this.removeItemDeUmaLista(primeiroUsuario.getAmigos(), segundoUsername));
		segundoUsuario.setAmigos(this.removeItemDeUmaLista(segundoUsuario.getAmigos(), primeiroUsername));

		this.usuarioRepository.save(primeiroUsuario);
		this.usuarioRepository.save(segundoUsuario);
	}

	// Recebe o nome do usuário e retorna uma lista com seus amigos.
	public List<String> obterListaDeAmigos(String username) {
		Usuario usuario = this.usuarioRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
		return usuario.getAmigos();
	}

	// Recebe o nome de dois usuários e retorna uma lista de seus amigos em comum.
	public List<String> obterAmigosEmComum(String primeiroUsername, String segundoUsername) {
		if (!this.usernameIsExistente(primeiroUsername) || !this.usernameIsExistente(segundoUsername)) {
			return null;
		}

		List<String> primeiraListaDeAmigos = this.obterListaDeAmigos(primeiroUsername);
		List<String> segundaListaDeAmigos = this.obterListaDeAmigos(segundoUsername);

		List<String> listaDeAmigosEmComum = primeiraListaDeAmigos.stream()
				.filter(amigo -> segundaListaDeAmigos.contains(amigo)).collect(Collectors.toList());

		return listaDeAmigosEmComum;
	}

	// Recebe o nome de um usuário 'usernameAlvo" e uma palavra "startsWith".
	// Pesquisa e retorna nome de amigos desse usuário que começam com a palavra
	// "startsWith"
	public List<String> pesquisarAmigos(String usernameAlvo, String startsWith) {
		Usuario usuarioAlvo = this.usuarioRepository.findByUsernameIgnoreCase(usernameAlvo)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

		List<Usuario> usuarios = this.usuarioRepository.findByUsernameStartsWithIgnoreCase(startsWith);

		List<String> amigosEncontrados = usuarios.stream()
				.filter(usuario -> usuarioAlvo.getAmigos().contains(usuario.getUsername()))
				.map(usuario -> usuario.getUsername()).collect(Collectors.toList());

		return amigosEncontrados;
	}

	// Verifica se dois usuários são amigos, e retorna uma resposta do tipo boolean.
	public boolean usuariosAreAmigos(String primeiroUsername, String segundoUsername) {
		Optional<Usuario> usuario = this.usuarioRepository.findByUsernameIgnoreCase(primeiroUsername);
		return this.containsIgnoreCase(usuario.get().getAmigos(), segundoUsername);
	}

	// Recebe uma lista de String e uma string.
	// Em seguida, confere se a string pertence a lista ignorando letras maiúsculas
	// e minúsculas.
	public boolean containsIgnoreCase(List<String> lista, String string) {
		List<String> listaDeCorrespondencias = lista.stream().filter(item -> item.equalsIgnoreCase(string))
				.collect(Collectors.toList());
		return listaDeCorrespondencias.isEmpty() ? false : true;
	}

	public List<String> removeItemDeUmaLista(List<String> lista, String itemASerRemovido) {
		List<String> novaListaSemItem = lista.stream().filter(item -> !item.equalsIgnoreCase(itemASerRemovido))
				.collect(Collectors.toList());
		return novaListaSemItem;
	}
}
