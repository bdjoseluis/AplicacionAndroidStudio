package com.backend.TGF.model.repository;



import com.backend.TGF.model.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface IUserRepository extends CrudRepository<User, Long>
{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
