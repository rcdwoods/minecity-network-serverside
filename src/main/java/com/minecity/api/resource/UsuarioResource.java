package com.minecity.api.resource;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minecity.api.model.Usuario;
import com.minecity.api.service.impl.UsuarioServiceImpl;

@RestController
@RequestMapping("/usuarios")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UsuarioResource {

	@Autowired
	private UsuarioServiceImpl usuarioServiceImpl;

	@GetMapping("/{username}")
	@PreAuthorize("hasAuthority('ROLE_ACESSAR_PERFIL')")
	public ResponseEntity<?> getUsuario(@PathVariable String username) {
		Usuario usuarioObtido = this.usuarioServiceImpl.obterUsuarioPorNome(username);
		return usuarioObtido.equals(null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarioObtido);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_BASIC')")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) throws Exception {
		Usuario usuarioCadastrado = this.usuarioServiceImpl.cadastrarUsuario(usuario);
		return usuarioCadastrado.equals(null) ? ResponseEntity.status(HttpStatus.CONFLICT).build()
				: ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}

	@GetMapping("/existe/username/{username}")
	public boolean usernameIsExistente(@PathVariable String username) {
		return this.usuarioServiceImpl.usernameIsExistente(username);
	}

	@GetMapping("/existe/email/{email}")
	public boolean emailIsExistente(@PathVariable String email) {
		return this.usuarioServiceImpl.emailIsExistente(email);
	}

	@GetMapping("/pesquisa/{startsWith}")
	public ResponseEntity<?> pesquisarUsuario(@PathVariable String startsWith) {
		List<String> usuariosEncontrados = this.usuarioServiceImpl.pesquisaUsuarios(startsWith);
		return usuariosEncontrados.isEmpty() ? ResponseEntity.noContent().build()
				: ResponseEntity.ok(usuariosEncontrados);
	}

	@PostMapping("/amigos")
	public ResponseEntity<?> adicionarAmizadeEntreUsuarios(@RequestBody List<String> usernames) throws Exception {
		if (usernames.size() != 2) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		boolean amizadeIsCriada = this.usuarioServiceImpl.novaAmizade(usernames.get(0), usernames.get(1));
		return amizadeIsCriada ? ResponseEntity.status(HttpStatus.CREATED).build()
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@GetMapping("/{username}/amigos")
	public ResponseEntity<?> obterListaDeAmigos(@PathVariable String username, HttpServletRequest httpServletRequest) {
		List<String> listaDeAmigos = this.usuarioServiceImpl.obterListaDeAmigos(username);
		String token = httpServletRequest.getHeader("Authorization");
		System.out.println("ACCESS TOKEN:" + token.replace("Bearer ", ""));
		JwtAccessTokenConverter jwt = new JwtAccessTokenConverter();
		if (listaDeAmigos == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok().body(listaDeAmigos);
	}
	
	@GetMapping("/{username}/amigos/{friendname}")
	public ResponseEntity<?> pesquisarAmigos(@PathVariable String username, @PathVariable String friendname) {
		List<String> amigosEncontrados = this.usuarioServiceImpl.pesquisarAmigos(username, friendname);
		return amigosEncontrados == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(amigosEncontrados);
	}

	@PostMapping("/{username}/pedidosdeamizade/{autor}")
	public ResponseEntity<?> adicionarSolicitacaoDeAmizade(@PathVariable String username, @PathVariable String autor)
			throws Exception {
		boolean solicitacaoIsAdicionada = this.usuarioServiceImpl.addSolicitacaoDeAmizade(autor, username);
		return solicitacaoIsAdicionada ? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@DeleteMapping("/{username}/pedidosdeamizade/{autor}")
	public ResponseEntity<?> removerSolicitacaoDeAmizade(@PathVariable String username, @PathVariable String autor)
			throws Exception {
		boolean solicitacaoIsRemovida = this.usuarioServiceImpl.removerSolicitacaoDeAmizade(username, autor);
		return solicitacaoIsRemovida ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
	}

	@GetMapping("/amigos/{primeiroUsername}/{segundoUsername}")
	public ResponseEntity<?> obterAmigosEmComum(@PathVariable String primeiroUsername, @PathVariable String segundoUsername) {
		List<String> listaDeAmigosEmComum = this.usuarioServiceImpl.obterAmigosEmComum(primeiroUsername,
				segundoUsername);
		return listaDeAmigosEmComum == null ?
				ResponseEntity.noContent().build() : ResponseEntity.ok(listaDeAmigosEmComum);
	}

}
