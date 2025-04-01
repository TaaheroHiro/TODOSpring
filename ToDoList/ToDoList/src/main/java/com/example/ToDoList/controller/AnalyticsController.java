package com.example.ToDoList.controller;

import com.example.ToDoList.model.TodoItem;
import com.example.ToDoList.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class AnalyticsController {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping("/analytics")
    public String getAnalytics(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        try {
            logger.info("Fetching analytics for startDate: {} and endDate: {}", startDate, endDate);

            // Set default to last 7 days if dates are not provided
            if (startDate == null) {
                startDate = LocalDate.now().minusDays(7);
                logger.info("StartDate not provided. Defaulting to: {}", startDate);
            }
            if (endDate == null) {
                endDate = LocalDate.now();
                logger.info("EndDate not provided. Defaulting to: {}", endDate);
            }

            // Convert LocalDate to LocalDateTime for querying with time precision
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            // Fetching data from service
            long createdCount = todoItemService.countTodosCreatedBetween(startDateTime, endDateTime);
            long completedCount = todoItemService.countTodosCompletedBetween(startDateTime, endDateTime);

            logger.info("Todo stats fetched successfully: Created - {}, Completed - {}", createdCount, completedCount);

            // Add the newTodo object to the model
            model.addAttribute("newTodo", new TodoItem());
            model.addAttribute("createdCount", createdCount);
            model.addAttribute("completedCount", completedCount);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);

            return "todo-analytics";

        } catch (Exception e) {
            logger.error("Error occurred while fetching analytics: ", e);
            model.addAttribute("errorMessage", "An error occurred while fetching analytics. Please try again.");
            return "error";
        }
    }
}
