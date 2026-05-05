package com.taskmanager.features.tasks.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskmanager.features.users.respository.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @NotBlank(message = "Nome da task é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String title;

    @NotBlank(message = "Descrição da task é obrigatório")
    @Size(min = 3, max = 255, message = "Descrição deve ter entre 3 e 255 caracteres")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum Priority { LOW, MEDIUM, HIGH }
    public enum Status   { PENDING, IN_PROGRESS, DONE }

    public Task() {}

    public Task(String title, String description, Priority priority, Status status, User user) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public User getUser()              { return user; }
    public void setUser(User user)     { this.user = user; }

    public String getTitle()             { return title; }
    public void setTitle(String title)   { this.title = title; }

    public String getDescription()                   { return description; }
    public void setDescription(String description)   { this.description = description; }

    public Priority getPriority()                { return priority; }
    public void setPriority(Priority priority)   { this.priority = priority; }

    public Status getStatus()            { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }
}
