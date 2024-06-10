package com.backend.TGF.mappers;

import com.backend.TGF.dto.Athentication.LoginUserDTO;
import com.backend.TGF.dto.Athentication.RegisterUserDTO;
import com.backend.TGF.dto.Athentication.UpdateUserDTO;
import com.backend.TGF.dto.Athentication.UserDTO;
import com.backend.TGF.dto.UserDTORequest;
import com.backend.TGF.model.entity.Role;
import com.backend.TGF.model.entity.User;
import com.backend.TGF.model.repository.IRoleRepository;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class UserMapper
{
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    IRoleRepository roleRepository;

    public User fromDTO(LoginUserDTO userDTO)
    {
        return modelMapper.map(userDTO, User.class);
    }

    public User fromDTO(UserDTORequest userDTO)
    {
        return modelMapper.map(userDTO, User.class);
    }

    public User fromDTO(RegisterUserDTO registerUserDTO)
    {
        //Mapea los nombres de la lista Actores a una lista de String.
        modelMapper.typeMap(RegisterUserDTO.class, User.class).addMappings(mapper -> {
            mapper.using(new RolesListConverter(roleRepository)).map(RegisterUserDTO::getRolIds, User::setRoles);
        });

        return modelMapper.map(registerUserDTO, User.class);
    }

    // Nuevo método para mapear UpdateUserDTO a User
    public void updateEntityFromDTO(UpdateUserDTO updateUserDTO, User existingUser) {
        // Actualizar los campos del usuario existente con los valores de UpdateUserDTO
        modelMapper.map(updateUserDTO, existingUser);
    }

    public UserDTO toDTO(User user) {
        // Mapea un User a un UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setGenero(user.getGenero());
        userDTO.setComidas(user.getComidas());
        userDTO.setDiasquevoy(user.getDiasquevoy());
        userDTO.setAge(user.getAge());
        userDTO.setImage(user.getImage());
        userDTO.setCreationDate(user.getCreationDate());
        userDTO.setLastLogin(user.getLastLogin());
        userDTO.setActive(user.isActive());

        // Mapea los roles a sus nombres y establece en userDTO
        userDTO.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        return userDTO;
    }
}

