package com.microcompany.accountsservice.exception;

public class UserNotFoundException extends GlobalException{

    protected static final long serialVersionUID = 3L;

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(Long userId) {
        super("User with id: " + userId + " not exist");
    }
}
