package com.backend.TGF.exceptions.User;

public class UserRegistrationException extends RuntimeException
{
    public UserRegistrationException() {
        super();
    }

    public UserRegistrationException(String message) {
        super(message);
    }
}
