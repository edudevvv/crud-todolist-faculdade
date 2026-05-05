package com.taskmanager.features.users;

import com.taskmanager.features.users.respository.User;
import com.taskmanager.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de registro e login de usuários")
@SecurityRequirement(name = "")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
        summary = "Registrar usuário",
        description = "Cria uma nova conta de usuário com email e senha."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Usuário já existe")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"email\": \"usuario@email.com\", \"password\": \"senha123\"}")
        )
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> handleRegisterUser(@Valid @RequestBody User data) {
        var email = data.getEmail();
        var password = data.getPassword();

        var registerResponse = userService.handleRegister(email, password);

        ApiResponse<String> response = new ApiResponse<>(true, registerResponse, null);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Login",
        description = "Autentica o usuário e retorna um token JWT válido por 1 hora."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso — token JWT retornado em 'data'",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"email\": \"usuario@email.com\", \"password\": \"senha123\"}")
        )
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> handleLoginUser(@Valid @RequestBody User data) {
        var email = data.getEmail();
        var password = data.getPassword();

        System.out.println(email + password);
        var token = userService.handleLogin(email, password);

        ApiResponse<String> response = new ApiResponse<>(true, "Login realizado com sucesso", token);
        return ResponseEntity.ok(response);
    }
}
