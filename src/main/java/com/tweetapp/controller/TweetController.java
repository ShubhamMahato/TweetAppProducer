package com.tweetapp.controller;

import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TweetController {

    @Autowired
    TweetService tweetService;

    @PostMapping("/{username}/add")
    public ResponseEntity postTweet(@RequestBody Tweet tweet, @PathVariable String username) {
        log.info("START :: postTweet :: Tweet :: {}", tweet);
        if (tweet == null || StringUtils.isBlank(username) || tweet.getMessage()==null) {
            log.debug("END :: postTweet :: Tweet :: {} :: error :: {}", tweet, ReasonConstant.WRONG_INPUT);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        log.info("MID :: postTweet :: Tweet :: {}", tweet);
        UserLoginResponse userLogin = tweetService.postTweet(tweet, username);
        log.info("AFTER  :: postTweet :: userLogin :: {}", userLogin);
        if (userLogin.getError()!=null && !userLogin.getError().equalsIgnoreCase(ReasonConstant.POST_SAVED)) {
            log.info("START :: postTweet :: Tweet :: {} :: error :: {}", tweet, userLogin.getError());
            return ResponseEntity.badRequest().body(userLogin.getError());
        }
        log.info("END :: postTweet :: Tweet :: {}", tweet);
        return ResponseEntity.ok(userLogin);
    }

    @PutMapping("/{username}/update/{id}")
    public ResponseEntity updateTweet( @RequestBody Tweet tweetupdate, @PathVariable String username, @PathVariable String id) {
        log.info("START :: updateTweet :: username :: {} :: id :: {}", username, id);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(id)) {
            log.info("START :: updateTweet :: username :: {} :: id :: {} :: error :: {}", username, id, ReasonConstant.WRONG_INPUT);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        if(tweetupdate.getMessage()!=null && tweetupdate.getMessage().length()<=144) {
            Tweet tweet = Tweet.builder().message(tweetupdate.getMessage()).id(id).build();

            UserLoginResponse userLogin = tweetService.updateTweet(tweet, username);
            if (userLogin.getError()!=null && !userLogin.getError().equalsIgnoreCase(ReasonConstant.POST_SAVED)) {
                log.info("START :: updateTweet :: username :: {} :: id :: {} :: error :: {}", username, id, userLogin.getError());
                return ResponseEntity.badRequest().body(userLogin.getError());
            }
            log.info("END :: updateTweet :: username :: {} :: id :: {}", username, id);
            return ResponseEntity.ok(userLogin);
        }
        return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
    }

    @DeleteMapping("/{username}/delete/{id}")
    public ResponseEntity deleteTweet(@PathVariable String id, @PathVariable String username) {
        log.info("START :: deleteTweet :: username :: {} :: id :: {}", username, id);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(username)) {
            log.info("START :: deleteTweet :: username :: {} :: id :: {} :: error :: {}", username, id, ReasonConstant.WRONG_INPUT);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        Tweet tweet = Tweet.builder().id(id).build();
        UserLoginResponse userLogin = tweetService.deleteTweet(tweet, username);
        if (userLogin.getError()!=null && !userLogin.getError().equalsIgnoreCase(ReasonConstant.POST_SAVED)) {
            log.info("START :: deleteTweet :: username :: {} :: id :: {} :: error :: {}", username, id, userLogin.getError());
            return ResponseEntity.badRequest().body(userLogin.getError());
        }
        log.info("END :: deleteTweet :: username :: {} :: id :: {}", username, id);
        return ResponseEntity.ok(userLogin);
    }

    @PostMapping("/{username}/reply/{id}")
    public ResponseEntity replyTweet(@RequestBody Tweet tweet, @PathVariable String id, @PathVariable String username) {
        log.info("START :: replyTweet :: tweet :: {} id :: {} username :: {}", tweet, id, username);
        if (tweet == null || StringUtils.isBlank(username) || StringUtils.isBlank(id)) {
            log.debug("START :: replyTweet :: tweet :: {} id :: {} username :: {} :: error :: {}", tweet, id, username, ReasonConstant.WRONG_INPUT);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        UserLoginResponse userLogin = tweetService.replyTweet(tweet, id, username);
        if (userLogin.getError()!=null && !userLogin.getError().equalsIgnoreCase(ReasonConstant.POST_SAVED)) {
            log.debug("START :: replyTweet :: tweet :: {} id :: {} username :: {} :: error :: {}", tweet, id, username, userLogin.getError());
            return ResponseEntity.badRequest().body(userLogin.getError());
        }
        log.info("END :: replyTweet :: tweet :: {} id :: {} username :: {}", tweet, id, username);
        return ResponseEntity.ok(userLogin);
    }

    @PutMapping("/{username}/like/{id}")
    public ResponseEntity likeTweet(@PathVariable String id, @PathVariable String username) {
        log.info("START :: likeTweet :: id :: {} username :: {}", id, username);

        if (StringUtils.isBlank(username)) {
            log.debug("START :: likeTweet :: id :: {} username :: {} :: error :: {}", id, username, ReasonConstant.WRONG_INPUT);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        UserLoginResponse userLogin = tweetService.likeTweet(id, username);
        if (userLogin.getError()!=null &&!userLogin.getError().equalsIgnoreCase(ReasonConstant.POST_SAVED)) {
            log.debug("START :: likeTweet :: id :: {} username :: {} :: error :: {}", id, username, userLogin.getError());

            return ResponseEntity.badRequest().body(userLogin.getError());
        }
        log.info("END :: likeTweet :: id :: {} username :: {}", id, username);

        return ResponseEntity.ok(userLogin);
    }

    @GetMapping("/{username}/all")
    public ResponseEntity getAllTweets(@PathVariable String username) {
        log.info("START :: getAllTweets :: username :: {}", username);
        List<Tweet> tweets = tweetService.getAllTweets(username);
        if (tweets.isEmpty()) {
            log.debug("END :: getAllTweets :: username :: {}", username);
            return ResponseEntity.badRequest().body(ReasonConstant.NO_POST_AVAILABLE);
        }
        log.debug("END :: getAllTweets :: username :: {}", username);
        return ResponseEntity.ok().body(tweets);
    }

    @GetMapping("/{username}")
    public ResponseEntity getAllUsersTweets(@PathVariable String username) {
        log.info("START :: getAllUsersTweets :: username :: {}", username);
        List<Tweet> tweets = tweetService.getAllUsersTweets(username);
        if (tweets.isEmpty()) {
            log.info("END :: getAllUsersTweets :: username :: {} :: error :: {}", username,ReasonConstant.NO_POST_AVAILABLE);
            return ResponseEntity.badRequest().body(ReasonConstant.NO_POST_AVAILABLE);
        }
        log.info("END :: getAllUsersTweets :: username :: {} :: tweets :: {}", username,tweets);
        return ResponseEntity.ok().body(tweets);
    }

}
