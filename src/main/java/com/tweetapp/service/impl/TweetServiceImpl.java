package com.tweetapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.dao.LikedTweetDao;
import com.tweetapp.dao.TweetDao;
import com.tweetapp.model.LikedTweet;
import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetEventType;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.TweetService;
import com.tweetapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.*;

@Service
@Slf4j
public class TweetServiceImpl implements TweetService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TweetDao tweetDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LikedTweetDao likedTweetDao;


    public UserLoginResponse postTweet(Tweet tweet, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        Tweet tweet1;
        if (userLoginResponse.getError() == null) {
            Date date = new Date();
            tweet1 = Tweet.builder().id(username + "~" + date.toString()).likes(null).message(tweet.getMessage()).postDate(date).tweetEventType(TweetEventType.NEW).username(username).build();
            String id = tweet1.getId();
            String value = null;
            try {
                value = objectMapper.writeValueAsString(tweet1);
            } catch (JsonProcessingException e) {
                userLoginResponse.setError(ReasonConstant.WRONG_INPUT);
            }
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.sendDefault(id, value);
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    userLoginResponse.setError(ReasonConstant.UNABLE_TO_SAVE_POST);

                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    userLoginResponse.setError(ReasonConstant.POST_SAVED);

                }
            });
        }
        return userLoginResponse;
    }

    public UserLoginResponse updateTweet(Tweet tweet, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        Tweet tweet1;
        if (userLoginResponse.getError() == null) {
            tweet.setTweetEventType(TweetEventType.UPDATE);
            String id = tweet.getId();
            String value = null;
            try {
                value = objectMapper.writeValueAsString(tweet);
            } catch (JsonProcessingException e) {
                userLoginResponse.setError(ReasonConstant.WRONG_INPUT);
            }
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.sendDefault(id, value);
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    userLoginResponse.setError(ReasonConstant.UNABLE_TO_SAVE_POST);

                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    userLoginResponse.setError(ReasonConstant.POST_SAVED);

                }
            });
        }
        return userLoginResponse;
    }

    public UserLoginResponse deleteTweet(Tweet tweet, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        Tweet tweet1;
        if (userLoginResponse.getError() == null) {
            tweet1 = Tweet.builder().id(tweet.getId()).tweetEventType(TweetEventType.DELETE).build();
            String id = tweet1.getId();
            String value = null;
            try {
                value = objectMapper.writeValueAsString(tweet1);
            } catch (JsonProcessingException e) {
                userLoginResponse.setError(ReasonConstant.WRONG_INPUT);
            }
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.sendDefault(id, value);
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    userLoginResponse.setError(ReasonConstant.UNABLE_TO_SAVE_POST);

                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    userLoginResponse.setError(ReasonConstant.POST_SAVED);

                }
            });
        }
        return userLoginResponse;
    }

    public UserLoginResponse replyTweet(Tweet tweet, String id, String username) {
        UserLoginResponse userLoginResponse = userService.findUser(username);
        Optional<Tweet> tweet1=tweetDao.findById(id);
        if (userLoginResponse.getError() == null && tweet1.isPresent()) {
            Date date = new Date();
            Tweet tweetReply = Tweet.builder().id(username + "~" + date.toString()).message(tweet.getMessage()).postDate(date).username(username).build();
            if(tweet1.get().getReplyTweets()==null || tweet1.get().getReplyTweets().isEmpty()) {
                tweet1.get().setReplyTweets(Collections.singletonList(tweetReply));
            }
            else{
                tweet1.get().getReplyTweets().add(tweetReply);
            }
            tweet1.get().setTweetEventType(TweetEventType.NEW);
            String id1 = tweet1.get().getId();
            String value = null;
            try {
                value = objectMapper.writeValueAsString(tweet1);
            } catch (JsonProcessingException e) {
                userLoginResponse.setError(ReasonConstant.WRONG_INPUT);
            }
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.sendDefault(id1, value);
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    userLoginResponse.setError(ReasonConstant.UNABLE_TO_SAVE_POST);

                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    userLoginResponse.setError(ReasonConstant.POST_SAVED);
                }
            });
        }
        return userLoginResponse;
    }

    @Override
    public UserLoginResponse likeTweet(String id, String username) {
        log.info("START :: likeTweet :: id :: {}  :: username :: {}",id,username);
        UserLoginResponse userLoginResponse = userService.findUser(username);
        List<Tweet> tweetList=tweetDao.findAll();
        Tweet tweet=new Tweet();
        for(Tweet t:tweetList){
            if(t.getId().equalsIgnoreCase(id)){
                tweet=t;
            }
        }
        log.info("START :: likeTweet ::  :: tweet :: {}",tweet);
        if(userLoginResponse.getError() == null && tweet.getId()!=null){
            LikedTweet likedTweet=LikedTweet.builder().id(id).username(username).build();
            likedTweetDao.save(likedTweet);
            int likes=tweet.getLikes()!=null ? tweet.getLikes(): 0;
            tweet.setLikes(likes==0?1:likes+1);
            tweetDao.save(tweet);
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
        List<Tweet> tweets=tweetDao.findAll();
        List<LikedTweet> likedTweet=likedTweetDao.findByUsername(username);

        tweets.forEach(x-> likedTweet.forEach(p->{
            if(p.getId().equals(x.getId())){
                x.setLiked(true);
            }
        }));

        System.out.println(tweets);
        return tweets;
    }

    @Override
    public List<Tweet> getAllUsersTweets(String username) {
        List<Tweet> tweets=tweetDao.findByUsername(username);
        List<LikedTweet> likedTweet=likedTweetDao.findByUsername(username);

        tweets.forEach(x-> likedTweet.forEach(p->{
            if(p.getId().equals(x.getId())){
                x.setLiked(true);
            }
        }));
        log.info("getALlUsersTweets :: tweets :: {}",tweets);
        return tweets;
    }

}
