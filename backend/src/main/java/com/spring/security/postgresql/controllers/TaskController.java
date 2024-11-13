package com.spring.security.postgresql.controllers;

import com.spring.security.postgresql.exception.ResourceNotFoundException;
import com.spring.security.postgresql.models.Task;
import com.spring.security.postgresql.models.User;
import com.spring.security.postgresql.payload.dto.TaskDTO;
import com.spring.security.postgresql.repository.TaskRepository;
import com.spring.security.postgresql.repository.UserRepository;
import com.spring.security.postgresql.security.services.UserDetailsImpl;
import com.spring.security.postgresql.services.TaskService;
import com.spring.security.postgresql.utils.ConstUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);


    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/createTask")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task, Authentication authentication) {
        logger.info("Attempting to create a new task with title: {}", task.getTitle());

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            logger.debug("Current user ID: {}, username: {}", userDetails.getId(), userDetails.getUsername());
            Optional<User> creatorOptional = userRepository.findById(userDetails.getId());
            if (creatorOptional.isPresent()) {
                User creator = creatorOptional.get();
                logger.info("User {} (ID: {}) found, creating task", creator.getUsername(), creator.getId());
                Task createdTask = taskService.createTask(task, creator);
                logger.info("Task successfully created with ID: {}", createdTask.getId());

                return new ResponseEntity<>(ConstUtils.TASK_CREATED_SUCCESSFULLY, HttpStatus.OK);
            } else {
                logger.error("User with ID {} not found", userDetails.getId());
                return ResponseEntity.status(404).body("User not found");
            }

        } catch (Exception e) {
            logger.error("Error occurred while creating task", e);
            return ResponseEntity.status(500).body("An error occurred while creating the task");
        }
    }

    @PostMapping("/updateTask/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody Task task, Authentication authentication) {
        logger.info("Attempting to update a task with ID: {}", id);

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<User> creatorOptional = userRepository.findById(userDetails.getId());
            if (creatorOptional.isPresent()) {
                User user = creatorOptional.get();
                taskService.updateTask(id, task, user);
                return new ResponseEntity<>(ConstUtils.TASK_UPDATED_SUCCESSFULLY, HttpStatus.OK);
            } else {
                logger.error("User with ID {} not found", userDetails.getId());
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            logger.error("Error updating task with ID: {}", id, e);
            return new ResponseEntity<>("Error updating task", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteTask/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        logger.info("Attempting to delete a task with ID: {}", id);
        try {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while deleting a task with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allTasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDTO>> getAllTask(){
        try {
            List<TaskDTO> tasks = taskService.getAllTasks();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while getting all tasks", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/myTasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        try {
            List<TaskDTO> tasks = taskService.getAllTasksByCurrentUser(id);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            logger.error("No tasks found", e);
            return new ResponseEntity<>(ConstUtils.USER_ID_MISMATCH, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while getting all tasks", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
