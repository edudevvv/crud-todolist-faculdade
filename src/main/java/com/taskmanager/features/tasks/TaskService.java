package com.taskmanager.features.tasks;

import com.taskmanager.features.tasks.interfaces.TaskRepository;
import com.taskmanager.features.tasks.repository.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
