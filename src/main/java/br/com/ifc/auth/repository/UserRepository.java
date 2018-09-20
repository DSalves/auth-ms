package br.com.ifc.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ifc.auth.model.User;

public interface UserRepository extends MongoRepository<User, String>{
}
