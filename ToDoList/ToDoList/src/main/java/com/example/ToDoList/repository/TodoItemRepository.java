package com.example.ToDoList.repository;

import com.example.ToDoList.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    // Count to-dos created between two dates
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Count to-dos completed between two dates
    long countByCompletedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}