package com.tweetapp.controller;

import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.User;
import com.tweetapp.model.request.ForgotPassword;
import com.tweetapp.model.request.UserLoginRequest;
import com.tweetapp.model.response.UserLoginResponse;
import com.tweetapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        log.info("START :: registerUser:: user: {}",user);
        String isRegistered = userService.saveUser(user);
        if (isRegistered.equalsIgnoreCase(ReasonConstant.USER_REGISTERED)) {
            log.info("START :: registerUser:: success: {}",user);
            return new ResponseEntity<>(ReasonConstant.USER_REGISTERED,HttpStatus.OK);
        } else {
            log.debug("START :: registerUser:: error: {}",user);
            return new ResponseEntity<>(isRegistered,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody UserLoginRequest user) {
        log.info("START :: loginUser:: UserLoginRequest: {}",user);
        if (null == user.getEmail() || null == user.getPassword()) {
            log.debug("NULL DATA :: loginUser:: UserLoginRequest: {}",user);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        UserLoginResponse userLogin = userService.loginUser(user.getEmail(), user.getPassword());
        if (userLogin.getError() != null) {
            log.debug("NOT FOUND:: loginUser:: UserLoginRequest: {}",user);
            return ResponseEntity.badRequest().body(userLogin.getError());
        }
        log.debug("END:: loginUser:: UserLoginRequest: {}",user);
        return ResponseEntity.ok(userLogin);
    }

    @PostMapping("/{username}/forgot")
    public ResponseEntity forgotPassword(@RequestBody ForgotPassword forgotPassword, @PathVariable String username) {
        log.info("START :: forgotPassword :: {} username :: {}",forgotPassword,username);
        if (null == forgotPassword.getNewPassword() || null == forgotPassword.getConfirmPassword()) {
            log.debug("START :: forgotPassword :: {} username :: {}",forgotPassword,username);
            return ResponseEntity.badRequest().body(ReasonConstant.WRONG_INPUT);
        }
        String changedPassword = userService.forgotPassword(username, forgotPassword.getNewPassword(), forgotPassword.getConfirmPassword());
        if (changedPassword.equalsIgnoreCase(ReasonConstant.PASSWORD_CHANGED_PLEASE_LOGIN_AGAIN)) {
            log.info("END :: forgotPassword :: {} username :: {}",forgotPassword,username);
            return ResponseEntity.ok().body(ReasonConstant.PASSWORD_CHANGED_PLEASE_LOGIN_AGAIN);
        }
        log.info("END :: forgotPassword :: {} username :: {} :: error :: {}",forgotPassword,username, changedPassword);
        return ResponseEntity.badRequest().body(changedPassword);
    }

    @GetMapping("/users/all")
    public ResponseEntity getAllUsers() {
        log.info("START :: loginUser:: getAllUsers: ");
        List<String> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.debug("START :: loginUser:: getAllUsers: ");
            return ResponseEntity.badRequest().body(ReasonConstant.NO_USER_FOUND);
        }
        log.info("END :: loginUser:: getAllUsers: ");
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/user/search/{username}")
    public ResponseEntity findUser(@PathVariable String username) {
        log.info("START :: findUser :: username : {}",username);
        UserLoginResponse user = userService.findUser(username);
        if (user.getError()!=null) {
            log.debug("NED :: findUser :: username : {} :: error :: {} ",username,user.getError());
            return ResponseEntity.badRequest().body(user.getError());
        }
        log.info("END :: findUser :: username : {}",username);
        return ResponseEntity.ok().body(user);
    }


}
