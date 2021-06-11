package com.minecity.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
		this.primeiroUsuario.get().addAmigo("Richard");
		this.primeiroUsuario.get().addAmigo("Roberto");
		this.primeiroUsuario.get().addAmigo("George");
		this.primeiroUsuario.get().addAmigo("Rose");
		this.primeiroUsuario.get().addAmigo("Marcelo");
		
		List<String> listaDeAmigosEsperada = new ArrayList<>();
		listaDeAmigosEsperada.add("Richard");
		listaDeAmigosEsperada.add("Roberto");
		listaDeAmigosEsperada.add("George");
		listaDeAmigosEsperada.add("Rose");
		listaDeAmigosEsperada.add("Marcelo");
		
		List<String> listaDeAmigos = this.usuarioService.obterListaDeAmigos("Gabriel");
		assertEquals(listaDeAmigosEsperada, listaDeAmigos);
	}
	
	@Test
	void deveRetornarListaDeAmigosEmComumEsperada() {
		this.primeiroUsuario.get().addAmigo("Richard");
		this.primeiroUsuario.get().addAmigo("Roberto");
		this.primeiroUsuario.get().addAmigo("George");
		this.primeiroUsuario.get().addAmigo("Rose");
		this.primeiroUsuario.get().addAmigo("Marcelo");
		
		this.segundoUsuario.get().addAmigo("José");
		this.segundoUsuario.get().addAmigo("Roberto");
		this.segundoUsuario.get().addAmigo("Claudio");
		this.segundoUsuario.get().addAmigo("Marcelo");
		
		List<String> listaDeAmigosEmComumEsperada = new ArrayList<>();
		listaDeAmigosEmComumEsperada.add("Roberto");
		listaDeAmigosEmComumEsperada.add("Marcelo");
		
		List<String> listaDeAmigosEmComum = this.usuarioService.obterAmigosEmComum("Gabriel", "Henrique");
		assertEquals(listaDeAmigosEmComumEsperada, listaDeAmigosEmComum);
	}
	
	@Test
	void deveEfetuarNovaAmizade() throws Exception {
		this.primeiroUsuario.get().addAmigo("Richard");
		this.segundoUsuario.get().addAmigo("Rafael");
		
		boolean amizadeIsEfetuada = this.usuarioService.novaAmizade("Henrique", "Gabriel");
		
		assertEquals(true, amizadeIsEfetuada);
	}

	
	@Test
	void naoDeveEfetuarNovaAmizade_QuandoUsuariosJaSaoAmigos() throws Exception {
		this.primeiroUsuario.get().addAmigo("Henrique");
		this.segundoUsuario.get().addAmigo("Gabriel");
		
		boolean amizadeIsEfetuada = this.usuarioService.novaAmizade("Henrique", "Gabriel");
		
		assertEquals(false, amizadeIsEfetuada);
	}

}
