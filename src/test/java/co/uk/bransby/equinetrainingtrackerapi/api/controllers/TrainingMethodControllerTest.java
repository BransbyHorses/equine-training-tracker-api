package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingMethodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = TrainingMethodController.class)
class TrainingMethodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrainingMethodService trainingMethodService;

    private List<TrainingMethod> trainingMethods;

    @Test
    void willGetMethods() {
        this.trainingMethods = new ArrayList<>();
        trainingMethods.add(new TrainingMethod(1L, "Test Training Method 1", ""));
        trainingMethods.add(new TrainingMethod(2L, "Test Training Method 2", ""));
        trainingMethods.add(new TrainingMethod(3L, "Test Training Method 3", ""));
        trainingMethods.add(new TrainingMethod(4L, "Test Training Method 4", ""));
        trainingMethods.add(new TrainingMethod(5L, "Test Training Method 5", ""));
    }

    @Test
    void willGetMethod() {
    }

    @Test
    void willCreateMethod() {
    }

    @Test
    void willUpdateMethod() {
    }

    @Test
    void willDeleteMethod() {
    }
}