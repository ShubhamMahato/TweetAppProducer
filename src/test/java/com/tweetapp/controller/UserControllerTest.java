//package com.tweetapp.controller;
//
//import com.tweetapp.model.ReasonConstant;
//import com.tweetapp.model.User;
//import com.tweetapp.model.request.ForgotPassword;
//import com.tweetapp.model.request.UserLoginRequest;
//import com.tweetapp.model.response.UserLoginResponse;
//import com.tweetapp.service.UserService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//public class UserControllerTest {
//
//    @InjectMocks
//    UserController userController;
//
//    @Mock
//    UserService userService;
//
//    @Test
//    public void registerUser(){
//        User user=User.builder().email("sdsdsds").firstName("fssdsd").lastName("ssdsdsd").confirmPassword("sdsdsds").loginId("sdsdsds").build();
//        Mockito.when(userService.saveUser(Mockito.any())).thenReturn(ReasonConstant.USER_REGISTERED);
//        Assert.assertNotNull(userController.registerUser(user));
//    }
//    @Test
//    public void registerUser_error(){
//        User user=User.builder().email("sdsdsds").firstName("fssdsd").lastName("ssdsdsd").confirmPassword("sdsdsds").loginId("sdsdsds").build();
//        Mockito.when(userService.saveUser(Mockito.any())).thenReturn(ReasonConstant.WRONG_INPUT);
//        Assert.assertNotNull(userController.registerUser(user));
//    }
//
//    @Test
//    public void loginUser(){
//        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").build();
//        UserLoginRequest userLoginRequest=UserLoginRequest.builder().password("dsdsdsds").email("dsdsdsdsd").build();
//        Mockito.when(userService.loginUser(Mockito.anyString(),Mockito.any())).thenReturn(userLoginResponse);
//        Assert.assertNotNull(userController.loginUser(userLoginRequest));
//    }
//
//    @Test
//    public void forgotPassword(){
//        ForgotPassword forgotPassword=ForgotPassword.builder().confirmPassword("sdsdsdsd").newPassword("dssds").build();
//        Mockito.when(userService.forgotPassword(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(ReasonConstant.PASSWORD_CHANGED_PLEASE_LOGIN_AGAIN);
//        Assert.assertNotNull(userController.forgotPassword(forgotPassword,"sdsd"));
//    }
//
//    @Test
//    public void findUser(){
//
//        UserLoginResponse userLoginResponse= UserLoginResponse.builder().username("adss").email("dadadasdas").lastName("dsdsd").firstName("dadad").build();
//
//        Mockito.when(userService.findUser(Mockito.anyString())).thenReturn(userLoginResponse);
//        Assert.assertNotNull(userController.findUser("sdsd"));
//    }
//}
