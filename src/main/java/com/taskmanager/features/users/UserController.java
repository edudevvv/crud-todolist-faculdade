package com.taskmanager.features.users;

import com.taskmanager.features.users.respository.User;
import com.taskmanager.utils.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> handleRegisterUser(@Valid @RequestBody User data) {
        var email = data.getEmail();
        var password = data.getPassword();

        var registerResponse = userService.handleRegister(email, password);

        ApiResponse<String> response = new ApiResponse<>(true, registerResponse, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> handleLoginUser(@Valid @RequestBody User data) {
        var email = data.getEmail();
        var password = data.getPassword();

        var token = userService.handleLogin(email, password);

        ApiResponse<String> response = new ApiResponse<>(true, "Login realizado com sucesso", token);
        return ResponseEntity.ok(response);
    }
}
