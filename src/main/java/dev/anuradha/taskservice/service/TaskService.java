package dev.anuradha.taskservice.service;

import dev.anuradha.taskservice.model.Task;
import dev.anuradha.taskservice.dto.TaskRequestDTO;
import dev.anuradha.taskservice.exception.UnauthorizedException;
import dev.anuradha.taskservice.model.TaskStatus;
import dev.anuradha.taskservice.repository.TaskRepository;
import dev.anuradha.taskservice.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Service
@Setter
@Getter
public class TaskService {

    private TaskRepository taskRepository;
    private JwtUtil jwtUtil;

    public TaskService(TaskRepository taskRepository,
                       JwtUtil jwtUtil) {
        this.taskRepository = taskRepository;
        this.jwtUtil = jwtUtil;
    }
    @Transactional
    public Task createTask(String userEmail, TaskRequestDTO request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedBy(userEmail);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public List<Task> getTasksForUser(String userEmail) {
        return taskRepository.findByCreatedBy(userEmail);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Long id, String userEmail, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found"));

        if (!isAdmin && !task.getCreatedBy().equals(userEmail)) {
            throw new UnauthorizedException("You are not allowed to delete this task.");
        }
        taskRepository.delete(task);
    }

    public Task updateTask(Long id, String token, String title, String description, TaskStatus status) {
        String userEmail = jwtUtil.extractUsername(token);
        boolean isAdmin = jwtUtil.isAdmin(token);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!isAdmin && !task.getCreatedBy().equals(userEmail)) {
            throw new UnauthorizedException("You are not allowed to update this task.");
        }

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        return taskRepository.save(task);
    }

}
