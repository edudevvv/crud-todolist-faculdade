package fecaf.eduardo.gerenciador.features.tasks;

import fecaf.eduardo.gerenciador.features.tasks.interfaces.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fecaf.eduardo.gerenciador.features.tasks.repository.Task;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void save(String name, String description) {
       Task task = new Task(name, description);

       var tasks = this.taskRepository.save(task);
       System.out.println(tasks);

    }
}
