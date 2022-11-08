package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingMethodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
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

@WebMvcTest(controllers = TrainingMethodController.class)
class TrainingMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrainingMethodService trainingMethodService;

    private List<TrainingMethod> trainingMethods;

    @BeforeEach
    void setUp() {
        this.trainingMethods = new ArrayList<>();
        trainingMethods.add(new TrainingMethod(1L, "Test Training Method 1", ""));
        trainingMethods.add(new TrainingMethod(2L, "Test Training Method 2", ""));
        trainingMethods.add(new TrainingMethod(3L, "Test Training Method 3", ""));
        trainingMethods.add(new TrainingMethod(4L, "Test Training Method 4", ""));
        trainingMethods.add(new TrainingMethod(5L, "Test Training Method 5", ""));
    }

    @Test
    void willGetTrainingMethodsAndReturnOkResponse() throws Exception {
        given(trainingMethodService.listMethods()).willReturn(trainingMethods);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/training-methods"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(trainingMethods.size()));
    }

    @Test
    void willGetTrainingMethodAndReturnOkResponse() throws Exception {
        given(trainingMethodService.listMethod(1L)).willReturn(trainingMethods.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/training-methods/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(trainingMethods.get(0).getId()));
    }

    @Test
    void willCreateMethodAndReturnCreatedResponse() throws Exception {
        given(trainingMethodService.createMethod(ArgumentMatchers.any(TrainingMethod.class))).willAnswer((invocation -> invocation.getArgument(0)));
        TrainingMethod newTrainingMethod = new TrainingMethod(6L, "Test Training Method 6", "");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/training-methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newTrainingMethod)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Training Method 6"));
    }

    @Test
    void willDeleteMethodAndReturnOkResponse() throws Exception {
        given(trainingMethodService.listMethod(1L)).willReturn(trainingMethods.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/training-methods/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}