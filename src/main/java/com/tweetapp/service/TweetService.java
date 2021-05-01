package com.tweetapp.service;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.response.UserLoginResponse;

import java.util.List;

public interface TweetService {
    UserLoginResponse postTweet(Tweet tweet, String username);
    UserLoginResponse updateTweet(Tweet tweet, String username);
    UserLoginResponse deleteTweet(Tweet tweet, String username);
    UserLoginResponse replyTweet(Tweet tweet, String id, String username);
    UserLoginResponse likeTweet(String id, String username);
    List<Tweet> getAllTweets(String username);
    List<Tweet> getAllUsersTweets(String username);
}
