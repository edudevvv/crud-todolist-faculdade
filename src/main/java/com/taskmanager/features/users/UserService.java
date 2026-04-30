package com.taskmanager.features.users;

import com.taskmanager.features.users.interfaces.UserRepository;
import com.taskmanager.features.users.respository.User;
import com.taskmanager.handlers.UnauthorizedExceptionHandler;
import com.taskmanager.utils.JwtUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String handleRegister(String email, String password) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            User userRegister = new User(email, password);

            userRegister.setPassword(passwordEncoder.encode(password));
            userRepository.save(userRegister);

            return "Usuario registrado com sucesso!";
        }

        throw new UnauthorizedExceptionHandler("Usuario já existe.");
    }

    public String handleLogin(String email, String password) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedExceptionHandler("Usuário não existe!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedExceptionHandler("Credenciais inválidas");
        }

        return jwtUtils.generateToken(email);
    }
}
