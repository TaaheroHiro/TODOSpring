package com.example.ToDoList.controller;

import com.example.ToDoList.model.TodoItem;
import com.example.ToDoList.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/")
public class TodoItemWebController {

    private static final Logger logger = LoggerFactory.getLogger(TodoItemWebController.class);

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping
    public String getAllTodos(Model model) {
        try {
            logger.info("Fetching all to-do items for web view");
            List<TodoItem> todoItems = todoItemService.getAllTodos();
            model.addAttribute("todos", todoItems);
            model.addAttribute("newTodo", new TodoItem());
            return "todo-list";
        } catch (Exception e) {
            logger.error("Error occurred while fetching to-do items for web view: ", e);
            model.addAttribute("errorMessage", "An error occurred while loading the to-do list. Please try again.");
            return "error";  // Assuming you have a generic error page
        }
    }

    @PostMapping("/create")
    public String createTodo(@ModelAttribute TodoItem todoItem) {
        try {
            logger.info("Creating a new to-do item");
            todoItemService.createTodo(todoItem);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error occurred while creating a new to-do item: ", e);
            return "error";  // Redirect to error page in case of an exception
        }
    }

    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id, @ModelAttribute TodoItem todoItem) {
        try {
            logger.info("Updating to-do item with id: {}", id);
            todoItemService.updateTodo(id, todoItem);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error occurred while updating to-do item with id: {}", id, e);
            return "error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable Long id, Model model) {
        try {
            logger.info("Fetching to-do item with id: {} for editing", id);
            TodoItem todoItem = todoItemService.getTodoById(id).orElse(new TodoItem());
            model.addAttribute("todo", todoItem);
            return "edit-todo";
        } catch (Exception e) {
            logger.error("Error occurred while fetching to-do item with id: {} for editing: ", id, e);
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        try {
            logger.info("Deleting to-do item with id: {}", id);
            todoItemService.deleteTodoById(id);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("Error occurred while deleting to-do item with id: {}", id, e);
            return "error";
        }
    }
}
