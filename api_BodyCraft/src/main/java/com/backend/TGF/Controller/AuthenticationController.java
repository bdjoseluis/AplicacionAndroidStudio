package com.backend.TGF.Controller;




import com.backend.TGF.dto.Athentication.LoginUserDTO;
import com.backend.TGF.dto.Athentication.RegisterUserDTO;
import com.backend.TGF.dto.Athentication.UpdateUserDTO;
import com.backend.TGF.exceptions.Response;
import com.backend.TGF.mappers.UserMapper;
import com.backend.TGF.model.entity.User;
import com.backend.TGF.model.services.Authentication.IAuthenticationService;
import com.backend.TGF.model.services.User.IUserService;
import com.backend.TGF.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Response> register(@RequestBody RegisterUserDTO registerUserDto) {
        User user = userMapper.fromDTO(registerUserDto);

        authenticationService.signup(user);

        String message = "Usuario registrado correctamente";

        return new ResponseEntity<Response>(Response.noErrorResponse(message), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User loginUser = userMapper.fromDTO(loginUserDto);

        User authenticatedUser = authenticationService.authenticate(loginUser);

        String jwtToken = jwtTokenProvider.generateToken(authenticatedUser);

        //LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(jwtToken);
    }

    // Nuevo método para buscar un usuario por su email usando @PathVariable
    @GetMapping("/userByEmail/{email}")
    public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email) {
        // Utilizar el servicio de usuario para obtener el usuario por email
        Optional<User> user = userService.findByEmail(email);

        // Verificar si el usuario fue encontrado
        if (user.isPresent()) {
            // Retornar el usuario encontrado
            return ResponseEntity.ok(user);
        } else {
            // Si no se encuentra el usuario, retornar un error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/updateUser")
    public ResponseEntity<Response> updateUser(@RequestBody UpdateUserDTO updateUserDto) {
        // Validar que el ID del usuario a actualizar no sea nulo
        if (updateUserDto.getId() == null) {
            String message = "ID de usuario es requerido para la actualización";
            return ResponseEntity.badRequest().body(Response.validationError(Map.of("ID", message)));
        }

        // Buscar el usuario actual en la base de datos por su ID
        Optional<User> existingUserOptional = userService.findById(updateUserDto.getId());

        if (existingUserOptional.isEmpty()) {
            // Si el usuario no se encuentra, retornar un error 404
            String message = "Usuario no encontrado";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.notFoundError(Map.of("User", message)));
        }

        User existingUser = existingUserOptional.get();

        // Actualizar los campos del usuario existente con los datos de UpdateUserDTO
        userMapper.updateEntityFromDTO(updateUserDto, existingUser);

        try {
            // Guardar los cambios en el servicio de usuario
            User savedUser = userService.updateUser(existingUser);

            // Si la actualización fue exitosa, retornar un mensaje de éxito
            String message = "Usuario actualizado correctamente";
            return ResponseEntity.ok(Response.noErrorResponse(message));
        } catch (Exception e) {
            // Si ocurrió un error durante la actualización, retornar un mensaje de error general
            String message = "No se pudo actualizar el usuario debido a un error interno";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.generalError(Map.of("Error", message)));
        }
    }
    // Método para buscar un usuario por su ID usando @PathVariable
    @GetMapping("/userById/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}