package fecaf.eduardo.gerenciador.features.users.respository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;


public class User {
    @Id
    private String id;

    @NotBlank(message = "Email é obrigatório.")
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    private String password;

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
