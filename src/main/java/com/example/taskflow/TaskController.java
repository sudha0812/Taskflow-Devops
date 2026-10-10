package com.example.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final AtomicLong ids = new AtomicLong();

    public TaskController() {
        tasks.add(new Task(ids.incrementAndGet(), "Prepare CI/CD demo", LocalDate.now().plusDays(2), "In Progress"));
        tasks.add(new Task(ids.incrementAndGet(), "Review assignment", LocalDate.now().plusDays(5), "To Do"));
    }

    @GetMapping
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody CreateTask request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task title is required");
        }
        Task task = new Task(ids.incrementAndGet(), request.title().trim(), request.dueDate(), "To Do");
        tasks.add(task);
        return task;
    }

    @PatchMapping("/{id}/status")
    public Task updateStatus(@PathVariable long id, @RequestBody StatusUpdate request) {
        for (int i = 0; i < tasks.size(); i++) {
            Task current = tasks.get(i);
            if (current.id() == id) {
                if (request.status() == null || !List.of("To Do", "In Progress", "Done").contains(request.status())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported status");
                }
                Task updated = new Task(current.id(), current.title(), current.dueDate(), request.status());
                tasks.set(i, updated);
                return updated;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }

    public record CreateTask(String title, LocalDate dueDate) {}
    public record StatusUpdate(String status) {}
}
