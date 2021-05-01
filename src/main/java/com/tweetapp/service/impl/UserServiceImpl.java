package com.tweetapp.service.impl;

import com.tweetapp.dao.UserDao;
import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.User;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.UserService;
import com.tweetapp.service.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

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
            userDao.save(user);
            return ReasonConstant.USER_REGISTERED;
        } else {
            return userValidation;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return userDao.findByEmail(email).isPresent();
    }

    private boolean isValidLoginId(String loginId) {
        if (loginId == null) {
            return false;
        }
        return userDao.findByLoginId(loginId).isPresent();
    }

    @Override
    public UserLoginResponse loginUser(String username, String password) {
        Optional<User> user = userDao.findByEmailAndPassword(username, password);

        if (!user.isPresent()) {
            return UserLoginResponse.builder().error(ReasonConstant.INVALID_CREDENTIALS_PLEASE_TRY_AGAIN)
                    .build();
        }
        if (user.get().getEmail() != null || user.get().getFirstName() != null || user.get().getLastName() != null ||
                user.get().getLoginId() != null) {
            return UserLoginResponse.builder().username(user.get().getLoginId()).email(user.get().getEmail()).
                    firstName(user.get().getFirstName()).lastName(user.get().getLastName())
                    .build();
        }
        return UserLoginResponse.builder().error(ReasonConstant.INVALID_CREDENTIALS_PLEASE_TRY_AGAIN)
                .build();
    }

    @Override
    public String forgotPassword(String username, String newPassword, String confirmPassword) {
        Optional<User> user = userDao.findByLoginId(username);
        if (!user.isPresent()) {
            return ReasonConstant.NO_USER_FOUND;
        }
        if (newPassword.length() < 8 || confirmPassword.length() < 8) {
            return ReasonConstant.PASSWORD_LENGTH_SHOULD_BE_EIGHT_AND_CAN_T_BE_NULL;
        }
        if (!UserValidator.isValidPassword(newPassword, confirmPassword)) {
            return ReasonConstant.PASSWORD_AND_CONFIRM_NOT_SAME;
        }
        user.get().setPassword(newPassword);
        user.get().setConfirmPassword(confirmPassword);
        userDao.save(user.get());
        return ReasonConstant.PASSWORD_CHANGED_PLEASE_LOGIN_AGAIN;
    }

    @Override
    public List<String> getAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            return new ArrayList<>();
        } else {
            return users.stream().map(User::getLoginId).collect(Collectors.toList());
        }

    }

    @Override
    public UserLoginResponse findUser(String username) {
        Optional<User> user = userDao.findByLoginId(username);
        if (!user.isPresent()) {
            return UserLoginResponse.builder().error(ReasonConstant.NO_USER_FOUND).build();
        } else if (user.get().getEmail() != null || user.get().getFirstName() != null || user.get().getLastName() != null ||
                user.get().getLoginId() != null) {
            return UserLoginResponse.builder().firstName(user.get().getFirstName()).lastName(user.get().getFirstName()).
                    email(user.get().getEmail()).username(user.get().getLoginId()).build();
        }
        return UserLoginResponse.builder().error(ReasonConstant.NO_USER_FOUND).build();
    }


}
