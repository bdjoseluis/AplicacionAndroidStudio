package com.backend.TGF.model.services.Authentication;


import com.backend.TGF.model.entity.User;

public interface IAuthenticationService
{
    public User signup(User newUser);

    public User authenticate(User user);
}
