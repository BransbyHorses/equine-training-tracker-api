package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingEnvironment;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EnvironmentService;
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

import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = EnvironmentController.class)
class TrainingEnvironmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EnvironmentService environmentService;

    @Mock
    private ObjectMapper objectMapper;

    private List<TrainingEnvironment> environments;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        this.environments = new ArrayList<>();
        environments.add(new TrainingEnvironment(1L, "New Environment"));
        environments.add(new TrainingEnvironment(2L, "New Environment"));
        environments.add(new TrainingEnvironment(3L, "New Environment"));
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
        TrainingEnvironment newEnvironment = new TrainingEnvironment(4L, "New Environment");
        given(environmentService.createEnvironment(ArgumentMatchers.any(TrainingEnvironment.class))).willAnswer((invocation -> invocation.getArgument(0)));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/environments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEnvironment)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4L));
    }

    @Test
    void deleteEnvironmentAndReturnOkResponse() throws Exception {
        given(environmentService.getEnvironment(1L)).willReturn(environments.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/environments/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}