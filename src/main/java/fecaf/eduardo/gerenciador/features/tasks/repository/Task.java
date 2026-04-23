package fecaf.eduardo.gerenciador.features.tasks.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tasks")
public class Task {
    @Id
    private String id;

    @NotBlank(message = "Nome da task é obrigatório")
    @Size(min = 8, max = 100, message = "Nome deve ter entre 8 e 100 caracteres")
    private String title;

    @NotBlank(message = "Descrição da task é obrigatório")
    @Size(min = 10, max = 100, message = "Descrição deve ter entre 10 e 100 caracteres")
    private String description;

    public Task() {}

    public Task(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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