package com.example.taskflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskFlowApplicationTests {
    @Autowired MockMvc mockMvc;

    @Test
void homePageLoads() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("forward:index.html"));
}

    @Test
    void tasksCanBeRetrieved() throws Exception {
        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void canCreateTask() throws Exception {
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"Test task\",\"dueDate\":\"2026-12-01\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Test task"));
    }

    @Test
    void rejectsBlankTaskTitle() throws Exception {
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"   \"}"))
            .andExpect(status().isBadRequest());
    }
}
