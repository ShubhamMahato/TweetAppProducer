package com.tweetapp.service.util;

import com.tweetapp.model.ReasonConstant;
import com.tweetapp.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidator {

    public static String validateUser(User user) {
        if (!isValidString(user.getFirstName())) {
            return ReasonConstant.FIRST_NAME_NOT_VALID;
        } else if (!isValidString(user.getLastName())) {
            return ReasonConstant.LAST_NAME_NOT_VALID;
        } else if (!emailValidator(user.getEmail())) {
            return ReasonConstant.EMAIL_NOT_VALID;
        } else if (user.getPassword() == null || user.getPassword().length() < 8) {
            return ReasonConstant.PASSWORD_LENGTH_SHOULD_BE_EIGHT_AND_CAN_T_BE_NULL;
        } else if (!isValidPassword(user.getPassword(), user.getConfirmPassword())) {
            return ReasonConstant.PASSWORD_AND_CONFIRM_NOT_SAME;
        } else if (!isValidPhoneNumber(user.getPhoneNumber())) {
            return ReasonConstant.PHONE_NUMBER_NOT_VALID;
        } else {
            return ReasonConstant.VALID;
        }

    }

    /**
     * Validate the email
     *
     * @param email
     * @return
     */
    private static boolean emailValidator(String email) {
        if (email == null) {
            return false;
        }
        // Get an EmailValidator
        EmailValidator validator = EmailValidator.getInstance();

        // Validate specified String containing an email address
        return validator.isValid(email);
    }

    private static boolean isStringOnlyAlphabet(String str) {
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[a-zA-Z]*$")));
    }

    /**
     * is VALID firstname and lastname
     *
     * @param name
     * @return
     */
    private static boolean isValidString(String name) {
        if (name == null) {
            return false;
        }
        return isStringOnlyAlphabet(name);
    }


    public static boolean isValidPassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }


    private static boolean isValidPhoneNumber(String s) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private UserValidator() {

    }
}
