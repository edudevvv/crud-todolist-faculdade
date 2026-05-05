package com.taskmanager.features.tasks;

import com.taskmanager.features.tasks.interfaces.TaskRepository;
import com.taskmanager.features.tasks.repository.Task;
import com.taskmanager.features.users.interfaces.UserRepository;
import com.taskmanager.features.users.respository.User;
import com.taskmanager.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas do usuário autenticado")
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas do usuário autenticado.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<?>> handleListTask() {
        User user = getAuthenticatedUser();
        var tasks = taskRepository.findAllByUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks listadas com sucesso.", tasks));
    }

    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa para o usuário autenticado.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task criada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = """
                {
                  "title": "Estudar Spring Boot",
                  "description": "Revisar conceitos de segurança e JPA",
                  "priority": "HIGH",
                  "status": "PENDING"
                }
                """)
        )
    )
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

    @Operation(summary = "Editar tarefa", description = "Atualiza os dados de uma tarefa existente do usuário autenticado.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task editada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task não encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    @PutMapping("/edit/{taskId}")
    public ResponseEntity<ApiResponse<String>> handleEditTask(
            @Parameter(description = "ID UUID da tarefa") @PathVariable String taskId,
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

    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa do usuário autenticado pelo ID.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task deletada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task não encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido")
    })
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<ApiResponse<String>> handleDeleteTask(
            @Parameter(description = "ID UUID da tarefa") @PathVariable String taskId) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task não encontrada."));
        taskRepository.delete(task);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task deletada com sucesso!", null));
    }
}
