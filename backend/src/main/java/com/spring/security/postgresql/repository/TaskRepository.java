package com.spring.security.postgresql.repository;

import com.spring.security.postgresql.models.Task;
import com.spring.security.postgresql.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT COUNT(t) FROM Task t WHERE t.uuid = :uuid")
    Long countByUuid(String uuid);

    @Query("SELECT t FROM Task t WHERE t.assignedUser =: currentUser")
    List<Task> findByAssignedUser(Long currentUser);
}
