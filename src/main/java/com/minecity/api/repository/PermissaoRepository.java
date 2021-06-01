package com.minecity.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minecity.api.model.Permissao; 

public interface PermissaoRepository extends MongoRepository<Permissao, Long> {

}
