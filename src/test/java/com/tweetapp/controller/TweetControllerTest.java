package com.tweetapp.controller;

import com.tweetapp.model.Tweet;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.TweetService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TweetControllerTest {

    @InjectMocks TweetController tweetController;

    @Mock
    TweetService tweetService;

    @Test
    public void postTweet(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").message("sdafdsfsdfs").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").build();

        Mockito.when(tweetService.postTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.postTweet(tweet,username);
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void postTweet_NullTweet(){
        Tweet tweet=null;
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").build();

        Mockito.when(tweetService.postTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.postTweet(tweet,username);
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void postTweet_Error(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").message("sdafdsfsdfs").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.postTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.postTweet(tweet,username);
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void updateTweet(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").message("sdafdsfsdfs").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.updateTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.updateTweet(tweet,username,"dwdwwd");
        Assert.assertNotNull(res.getBody());
    }
    @Test
    public void updateTweet_blankUsername(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").message("sdafdsfsdfs").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.updateTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.updateTweet(tweet,null,"dwdwwd");
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void updateTweet_NullMessage(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.updateTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.updateTweet(tweet,null,"dwdwwd");
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void deleteTweet(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.deleteTweet(Mockito.any(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.deleteTweet(null,"dwdwwd");
        Assert.assertNotNull(res.getBody());

    }

    @Test
    public void replyTweet(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").postDate(new Date()).build();
        String username="dadadada";
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.replyTweet(Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.replyTweet(tweet,null,"dwdwwd");
        Assert.assertNotNull(res.getBody());
    }


    @Test
    public void likeTweet(){
        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").error("sdsdsdsd").build();

        Mockito.when(tweetService.likeTweet(Mockito.anyString(),Mockito.anyString())).thenReturn(userLoginResponse);
        ResponseEntity res=tweetController.likeTweet("kjhjl","dwdwwd");
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void getAllTweets(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").postDate(new Date()).build();
        Mockito.when(tweetService.getAllTweets(Mockito.anyString())).thenReturn(Collections.singletonList(tweet));
        ResponseEntity res=tweetController.getAllTweets("dwdwwd");
        Assert.assertNotNull(res.getBody());
    }

    @Test
    public void getAllUsersTweets(){
        Tweet tweet=Tweet.builder().id("dsdsdsds").postDate(new Date()).build();
        Mockito.when(tweetService.getAllUsersTweets(Mockito.anyString())).thenReturn(Collections.singletonList(tweet));
        ResponseEntity res=tweetController.getAllUsersTweets("dwdwwd");
        Assert.assertNotNull(res.getBody());
    }
}
