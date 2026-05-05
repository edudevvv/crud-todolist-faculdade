package com.taskmanager.features.tasks.interfaces;

import com.taskmanager.features.tasks.repository.Task;
import com.taskmanager.features.users.respository.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findAllByUser(User user);
    Optional<Task> findByIdAndUser(String id, User user);
}
