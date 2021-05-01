package com.tweetapp.service;

import com.tweetapp.model.User;
import com.tweetapp.model.response.UserLoginResponse;

import java.util.List;

public interface UserService {
    String saveUser(User user);
    UserLoginResponse loginUser(String username, String password);
    String forgotPassword(String username,String newPassword, String confirmPassword);
    List<String> getAllUsers();
    UserLoginResponse findUser(String username);
}
