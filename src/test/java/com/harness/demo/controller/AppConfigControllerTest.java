package com.harness.demo.controller;

import com.harness.demo.model.AppConfig;
import com.harness.demo.service.AppConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppConfigController.class)
class AppConfigControllerTest {
    @Autowired MockMvc mvc;
    @MockBean AppConfigService service;

    @Test void getConfigReturnsJson() throws Exception {
        when(service.getConfig()).thenReturn(new AppConfig(1L, "Harness", "Demo", 1));
        mvc.perform(get("/api/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heading").value("Harness"));
    }

    @Test void updateConfigReturnsOk() throws Exception {
        when(service.update("New heading", "New subtitle")).thenReturn(new AppConfig(1L, "New heading", "New subtitle", 2));
        mvc.perform(put("/api/config").contentType("application/json")
                        .content("{\"heading\":\"New heading\",\"subtitle\":\"New subtitle\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtitle").value("New subtitle"));
    }

    @Test void invalidUpdateReturnsBadRequest() throws Exception {
        when(service.update("", "x")).thenThrow(new IllegalArgumentException("Heading must not be blank"));
        mvc.perform(put("/api/config").contentType("application/json")
                        .content("{\"heading\":\"\",\"subtitle\":\"x\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Heading must not be blank"));
    }
}
