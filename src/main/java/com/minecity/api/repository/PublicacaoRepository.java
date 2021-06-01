package com.minecity.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minecity.api.model.Publicacao;

public interface PublicacaoRepository extends MongoRepository<Publicacao, String>{
	
	public Iterable<Publicacao> findAllByAutor(String autor);
	public Publicacao findByAutor(String autor);

}
