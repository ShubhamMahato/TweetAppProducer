package com.tweetapp.dao;

import com.tweetapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByEmailAndPassword(String email,String password);
}
