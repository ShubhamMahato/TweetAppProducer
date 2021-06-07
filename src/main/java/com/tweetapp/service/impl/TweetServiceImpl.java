package com.tweetapp.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.model.LikedTweet;
import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetServiceImpl implements TweetService {

    @Autowired
    DynamoDBMapper dynamoDBMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;



    public UserLoginResponse postTweet(Tweet tweet, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        Tweet tweet1;
        if (userLoginResponse.getError() == null) {
            Date date = new Date();
            tweet1 = Tweet.builder().likes(null).message(tweet.getMessage()).postDate(date).username(username).build();
            dynamoDBMapper.save(tweet1);
        }
        return userLoginResponse;
    }

    public UserLoginResponse updateTweet(Tweet tweet, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        if (userLoginResponse.getError() == null) {
            Tweet tweet1=dynamoDBMapper.load(Tweet.class,tweet.getId());
            if(tweet1!=null && tweet.getMessage() !=null ) {
                Tweet tweetToSave = Tweet.builder().id(tweet1.getId()).message(tweet.getMessage()).postDate(tweet1.getPostDate()).
                        replyTweets(tweet1.getReplyTweets()).username(tweet1.getUsername()).likes(tweet1.getLikes()).build();
                dynamoDBMapper.save(tweetToSave);
            }
        }
        return userLoginResponse;
    }

    public UserLoginResponse deleteTweet(Tweet tweet, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        if (userLoginResponse.getError() == null) {
            List<Tweet> tweetList=dynamoDBMapper.scan(Tweet.class,new DynamoDBScanExpression());
            Tweet tweet1=new Tweet();
            for(Tweet t:tweetList){
                log.info(t.getId()+ "     tweet    "+ tweet.getId());
                if(t.getId().equalsIgnoreCase(tweet.getId())){
                    tweet1=t;
                }
            }

            log.info("END :: deleteTweet :: deleted :  {}",tweet1);
            if(tweet1.getId()!=null) {
                dynamoDBMapper.delete(tweet1);
            }
        }
        return userLoginResponse;
    }

    public UserLoginResponse replyTweet(Tweet tweet, String id, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        Tweet tweet1=dynamoDBMapper.load(Tweet.class,id);
        if (userLoginResponse.getError() == null && tweet1!=null) {
            Date date = new Date();
            Tweet tweetReply = Tweet.builder().message(tweet.getMessage()).postDate(date).username(username).build();
            if(tweet1.getReplyTweets()==null || tweet1.getReplyTweets().isEmpty()) {
                tweet1.setReplyTweets(Collections.singletonList(tweetReply));
            }
            else{
                tweet1.getReplyTweets().add(tweetReply);
            }
                Tweet tweetToSave = Tweet.builder().id(tweet1.getId()).message(tweet1.getMessage()).postDate(tweet1.getPostDate()).
                        replyTweets(tweet1.getReplyTweets()).username(tweet1.getUsername()).likes(tweet1.getLikes()).build();
                dynamoDBMapper.save(tweetToSave);

        }
        return userLoginResponse;
    }

    @Override
    public UserLoginResponse likeTweet(String id, String username) {
        log.info("START :: likeTweet :: id :: {}  :: username :: {}",id,username);
        UserLoginResponse userLoginResponse = userService.findUser(username);
        List<Tweet> tweetList=dynamoDBMapper.scan(Tweet.class,new DynamoDBScanExpression());
        Tweet tweet=new Tweet();
        for(Tweet t:tweetList){
            if(t.getId().equalsIgnoreCase(id)){
                tweet=t;
            }
        }
        log.info("START :: likeTweet ::  :: tweet :: {}",tweet);
        if(userLoginResponse.getError() == null && tweet.getId()!=null){
            LikedTweet likedTweet=LikedTweet.builder().id(id).username(username).build();
            dynamoDBMapper.save(likedTweet);
            int likes=tweet.getLikes()!=null ? tweet.getLikes(): 0;
            tweet.setLikes(likes==0?1:likes+1);
            dynamoDBMapper.save(tweet);
            userLoginResponse.setError(ReasonConstant.POST_SAVED);
        }
        else{
            userLoginResponse.setError(ReasonConstant.WRONG_INPUT);
        }
        log.info("END :: likeTweet ::  :: tweet :: {}",tweet);
        return userLoginResponse;
    }

    @Override
    public List<Tweet> getAllTweets(String username) {
        List<Tweet> tweets=dynamoDBMapper.scan(Tweet.class,new DynamoDBScanExpression());

        List<LikedTweet> likedTweet=dynamoDBMapper.scan(LikedTweet.class,new DynamoDBScanExpression());
        likedTweet.stream().filter(x->x.getUsername().equalsIgnoreCase(username)).collect(Collectors.toList());

        tweets.forEach(x-> likedTweet.forEach(p->{
            if(p.getId().equals(x.getId())){
                x.setLiked(true);
            }
        }));
        return tweets;
    }

    @Override
    public List<Tweet> getAllUsersTweets(String username) {
        List<Tweet> tweets=dynamoDBMapper.scan(Tweet.class,new DynamoDBScanExpression());
        tweets.stream().filter(x->x.getUsername().equalsIgnoreCase(username)).collect(Collectors.toList());
        List<LikedTweet> likedTweet=dynamoDBMapper.scan(LikedTweet.class,new DynamoDBScanExpression());
        likedTweet.stream().filter(x->x.getUsername().equalsIgnoreCase(username)).collect(Collectors.toList());

        tweets.forEach(x-> likedTweet.forEach(p->{
            if(p.getId().equals(x.getId())){
                x.setLiked(true);
            }
        }));
        log.info("getALlUsersTweets :: tweets :: {}",tweets);
        return tweets;
    }

}
