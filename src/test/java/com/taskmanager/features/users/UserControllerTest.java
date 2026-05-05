package com.taskmanager.features.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.configs.JwtAuthFilter;
import com.taskmanager.configs.SecurityConfig;

import com.taskmanager.features.users.respository.User;
import com.taskmanager.handlers.UnauthorizedExceptionHandler;
import com.taskmanager.utils.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@TestPropertySource(properties = "jwt.secret=testSecretKey32charsLongForTesting!!")
@Import({SecurityConfig.class, JwtAuthFilter.class, JwtUtils.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    // ────────────────────────────────────────────
    // POST /auth/register
    // ────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/register → 200 quando dados válidos")
    void register_success() throws Exception {
        when(userService.handleRegister(anyString(), anyString()))
                .thenReturn("Usuario registrado com sucesso!");

        User body = new User("novo@email.com", "senha123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado com sucesso!"));
    }

    @Test
    @DisplayName("POST /auth/register → 401 quando usuário já existe")
    void register_userAlreadyExists() throws Exception {
        when(userService.handleRegister(anyString(), anyString()))
                .thenThrow(new UnauthorizedExceptionHandler("Usuario já existe."));

        User body = new User("existente@email.com", "senha123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/register → 400 quando email está em branco")
    void register_blankEmail() throws Exception {
        User body = new User("", "senha123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /auth/register → 400 quando senha está em branco")
    void register_blankPassword() throws Exception {
        User body = new User("teste@email.com", "");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    // ────────────────────────────────────────────
    // POST /auth/login
    // ────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/login → 200 e retorna token JWT quando credenciais válidas")
    void login_success() throws Exception {
        when(userService.handleLogin(anyString(), anyString()))
                .thenReturn("eyJhbGciOiJIUzI1NiJ9.token");

        User body = new User("usuario@email.com", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("eyJhbGciOiJIUzI1NiJ9.token"));
    }

    @Test
    @DisplayName("POST /auth/login → 401 quando credenciais inválidas")
    void login_invalidCredentials() throws Exception {
        when(userService.handleLogin(anyString(), anyString()))
                .thenThrow(new UnauthorizedExceptionHandler("Credenciais inválidas"));

        User body = new User("usuario@email.com", "senhaErrada");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/login → 401 quando usuário não existe")
    void login_userNotFound() throws Exception {
        when(userService.handleLogin(anyString(), anyString()))
                .thenThrow(new UnauthorizedExceptionHandler("Usuário não existe!"));

        User body = new User("inexistente@email.com", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }
}
