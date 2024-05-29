package com.mytodolist.service;

import com.mytodolist.model.Task;
import com.mytodolist.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final AtomicLong taskIdSequence = new AtomicLong(1);

    public List<Task> getAllTasks(User user) {
        return user.getTasks();
    }

    public Optional<Task> getTaskById(User user, Long taskId) {
        return user.getTasks().stream().filter(task -> task.getId().equals(taskId)).findFirst();
    }

    public Task createTask(User user, String title, String description, boolean completed) {
        Task task = new Task();
        task.setId(taskIdSequence.getAndIncrement());
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        user.getTasks().add(task);
        return task;
    }

    public Optional<Task> updateTask(User user, Long taskId, String title, String description, boolean completed) {
        Optional<Task> optionalTask = getTaskById(user, taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(title);
            task.setDescription(description);
            task.setCompleted(completed);
        }
        return optionalTask;
    }

    public void deleteTask(User user, Long taskId) {
        user.getTasks().removeIf(task -> task.getId().equals(taskId));
    }
}
