package com.taskmanager.features.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.configs.JwtAuthFilter;
import com.taskmanager.configs.SecurityConfig;
import com.taskmanager.features.tasks.interfaces.TaskRepository;
import com.taskmanager.features.tasks.repository.Task;
import com.taskmanager.features.users.interfaces.UserRepository;
import com.taskmanager.features.users.respository.User;
import com.taskmanager.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@TestPropertySource(properties = "jwt.secret=testSecretKey32charsLongForTesting!!")
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtils.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtils jwtUtils;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private UserRepository userRepository;

    private User mockUser;
    private String token;

    @BeforeEach
    void setUp() {
        mockUser = new User("test@email.com", "hashedPassword");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(mockUser));
        token = jwtUtils.generateToken("test@email.com");
    }

    // ────────────────────────────────────────────
    // GET /tasks/list
    // ────────────────────────────────────────────

    @Test
    @DisplayName("GET /tasks/list → 200 com lista de tasks do usuário autenticado")
    void listTasks_success() throws Exception {
        Task task1 = new Task("Tarefa 1", "Descrição 1", Task.Priority.HIGH, Task.Status.PENDING, mockUser);
        Task task2 = new Task("Tarefa 2", "Descrição 2", Task.Priority.LOW, Task.Status.DONE, mockUser);
        when(taskRepository.findAllByUser(mockUser)).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/tasks/list")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("GET /tasks/list → 200 com lista vazia quando usuário não tem tasks")
    void listTasks_empty() throws Exception {
        when(taskRepository.findAllByUser(mockUser)).thenReturn(List.of());

        mockMvc.perform(get("/tasks/list")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("GET /tasks/list → 401 sem autenticação JWT")
    void listTasks_unauthorized() throws Exception {
        mockMvc.perform(get("/tasks/list"))
                .andExpect(status().isUnauthorized());
    }

    // ────────────────────────────────────────────
    // POST /tasks/create
    // ────────────────────────────────────────────

    @Test
    @DisplayName("POST /tasks/create → 200 quando dados válidos")
    void createTask_success() throws Exception {
        Task body = new Task("Estudar Spring", "Revisar JPA e Security", Task.Priority.HIGH, Task.Status.PENDING, null);

        doNothing().when(taskService).save(anyString(), anyString(), any(), any(), any());

        mockMvc.perform(post("/tasks/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task criada com sucesso."));
    }

    @Test
    @DisplayName("POST /tasks/create → 400 quando título está em branco")
    void createTask_blankTitle() throws Exception {
        Task body = new Task("", "Descrição válida aqui", Task.Priority.MEDIUM, Task.Status.PENDING, null);

        mockMvc.perform(post("/tasks/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks/create → 400 quando descrição está em branco")
    void createTask_blankDescription() throws Exception {
        Task body = new Task("Título válido aqui", "", Task.Priority.MEDIUM, Task.Status.PENDING, null);

        mockMvc.perform(post("/tasks/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /tasks/create → 401 sem autenticação JWT")
    void createTask_unauthorized() throws Exception {
        Task body = new Task("Título", "Descrição da task", Task.Priority.LOW, Task.Status.PENDING, null);

        mockMvc.perform(post("/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    // ────────────────────────────────────────────
    // PUT /tasks/edit/{taskId}
    // ────────────────────────────────────────────

    @Test
    @DisplayName("PUT /tasks/edit/{taskId} → 200 quando task existe e pertence ao usuário")
    void editTask_success() throws Exception {
        String taskId = "task-uuid-123";
        Task existing = new Task("Título antigo", "Descrição antiga", Task.Priority.LOW, Task.Status.PENDING, mockUser);
        when(taskRepository.findByIdAndUser(taskId, mockUser)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any())).thenReturn(existing);

        Task update = new Task("Título novo", "Descrição nova", Task.Priority.HIGH, Task.Status.IN_PROGRESS, null);

        mockMvc.perform(put("/tasks/edit/{taskId}", taskId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task editada com sucesso."));
    }

    @Test
    @DisplayName("PUT /tasks/edit/{taskId} → 404 quando task não pertence ao usuário")
    void editTask_notFound() throws Exception {
        String taskId = "task-inexistente";
        when(taskRepository.findByIdAndUser(taskId, mockUser)).thenReturn(Optional.empty());

        Task update = new Task("Título novo", "Descrição nova", Task.Priority.MEDIUM, Task.Status.DONE, null);

        mockMvc.perform(put("/tasks/edit/{taskId}", taskId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /tasks/edit/{taskId} → 401 sem autenticação JWT")
    void editTask_unauthorized() throws Exception {
        Task update = new Task("Título", "Descrição", Task.Priority.LOW, Task.Status.PENDING, null);

        mockMvc.perform(put("/tasks/edit/{taskId}", "qualquer-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isUnauthorized());
    }

    // ────────────────────────────────────────────
    // DELETE /tasks/delete/{taskId}
    // ────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /tasks/delete/{taskId} → 200 quando task existe e pertence ao usuário")
    void deleteTask_success() throws Exception {
        String taskId = "task-uuid-456";
        Task existing = new Task("Título", "Descrição", Task.Priority.MEDIUM, Task.Status.PENDING, mockUser);
        when(taskRepository.findByIdAndUser(taskId, mockUser)).thenReturn(Optional.of(existing));
        doNothing().when(taskRepository).delete(existing);

        mockMvc.perform(delete("/tasks/delete/{taskId}", taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task deletada com sucesso!"));
    }

    @Test
    @DisplayName("DELETE /tasks/delete/{taskId} → 404 quando task não existe")
    void deleteTask_notFound() throws Exception {
        String taskId = "task-inexistente";
        when(taskRepository.findByIdAndUser(taskId, mockUser)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/tasks/delete/{taskId}", taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /tasks/delete/{taskId} → 401 sem autenticação JWT")
    void deleteTask_unauthorized() throws Exception {
        mockMvc.perform(delete("/tasks/delete/{taskId}", "qualquer-id"))
                .andExpect(status().isUnauthorized());
    }
}
