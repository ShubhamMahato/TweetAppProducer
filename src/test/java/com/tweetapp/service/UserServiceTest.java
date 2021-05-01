package com.tweetapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.dao.TweetDao;
import com.tweetapp.dao.UserDao;
import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetEventType;
import com.tweetapp.model.User;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.impl.TweetServiceImpl;
import com.tweetapp.service.impl.UserServiceImpl;
import com.tweetapp.service.util.UserValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserDao userDao;

    @Mock
    UserValidator userValidator;


    @Test
    public void saveUser(){
        User user=User.builder().email("sdsdsds").firstName("fssdsd").lastName("ssdsdsd").confirmPassword("sdsdsds").loginId("sdsdsds").build();
        Mockito.when(userDao.save(Mockito.any())).thenReturn(ReasonConstant.USER_REGISTERED);
        Assert.assertNotNull(userService.saveUser(user));
    }

    @Test
    public void loginUser(){
        Optional<User> user= Optional.ofNullable(User.builder().email("sdsdsds").firstName("fssdsd").lastName("ssdsdsd").confirmPassword("sdsdsds").loginId("sdsdsds").build());

        Mockito.when(userDao.findByEmailAndPassword(Mockito.anyString(),Mockito.anyString())).thenReturn(user);
        Assert.assertNotNull(userService.loginUser("sasas","casdadas"));
    }

}
