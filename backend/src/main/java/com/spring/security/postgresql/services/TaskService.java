package com.spring.security.postgresql.services;

import com.spring.security.postgresql.controllers.TaskController;
import com.spring.security.postgresql.exception.ResourceNotFoundException;
import com.spring.security.postgresql.models.ETaskStatus;
import com.spring.security.postgresql.models.Task;
import com.spring.security.postgresql.models.User;
import com.spring.security.postgresql.payload.dto.TaskDTO;
import com.spring.security.postgresql.repository.TaskRepository;
import com.spring.security.postgresql.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public Task createTask(Task task, User createdBy) {

        task.setUuid(generateUuid());
        task.setCreatedOn(LocalDate.now());
        task.setUpdatedOn(LocalDate.now());
        task.setStatus(ETaskStatus.PENDING);
        task.setCreatedBy(createdBy);
        task.setUpdatedBy(createdBy);

        if (task.getAssignedUser() != null) {
            User assignedUser = userRepository.findById(task.getAssignedUser().getId()).orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedUser(assignedUser);
        }
        return taskRepository.save(task);
    }

    private String generateUuid() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }

    public Task updateTask(Long id, Task task, User user) {
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isEmpty()) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }

        Task existingTask = existingTaskOptional.get();

        String username = user.getUsername();
        logger.info("User {} is updating the task", username);

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setAssignedUser(task.getAssignedUser());
        existingTask.setUpdatedOn(java.time.LocalDate.now());
        existingTask.setUpdatedBy(user);

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isEmpty()) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }
        Task taskToDelete = existingTaskOptional.get();
        taskRepository.delete(taskToDelete);
    }

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setUuid(task.getUuid());
            dto.setTitle(task.getTitle());
            dto.setAssignedUserEmail(task.getAssignedUser().getEmail());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus().toString());
            dto.setCreatedOn(task.getCreatedOn());
            dto.setUpdatedOn(task.getUpdatedOn());
            dto.setCreatedBy(task.getCreatedBy().getUsername());
            dto.setUpdatedBy(task.getUpdatedBy().getUsername());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<TaskDTO> getAllTasksByCurrentUser(Long id) {
        User currentUser = userService.fetchCurrentUser();
        if (currentUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!Objects.equals(currentUser.getId(), id)){
            throw new ResourceNotFoundException("User id mismatch");
        }
        List<Task> allTasks = taskRepository.findAll();
        List<Task> tasksAssignedToUser = allTasks.stream().filter(task -> task.getAssignedUser().equals(currentUser)).toList();
        return tasksAssignedToUser.stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setUuid(task.getUuid());
            dto.setTitle(task.getTitle());
            dto.setAssignedUserEmail(task.getAssignedUser().getEmail());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus().toString());
            dto.setCreatedOn(task.getCreatedOn());
            dto.setUpdatedOn(task.getUpdatedOn());
            dto.setCreatedBy(task.getCreatedBy().getUsername());
            dto.setUpdatedBy(task.getUpdatedBy().getUsername());
            return dto;
        }).collect(Collectors.toList());
    }
}
