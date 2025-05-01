package dev.anuradha.taskservice.controller;

import dev.anuradha.taskservice.model.Task;
import dev.anuradha.taskservice.dto.TaskRequestDTO;
import dev.anuradha.taskservice.service.TaskService;
import dev.anuradha.taskservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;
    private JwtUtil jwtUtil;

    public TaskController(TaskService taskService, JwtUtil jwtUtil) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
    }

//    @PostMapping
//    public Task createTask(@RequestBody TaskRequestDTO request, HttpServletRequest httpRequest) {
//        String email = jwtUtil.extractUsernameFromHeader(httpRequest);
//        return taskService.createTask(email, request);
//    }
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskRequestDTO request, HttpServletRequest servletRequest) {
        String username = jwtUtil.extractUsernameFromHeader(servletRequest);
        return ResponseEntity.ok("Task created for user: " + username);
    }

    @GetMapping
    public List<Task> getUserTasks(HttpServletRequest request) {
        String email = jwtUtil.extractUsernameFromHeader(request);
        return taskService.getTasksForUser(email);
    }

    @GetMapping("/all")
    public List<Task> getAllTasks(HttpServletRequest request) {
        String email = jwtUtil.extractUsernameFromHeader(request);
        boolean isAdmin = jwtUtil.extractRoleFromHeader(request).equals("ADMIN");
        if (!isAdmin) throw new RuntimeException("Access Denied");
        return taskService.getAllTasks();
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, HttpServletRequest request) {
        String email = jwtUtil.extractUsernameFromHeader(request);
        boolean isAdmin = jwtUtil.extractRoleFromHeader(request).equals("ADMIN");
        taskService.deleteTask(id, email, isAdmin);
    }
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestHeader("Authorization") String token,
                           @RequestBody Task task) {
        return taskService.updateTask(id, token, task.getTitle(), task.getDescription(), task.getStatus());
    }
}
