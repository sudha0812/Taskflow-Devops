package com.example.taskflow;

import java.time.LocalDate;

public record Task(long id, String title, LocalDate dueDate, String status) {}
