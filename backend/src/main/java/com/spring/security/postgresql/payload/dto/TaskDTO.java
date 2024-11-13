package com.spring.security.postgresql.payload.dto;

import com.spring.security.postgresql.models.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

public class TaskDTO {
    private String uuid;
    private String title;
    private String status;
    private String description;
    private String assignedUserEmail;
    private String createdBy;
    private String updatedBy;
    private LocalDate createdOn;
    private LocalDate updatedOn;

    public TaskDTO(String uuid, String title, String status, String description, String assignedUserEmail, String createdBy, String updatedBy, LocalDate createdOn, LocalDate updatedOn) {
        this.uuid = uuid;
        this.title = title;
        this.status = status;
        this.description = description;
        this.assignedUserEmail = assignedUserEmail;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public TaskDTO() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedUserEmail() {
        return assignedUserEmail;
    }

    public void setAssignedUserEmail(String assignedUserEmail) {
        this.assignedUserEmail = assignedUserEmail;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }
}
