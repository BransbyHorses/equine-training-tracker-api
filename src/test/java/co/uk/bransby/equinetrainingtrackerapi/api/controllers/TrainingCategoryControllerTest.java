package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingCategory;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = TrainingCategoryController.class)
@ActiveProfiles("test")
class TrainingCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    TrainingCategoryService trainingCategoryService;

    private List<TrainingCategory> trainingCategories;

    @BeforeEach
    void setUp() {
        this.trainingCategories = new ArrayList<>();
        this.trainingCategories.add(new TrainingCategory(1L, "Training Category"));
    }

    @Test
    void getTrainingCategoriesAndReturnOkResponse() throws Exception {
        given(trainingCategoryService.getTrainingCategories()).willReturn(trainingCategories);
        this.mockMvc.perform(get("/data/training-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(trainingCategories.size()));
    }

    @Test
    void getTrainingMethodAndReturnOkResponse() throws Exception {
        given(trainingCategoryService.getTrainingCategory(1L)).willReturn(trainingCategories.get(0));
        this.mockMvc.perform(get("/data/training-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(trainingCategories.get(0).getId()));
    }

    @Test
    void createTrainingCategoryAndReturnCreatedResponse() throws Exception {

        given(trainingCategoryService.createTrainingCategory(any(TrainingCategory.class)))
                .willAnswer((answer) -> answer.getArgument(0));
        this.mockMvc.perform(post("/data/training-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new TrainingCategory(2L, "New Training Category"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New Training Category"));
    }

    @Test
    void deleteTrainingCategory() throws Exception {
        doNothing().when(trainingCategoryService).deleteTrainingCategory(1L);
        this.mockMvc.perform(delete("/data/training-categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Training category with id 1 deleted"));
    }
}