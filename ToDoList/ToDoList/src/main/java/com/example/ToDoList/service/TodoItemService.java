package com.example.ToDoList.service;

import com.example.ToDoList.model.TodoItem;
import com.example.ToDoList.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {

    private static final Logger logger = LoggerFactory.getLogger(TodoItemService.class);

    @Autowired
    private TodoItemRepository todoItemRepository;

    public List<TodoItem> getAllTodos() {
        try {
            logger.info("Fetching all to-do items");
            return todoItemRepository.findAll();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all to-do items: ", e);
            throw e;  // Propagate the exception to be handled by a global error handler
        }
    }

    public Optional<TodoItem> getTodoById(Long id) {
        try {
            logger.info("Fetching to-do item with id: {}", id);
            return todoItemRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error occurred while fetching to-do item with id: {}", id, e);
            throw e;
        }
    }

    public TodoItem createTodo(TodoItem todoItem) {
        try {
            logger.info("Creating a new to-do item");
            todoItem.setCreatedAt(LocalDateTime.now());
            todoItem.setStatus(TodoItem.Status.PENDING);
            return todoItemRepository.save(todoItem);
        } catch (Exception e) {
            logger.error("Error occurred while creating a new to-do item: ", e);
            throw e;
        }
    }

    public TodoItem updateTodo(Long id, TodoItem todoItem) {
        try {
            logger.info("Updating to-do item with id: {}", id);
            Optional<TodoItem> existingTodo = todoItemRepository.findById(id);
            if (existingTodo.isPresent()) {
                TodoItem updatedTodo = existingTodo.get();
                updatedTodo.setName(todoItem.getName());
                updatedTodo.setDescription(todoItem.getDescription());
                updatedTodo.setStatus(todoItem.getStatus());
                // Set completedAt based on the status
                if (todoItem.getStatus() == TodoItem.Status.COMPLETED) {
                    updatedTodo.setCompletedAt(LocalDateTime.now());
                } else {
                    updatedTodo.setCompletedAt(null);
                    updatedTodo.setStatus(TodoItem.Status.PENDING);
                }
                return todoItemRepository.save(updatedTodo);
            } else {
                logger.warn("To-do item with id {} not found for update", id);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating to-do item with id: {}", id, e);
            throw e;
        }
    }

    public void deleteTodoById(Long id) {
        try {
            logger.info("Deleting to-do item with id: {}", id);
            todoItemRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting to-do item with id: {}", id, e);
            throw e;
        }
    }

    public long countTodosCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            logger.info("Counting to-do items created between {} and {}", startDate, endDate);
            return todoItemRepository.countByCreatedAtBetween(startDate, endDate);
        } catch (Exception e) {
            logger.error("Error occurred while counting to-do items created between {} and {}", startDate, endDate, e);
            throw e;
        }
    }

    public long countTodosCompletedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            logger.info("Counting to-do items completed between {} and {}", startDate, endDate);
            return todoItemRepository.countByCompletedAtBetween(startDate, endDate);
        } catch (Exception e) {
            logger.error("Error occurred while counting to-do items completed between {} and {}", startDate, endDate, e);
            throw e;
        }
    }
}
