package com.taskmanager.features.tasks.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tbl_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da task é obrigatório")
    @Size(min = 8, max = 100, message = "Nome deve ter entre 8 e 100 caracteres")
    private String title;

    @NotBlank(message = "Descrição da task é obrigatório")
    @Size(min = 10, max = 100, message = "Descrição deve ter entre 10 e 100 caracteres")
    private String description;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
