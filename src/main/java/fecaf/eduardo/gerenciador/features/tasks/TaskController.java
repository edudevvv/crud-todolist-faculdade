package fecaf.eduardo.gerenciador.features.tasks;

import fecaf.eduardo.gerenciador.features.tasks.interfaces.TaskRepository;
import fecaf.eduardo.gerenciador.features.tasks.repository.Task;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService tasksService;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/list")
    public List<Task> handleListTask() {
        return taskRepository.findAll();
    }

    @PostMapping("/create")
    public void handleCreateTask(@Valid @RequestBody() Task task) {
        tasksService.save(task.getTitle(), task.getDescription());
    }

    @PutMapping("/edit")
    public void handleEditTask() {}

    @DeleteMapping("/delete/:taskId")
    public void handleDeleteTask() {}

}
