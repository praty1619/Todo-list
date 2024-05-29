package com.mytodolist.controller;

import com.mytodolist.dto.TaskDTO;
import com.mytodolist.model.Task;
import com.mytodolist.model.User;
import com.mytodolist.service.AuthService;
import com.mytodolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private AuthService authService;

    private User authenticate(String username, String password) {
        return authService.authenticate(username, password)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    @GetMapping
    public List<TaskDTO> getAllTasks(@RequestHeader("username") String username, @RequestHeader("password") String password) {
        User user = authenticate(username, password);
        return taskService.getAllTasks(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@RequestHeader("username") String username, @RequestHeader("password") String password, @PathVariable Long id) {
        User user = authenticate(username, password);
        Optional<Task> task = taskService.getTaskById(user, id);
        if (task.isPresent()) {
            return ResponseEntity.ok(convertToDTO(task.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public TaskDTO createTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @RequestBody TaskDTO taskDTO) {
        User user = authenticate(username, password);
        Task task = taskService.createTask(user, taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.isCompleted());
        return convertToDTO(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@RequestHeader("username") String username, @RequestHeader("password") String password,
                                              @PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        User user = authenticate(username, password);
        Optional<Task> taskOptional = taskService.updateTask(user, id, taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.isCompleted());
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            return ResponseEntity.ok(convertToDTO(task));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@RequestHeader("username") String username, @RequestHeader("password") String password, @PathVariable Long id) {
        User user = authenticate(username, password);
        taskService.deleteTask(user, id);
        return ResponseEntity.noContent().build();
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setCompleted(task.isCompleted());
        return taskDTO;
    }
}
