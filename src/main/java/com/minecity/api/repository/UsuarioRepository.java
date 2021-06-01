package com.minecity.api.repository;

import java.util.List;
import java.util.Optional; 

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minecity.api.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	public Optional<Usuario> findByEmail(String email);
	public Optional<Usuario> findByUsernameIgnoreCase(String username);
	public boolean existsByUsernameIgnoreCase(String username);
	public boolean existsByEmailIgnoreCase(String email);
	public List<Usuario> findByUsernameStartsWithIgnoreCase(String starts);

}
