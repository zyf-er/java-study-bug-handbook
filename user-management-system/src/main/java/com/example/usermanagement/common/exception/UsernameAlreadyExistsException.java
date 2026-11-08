package com.example.usermanagement.common.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("用户名已存在: " + username);
    }
}
