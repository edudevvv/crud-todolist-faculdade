package fecaf.eduardo.gerenciador.features.users;

import fecaf.eduardo.gerenciador.features.users.respository.User;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String handleRegisterUser(@Valid @RequestBody User data) {
        var email = data.getEmail();
        var password = data.getPassword();

        var res = userService.handleRegister(email, password);
        return res;
    }

    @PostMapping("/login")
    public String handleLoginUser(@Valid @RequestBody User data) {
        var email = data.getEmail();
        var password = data.getPassword();

        var res = userService.handleLogin(email, password);
        return res;
    }
}
