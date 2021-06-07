package com.tweetapp.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.User;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.UserService;
import com.tweetapp.service.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public String saveUser(User user) {
        String userValidation = UserValidator.validateUser(user);
        if (!userValidation.equalsIgnoreCase(ReasonConstant.VALID)) {
            return userValidation;
        }
        if (isValidEmail(user.getEmail())) {
            return ReasonConstant.EMAIL_ALREADY_REGISTERED;
        }
        if (isValidLoginId(user.getLoginId())) {
            return ReasonConstant.LOGIN_ID_ALREADY_EXISTS;
        }
        if (userValidation.equalsIgnoreCase(ReasonConstant.VALID)) {
            dynamoDBMapper.save(user);
            return ReasonConstant.USER_REGISTERED;
        } else {
            return userValidation;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return dynamoDBMapper.load(User.class,email)!=null;
    }

    private boolean isValidLoginId(String loginId) {
        if (loginId == null) {
            return false;
        }
        List<User> users=dynamoDBMapper.scan(User.class,new DynamoDBScanExpression());
        for(User user:users){
            if(user.getLoginId()==loginId){
                return true;
            }
        }
        return false;
    }

    @Override
    public UserLoginResponse loginUser(String username, String password) {
        List<User> users=dynamoDBMapper.scan(User.class,new DynamoDBScanExpression());
        User user=null;
        for(User userListIte:users){
            if(userListIte.getEmail().equalsIgnoreCase(username) && userListIte.getPassword().equals(password)){
                user=userListIte;
            }
        }

        if (user==null) {
            return UserLoginResponse.builder().error(ReasonConstant.INVALID_CREDENTIALS_PLEASE_TRY_AGAIN)
                    .build();
        }
        if (user.getEmail() != null || user.getFirstName() != null || user.getLastName() != null ||
                user.getLoginId() != null) {
            return UserLoginResponse.builder().username(user.getLoginId()).email(user.getEmail()).
                    firstName(user.getFirstName()).lastName(user.getLastName())
                    .build();
        }
        return UserLoginResponse.builder().error(ReasonConstant.INVALID_CREDENTIALS_PLEASE_TRY_AGAIN)
                .build();
    }

    @Override
    public String forgotPassword(String username, String newPassword, String confirmPassword) {
        User user=null;
        List<User> users=dynamoDBMapper.scan(User.class,new DynamoDBScanExpression());
        for(User userItr: users){
            if(userItr.getLoginId().equalsIgnoreCase(username)){
                user=userItr;
            }
        }
        if (user==null) {
            return ReasonConstant.NO_USER_FOUND;
        }
        if (newPassword.length() < 8 || confirmPassword.length() < 8) {
            return ReasonConstant.PASSWORD_LENGTH_SHOULD_BE_EIGHT_AND_CAN_T_BE_NULL;
        }
        if (!UserValidator.isValidPassword(newPassword, confirmPassword)) {
            return ReasonConstant.PASSWORD_AND_CONFIRM_NOT_SAME;
        }
        user.setPassword(newPassword);
        user.setConfirmPassword(confirmPassword);
        dynamoDBMapper.save(user);
        return ReasonConstant.PASSWORD_CHANGED_PLEASE_LOGIN_AGAIN;
    }

    @Override
    public List<String> getAllUsers() {
        List<User> users = dynamoDBMapper.scan(User.class,new DynamoDBScanExpression());
        if (users.isEmpty()) {
            return new ArrayList<>();
        } else {
            return users.stream().map(User::getLoginId).collect(Collectors.toList());
        }

    }

    @Override
    public UserLoginResponse findUser(String username) {
        User user=null;
        List<User> users=dynamoDBMapper.scan(User.class,new DynamoDBScanExpression());
        for(User userItr: users){
            if(userItr.getLoginId().equalsIgnoreCase(username)){
                user=userItr;
            }
        }
        if (user==null) {
            return UserLoginResponse.builder().error(ReasonConstant.NO_USER_FOUND).build();
        } else if (user.getEmail() != null || user.getFirstName() != null || user.getLastName() != null ||
                user.getLoginId() != null) {
            return UserLoginResponse.builder().firstName(user.getFirstName()).lastName(user.getFirstName()).
                    email(user.getEmail()).username(user.getLoginId()).build();
        }
        return UserLoginResponse.builder().error(ReasonConstant.NO_USER_FOUND).build();
    }


}
