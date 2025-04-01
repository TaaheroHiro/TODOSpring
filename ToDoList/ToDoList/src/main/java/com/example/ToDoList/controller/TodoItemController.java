package com.example.ToDoList.controller;

import com.example.ToDoList.model.TodoItem;
import com.example.ToDoList.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/todos")
public class TodoItemController {

    private static final Logger logger = LoggerFactory.getLogger(TodoItemController.class);

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping
    public List<TodoItem> getAllTodos() {
        try {
            logger.info("Fetching all to-do items");
            return todoItemService.getAllTodos();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all to-do items: ", e);
            throw e; // rethrow exception to be handled globally or by a custom error handler
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoById(@PathVariable Long id) {
        try {
            logger.info("Fetching to-do item with id: {}", id);
            Optional<TodoItem> todoItem = todoItemService.getTodoById(id);
            return todoItem.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("To-do item with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error occurred while fetching to-do item with id: {}", id, e);
            throw e;
        }
    }

    @PostMapping
    public TodoItem createTodo(@RequestBody TodoItem todoItem) {
        try {
            logger.info("Creating a new to-do item");
            return todoItemService.createTodo(todoItem);
        } catch (Exception e) {
            logger.error("Error occurred while creating a new to-do item: ", e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
        try {
            logger.info("Deleting to-do item with id: {}", id);
            todoItemService.deleteTodoById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error occurred while deleting to-do item with id: {}", id, e);
            throw e;
        }
    }

    @PatchMapping("/complete/{id}")
    public ResponseEntity<Void> toggleCompletion(@PathVariable Long id) {
        try {
            logger.info("Toggling completion status for to-do item with id: {}", id);
            Optional<TodoItem> todoItem = todoItemService.getTodoById(id);
            if (todoItem.isPresent()) {
                TodoItem item = todoItem.get();
                item.setStatus(item.getStatus() == TodoItem.Status.COMPLETED ? TodoItem.Status.PENDING : TodoItem.Status.COMPLETED);
                todoItemService.updateTodo(id, item);
                return ResponseEntity.ok().build();
            } else {
                logger.warn("To-do item with id {} not found for toggling completion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred while toggling completion for to-do item with id: {}", id, e);
            throw e;
        }
    }
}
