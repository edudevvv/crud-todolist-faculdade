package fecaf.eduardo.gerenciador.features.users.interfaces;

import fecaf.eduardo.gerenciador.features.users.respository.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
