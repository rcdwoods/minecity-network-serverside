package com.minecity.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.minecity.api.model.Permissao;
import com.minecity.api.model.Usuario;
import com.minecity.api.repository.PermissaoRepository;
import com.minecity.api.repository.UsuarioRepository;

@SpringBootApplication
public class MinecityApplication implements CommandLineRunner {
	
	@Autowired
	private Usuario usuario;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PermissaoRepository permissaoRepository;
	@Autowired
	private Permissao permissao;

	public static void main(String[] args) {
		SpringApplication.run(MinecityApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		// Define o codigo das permissoes e suas descriçoes.
//		Permissao permissao = new Permissao();
//		Permissao permissao2 = new Permissao();
//		Permissao permissao3 = new Permissao();
//		Permissao permissao4 = new Permissao();
//		Permissao permissao5 = new Permissao();
//		Permissao permissao6 = new Permissao();
//		Permissao permissao7 = new Permissao();
//		Permissao permissao8 = new Permissao();
//		// Salva o código das permissoes e suas descrições.
//		// Permissao 1
//		permissao.setCodigo((long) 1);
//		permissao.setDescricao("ROLE_CADASTRAR_CATEGORIA");
//		// Permissao 2
//		permissao2.setCodigo((long) 2);
//		permissao2.setDescricao("ROLE_PESQUISAR_CATEGORIA");
//		// Permissao 3
//		permissao3.setCodigo((long) 3);
//		permissao3.setDescricao("ROLE_CADASTRAR_PESSOA");
//		// Permissao 4
//		permissao4.setCodigo((long) 4);
//		permissao4.setDescricao("ROLE_REMOVER_PESSOA");
//		// Permissao 5
//		permissao5.setCodigo((long) 5);
//		permissao5.setDescricao("ROLE_PESQUISAR_PESSOA");
//		// Permissao 6
//		permissao6.setCodigo((long) 6);
//		permissao6.setDescricao("ROLE_CADASTRAR_LANCAMENTO");
//		// Permissao 7
//		permissao7.setCodigo((long) 7);
//		permissao7.setDescricao("ROLE_REMOVER_LANCAMENTO");
//		// Permissoa 8 
//		permissao8.setCodigo((long) 8);
//		permissao8.setDescricao("ROLE_PESQUISAR_LANCAMENTO");
		
//		permissaoRepository.save(permissao);
//		permissaoRepository.save(permissao2);
//		permissaoRepository.save(permissao3);
//		permissaoRepository.save(permissao4);
//		permissaoRepository.save(permissao5);
//		permissaoRepository.save(permissao6);
//		permissaoRepository.save(permissao7);
//		permissaoRepository.save(permissao8);

//		Usuario angular = new Usuario();
//		usuario.setUsername("Angular");
//		usuario.setEmail("angular@minecity.com");
//		usuario.setSenha("$2a$10$obWqmJ83P1sOB7JZk0rv8uj6bSRIkOJ55ADb.FEl9xlCT3Wg3fJuy");
//		usuario.setPermissoes(permissaoRepository.findAll());
//		usuarioRepository.save(usuario);

	}

}
