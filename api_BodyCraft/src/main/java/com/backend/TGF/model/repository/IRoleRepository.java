package com.backend.TGF.model.repository;



import com.backend.TGF.model.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends CrudRepository<Role, Long>
{
    Role findByName(String name);
}
