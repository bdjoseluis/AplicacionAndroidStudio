package com.backend.TGF.model.services.User;

import com.backend.TGF.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService
{
    List<User> findAll();
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User addUser(User user, List<Long> rolIds);
    void remove(User user);
    User updateUser(User user);
}
