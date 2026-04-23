package fecaf.eduardo.gerenciador.features.tasks.interfaces;

import fecaf.eduardo.gerenciador.features.tasks.repository.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> { }