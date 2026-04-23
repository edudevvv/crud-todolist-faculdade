package fecaf.eduardo.gerenciador.features.users;

import fecaf.eduardo.gerenciador.features.users.interfaces.UserRepository;
import fecaf.eduardo.gerenciador.features.users.respository.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


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

        return "Usuario já existe.";
    }

    public String handleLogin(String email, String password) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não existe!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Credenciais invalidas";
        } else {
            return "Usuario logado.";
        }
    }
}
