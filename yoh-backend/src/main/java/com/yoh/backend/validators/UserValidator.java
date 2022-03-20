package com.yoh.backend.validators;

import com.yoh.backend.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private final static Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );


    public void validate(User user) throws IllegalArgumentException{
        this.validateEmail(user.getEmail());
        this.validateLogin(user.getLogin());
        this.validatePassword(user.getPassword());
    }

    private void validateLogin(String login) throws IllegalArgumentException {
        if (0 == login.length() || login.length() > 128) {
            throw new IllegalArgumentException(
                    String.format("Login has invalid value (%s). Check length of login.", login)
            );
        }
    }

    private void validateEmail(String email) throws IllegalArgumentException {
        if (0 == email.length() || email.length() > 128) {
            throw new IllegalArgumentException(
                    String.format("Email has invalid value (%s). Check length of email.", email)
            );
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()){
            throw new IllegalArgumentException(
                    String.format("Email has invalid value (%s). Check format of email.", email)
            );
        }
    }

    private void validatePassword(String password) throws IllegalArgumentException{
        if (0 == password.length() || password.length() > 256){
            throw new IllegalArgumentException(
                    String.format("Password has invalid value (%s). Check length of password.", password)
            );
        }
    }
}
