package com.tweetapp.dao;

import com.tweetapp.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetDao extends MongoRepository<Tweet,String> {
    List<Tweet> findByUsername(String username);
}
