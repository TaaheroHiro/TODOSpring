package com.example.ToDoList.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "to_do")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt; // Field for creation timestamp
    private LocalDateTime completedAt; // Field for completion timestamp

    @Enumerated(EnumType.STRING)
    private Status status; // Enum for status (e.g., PENDING, COMPLETED)

    // Default constructor
    public TodoItem() {
        this.createdAt = LocalDateTime.now(); // Set creation time by default
        this.status = Status.PENDING; // Default status
    }

    // Constructor with name and description
    public TodoItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now(); // Set creation time
        this.status = Status.PENDING; // Default status
    }

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Enums
    public enum Status {
        PENDING,
        COMPLETED
    }
}
