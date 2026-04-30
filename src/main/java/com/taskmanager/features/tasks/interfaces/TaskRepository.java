package com.taskmanager.features.tasks.interfaces;

import com.taskmanager.features.tasks.repository.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> { }
