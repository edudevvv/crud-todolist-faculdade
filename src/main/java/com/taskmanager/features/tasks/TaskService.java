package com.taskmanager.features.tasks;

import com.taskmanager.features.tasks.interfaces.TaskRepository;
import com.taskmanager.features.tasks.repository.Task;
import com.taskmanager.features.users.respository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void save(String title, String description, Task.Priority priority, Task.Status status, User user) {
        Task task = new Task(title, description, priority, status, user);
        taskRepository.save(task);
    }
}
