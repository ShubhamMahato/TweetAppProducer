package com.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "TWEET_USER")
public class User {
    private String firstName;
    private String lastName;
    @Id
    private String email;
    private String loginId;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
}
