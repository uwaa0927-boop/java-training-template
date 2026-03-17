package com.example.weatherapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PracticeController.class)
class PracticeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void thymeleafBasic_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/practice/thymeleaf-basic"))
                .andExpect(status().isOk());
    }

    @Test
    void layoutExample_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/practice/layout-example"))
                .andExpect(status().isOk());
    }
}