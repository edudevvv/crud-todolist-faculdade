package com.taskmanager.features.tasks;

import com.taskmanager.features.tasks.interfaces.TaskRepository;
import com.taskmanager.features.tasks.repository.Task;
import com.taskmanager.features.users.interfaces.UserRepository;
import com.taskmanager.features.users.respository.User;
import com.taskmanager.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService tasksService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> handleListTask() {
        User user = getAuthenticatedUser();
        var tasks = taskRepository.findAllByUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks listadas com sucesso.", tasks));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> handleCreateTask(@Valid @RequestBody Task task) {
        User user = getAuthenticatedUser();
        tasksService.save(
            task.getTitle(),
            task.getDescription(),
            task.getPriority() != null ? task.getPriority() : Task.Priority.MEDIUM,
            task.getStatus()   != null ? task.getStatus()   : Task.Status.PENDING,
            user
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Task criada com sucesso.", null));
    }

    @PutMapping("/edit/{taskId}")
    public ResponseEntity<ApiResponse<String>> handleEditTask(
            @PathVariable Long taskId,
            @Valid @RequestBody Task taskData) {

        User user = getAuthenticatedUser();
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task não encontrada."));

        task.setTitle(taskData.getTitle());
        task.setDescription(taskData.getDescription());
        if (taskData.getPriority() != null) task.setPriority(taskData.getPriority());
        if (taskData.getStatus()   != null) task.setStatus(taskData.getStatus());
        taskRepository.save(task);

        return ResponseEntity.ok(new ApiResponse<>(true, "Task editada com sucesso.", null));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<ApiResponse<String>> handleDeleteTask(@PathVariable Long taskId) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task não encontrada."));
        taskRepository.delete(task);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task deletada com sucesso!", null));
    }
}
