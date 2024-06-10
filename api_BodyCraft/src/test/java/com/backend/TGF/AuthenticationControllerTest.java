package com.backend.TGF;

import com.backend.TGF.Controller.AuthenticationController;
import com.backend.TGF.dto.Athentication.LoginUserDTO;
import com.backend.TGF.dto.Athentication.RegisterUserDTO;
import com.backend.TGF.dto.Athentication.UpdateUserDTO;
import com.backend.TGF.exceptions.Response;
import com.backend.TGF.mappers.UserMapper;
import com.backend.TGF.model.entity.User;
import com.backend.TGF.model.services.Authentication.IAuthenticationService;
import com.backend.TGF.model.services.User.IUserService;
import com.backend.TGF.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private IAuthenticationService authenticationService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private IUserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        // Configura los datos de entrada
        RegisterUserDTO registerUserDto = new RegisterUserDTO();
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setName("Test User");
        registerUserDto.setPassword("password");
        // Configura otros campos según sea necesario

        // Crea un usuario esperado
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        expectedUser.setName("Test User");
        // Configura otros campos según sea necesario

        // Mapea el DTO a una entidad
        when(userMapper.fromDTO(registerUserDto)).thenReturn(expectedUser);

        // Realiza la operación de registro
        ResponseEntity<Response> response = authenticationController.register(registerUserDto);

        // Verifica los resultados
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Usuario registrado correctamente");

        // Verifica las interacciones con los servicios
        verify(authenticationService, times(1)).signup(expectedUser);
        verify(userMapper, times(1)).fromDTO(registerUserDto);
    }

    @Test
    public void testUpdateUser() {
        // Configura los datos de entrada
        UpdateUserDTO updateUserDto = new UpdateUserDTO();
        updateUserDto.setId(1L);
        updateUserDto.setName("Updated User");
        updateUserDto.setPeso(70.5);
        updateUserDto.setDiasquevoy(3);
        updateUserDto.setComidas(10);
        // Configura otros campos según sea necesario

        // Crea un usuario existente
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old User");
        // Configura otros campos según sea necesario

        // Crea un usuario actualizado esperado
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setPeso(70.5);
        updatedUser.setDiasquevoy(3);
        updatedUser.setComidas(10);
        // Configura otros campos según sea necesario

        // Configura los mocks
        when(userService.findById(updateUserDto.getId())).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(existingUser)).thenReturn(updatedUser);

        // Realiza la operación de actualización
        ResponseEntity<Response> response = authenticationController.updateUser(updateUserDto);

        // Verifica los resultados
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Usuario actualizado correctamente");

        // Verifica las interacciones con los servicios
        verify(userService, times(1)).findById(updateUserDto.getId());
        verify(userService, times(1)).updateUser(existingUser);
        verify(userMapper, times(1)).updateEntityFromDTO(updateUserDto, existingUser);
    }
}
