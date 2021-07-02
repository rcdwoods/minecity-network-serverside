package com.minecity.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.minecity.api.model.Usuario;
import com.minecity.api.repository.UsuarioRepository;

@SpringBootTest
class UsuarioServiceImplTest {

	private UsuarioRepository usuarioRepository;
	private Optional<Usuario> primeiroUsuario;
	private Optional<Usuario> segundoUsuario;
	@Autowired
	private UsuarioServiceImpl usuarioService;

	@BeforeEach
	public void setup() {
		this.usuarioRepository = mock(UsuarioRepository.class);
		this.usuarioService = new UsuarioServiceImpl(this.usuarioRepository);

		Usuario primeiroUsuario = new Usuario();
		Usuario segundoUsuario = new Usuario();

		this.primeiroUsuario = Optional.ofNullable(primeiroUsuario);
		this.segundoUsuario = Optional.ofNullable(segundoUsuario);

		when(this.usuarioRepository.findByUsernameIgnoreCase("Gabriel")).thenReturn(this.primeiroUsuario);
		when(this.usuarioRepository.existsByUsernameIgnoreCase("Gabriel")).thenReturn(true);
		when(this.usuarioRepository.findByUsernameIgnoreCase("Henrique")).thenReturn(this.segundoUsuario);
		when(this.usuarioRepository.existsByUsernameIgnoreCase("Henrique")).thenReturn(true);
	}

	@Test
	void deveRetornarListaDeAmigosEsperada() {
		this.primeiroUsuario.get().setAmigos(Arrays.asList("Richard", "Roberto", "George", "Rose", "Marcelo"));

		List<String> listaDeAmigosEsperada = Arrays.asList("Richard", "Roberto", "George", "Rose", "Marcelo");
		List<String> listaDeAmigos = this.usuarioService.obterListaDeAmigos("Gabriel");

		assertEquals(listaDeAmigosEsperada, listaDeAmigos);
	}

	@Test
	void deveRetornarListaDeAmigosEmComumEsperada() {
		this.primeiroUsuario.get().setAmigos(Arrays.asList("Richard", "Roberto", "George", "Rose", "Marcelo"));
		this.segundoUsuario.get().setAmigos(Arrays.asList("José", "Roberto", "Claudio", "Marcelo"));

		List<String> listaDeAmigosEmComumEsperada = Arrays.asList("Roberto", "Marcelo");
		List<String> listaDeAmigosEmComum = this.usuarioService.obterAmigosEmComum("Gabriel", "Henrique");

		assertEquals(listaDeAmigosEmComumEsperada, listaDeAmigosEmComum);
	}

	@Test
	void deveEfetuarNovaAmizade() throws Exception {
		this.primeiroUsuario.get().addAmigo("Richard");
		this.segundoUsuario.get().addAmigo("Rafael");

		boolean amizadeIsEfetuada = this.usuarioService.novaAmizade("Gabriel", "Henrique");

		assertEquals(true, amizadeIsEfetuada);
	}

	@Test
	void naoDeveEfetuarNovaAmizade_QuandoUsuariosJaSaoAmigos() throws Exception {
		this.primeiroUsuario.get().addAmigo("Henrique");
		this.segundoUsuario.get().addAmigo("Gabriel");

		boolean amizadeIsEfetuada = this.usuarioService.novaAmizade("Gabriel", "Henrique");

		assertEquals(false, amizadeIsEfetuada);
	}

	@Test
	void deveRetornarQueUsuariosSaoAmigosIgnorandoCase() {
		this.primeiroUsuario.get().setAmigos(Arrays.asList("Rodrigo", "Ezequiel", "Henrique"));

		boolean usuariosAreAmigos = this.usuarioService.usuariosAreAmigos("Gabriel", "rodrigo");
		assertEquals(true, usuariosAreAmigos);
	}

	@Test
	void deveRetornarQueUsuariosNaoSaoAmigosIgnorandoCase() {
		this.primeiroUsuario.get().setAmigos(Arrays.asList("Rodrigo", "Ezequiel", "Henrique"));

		boolean usuariosAreAmigos = this.usuarioService.usuariosAreAmigos("Gabriel", "Marcelo");
		assertEquals(false, usuariosAreAmigos);
	}

	@Test
	void devePesquisarERetornarAmigosQueComecamComR() {
		Usuario primeiroUsuario = new Usuario("Roberto", null, null, Arrays.asList("Roberto", "Leonardo", "Rose"), 0,
				0);
		Usuario segundoUsuario = new Usuario("Roberto", null, null, null, 0, 0);
		Usuario terceiroUsuario = new Usuario("Rose", null, null, null, 0, 0);

		Optional<Usuario> primeiroUsuarioOpcional = Optional.ofNullable(primeiroUsuario);

		when(this.usuarioRepository.findByUsernameIgnoreCase("Richard")).thenReturn(primeiroUsuarioOpcional);
		when(this.usuarioRepository.findByUsernameStartsWithIgnoreCase("r"))
				.thenReturn(Arrays.asList(segundoUsuario, terceiroUsuario));

		List<String> listaDeAmigosEsperada = Arrays.asList("Roberto", "Rose");
		List<String> listaDeAmigosEncontrados = this.usuarioService.pesquisarAmigos("Richard", "r");
		assertEquals(listaDeAmigosEsperada, listaDeAmigosEncontrados);
	}

	@Test
	void deveDesfazerAmizadeEntreUsuarios() {
		List<String> listaDeAmigosDoPrimeiroUsuario = Arrays.asList("Richard", "Roberto", "Henrique", "Claudio",
				"Fernando");
		List<String> listaDeAmigosDoSegundoUsuario = Arrays.asList("Ricardo", "Rose", "Mari", "Gleison", "Gabriel");

		List<String> novaListaDeAmigosDoPrimeiroUsuario = this.usuarioService
				.removeItemDeUmaLista(listaDeAmigosDoPrimeiroUsuario, "Henrique");
		List<String> novaListaDeAmigosDoSegundoUsuario = this.usuarioService
				.removeItemDeUmaLista(listaDeAmigosDoSegundoUsuario, "Gabriel");

		List<String> novaListaDeAmigosDoPrimeiroUsuarioEsperada = Arrays.asList("Richard", "Roberto", "Claudio",
				"Fernando");
		List<String> novaListaDeAmigosDoSegundoUsuarioEsperada = Arrays.asList("Ricardo", "Rose", "Mari", "Gleison");

		assertEquals(novaListaDeAmigosDoPrimeiroUsuarioEsperada, novaListaDeAmigosDoPrimeiroUsuario);
		assertEquals(novaListaDeAmigosDoSegundoUsuarioEsperada, novaListaDeAmigosDoSegundoUsuario);
	}

}
