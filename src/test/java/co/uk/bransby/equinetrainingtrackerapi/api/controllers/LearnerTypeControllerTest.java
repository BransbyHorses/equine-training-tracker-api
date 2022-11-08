package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.LearnerType;
import co.uk.bransby.equinetrainingtrackerapi.api.services.LearnerTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LearnerTypeController.class)
@ActiveProfiles("test")
class LearnerTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LearnerTypeService learnerTypeService;

    private List<LearnerType> learnerTypes;

    @BeforeEach
    void setUp() {
        this.learnerTypes = new ArrayList<>();
        this.learnerTypes.add(new LearnerType(1L, "Test Learner Type"));
    }

    @Test
    void getLearnerTypes() throws Exception {
        given(learnerTypeService.getLearnerTypes()).willReturn(learnerTypes);
        this.mockMvc.perform(get("/data/learner-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(learnerTypes.size()));
    }

    @Test
    void getLearnerType() throws Exception {
        given(learnerTypeService.getLearnerType(1L)).willReturn(learnerTypes.get(0));
        this.mockMvc.perform(get("/data/learner-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(learnerTypes.get(0).getId()))
                .andExpect(jsonPath("$.name").value(learnerTypes.get(0).getName()));
    }

    @Test
    void createLearnerType() throws Exception {
        given(learnerTypeService.getLearnerType(1L)).willReturn(learnerTypes.get(0));
        this.mockMvc.perform(get("/data/learner-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(learnerTypes.get(0).getId()));
    }

    @Test
    void deleteLearnerType() throws Exception {
        doNothing().when(learnerTypeService).deleteLearnerType(1L);
        this.mockMvc.perform(delete("/data/learner-types/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Learner type deleted with id: 1"));
    }
}