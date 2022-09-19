package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Environment;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.EnvironmentDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EnvironmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = EnvironmentController.class)
class EnvironmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EnvironmentService environmentService;

    @Mock
    private ObjectMapper objectMapper;

    private List<Environment> environments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        environments.add(new Environment(1L, "New Environment"));
        environments.add(new Environment(2L, "New Environment"));
        environments.add(new Environment(3L, "New Environment"));
    }

    @Test
    void canGetEnvironmentsAndReturnOkResponse() throws Exception {
        given(environmentService.getEnvironments()).willReturn(environments);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/environments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(environments.size()));
    }

    @Test
    void canGetEnvironmentAndReturnOkResponse() throws Exception {
        given(environmentService.getEnvironment(1L)).willReturn(environments.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/environments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void canCreateEnvironmentAndReturnOkResponse() throws Exception {
        Environment newEnvironment = new Environment(4L, "New Environment");
        given(environmentService.createEnvironment(ArgumentMatchers.any(Environment.class))).willAnswer((invocation -> invocation.getArgument(0)));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/environments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEnvironment)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4L));
    }

    @Test
    @Disabled
    void canUpdateEnvironmentAndReturnOkResponse() throws Exception {}

    @Test
    void deleteEnvironmentAndReturnOkResponse() throws Exception {
        given(environmentService.getEnvironment(1L)).willReturn(environments.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/environments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}