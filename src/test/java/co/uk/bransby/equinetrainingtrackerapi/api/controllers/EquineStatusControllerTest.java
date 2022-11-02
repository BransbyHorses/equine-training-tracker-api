package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;

import static org.mockito.BDDMockito.given;

import java.util.*;

@WebMvcTest(controllers = EquineStatusController.class)
class EquineStatusControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EquineStatusService equineStatusService;

    private List<EquineStatus> categories;

    @BeforeEach
    void setUp() {
        this.categories = new ArrayList<>();
        categories.add(new EquineStatus(1L, "Test Category 1", new HashSet<>()));
        categories.add(new EquineStatus(2L, "Test Category 2", new HashSet<>()));
        categories.add(new EquineStatus(3L, "Test Category 3", new HashSet<>()));
        categories.add(new EquineStatus(4L, "Test Category 4", new HashSet<>()));
        categories.add(new EquineStatus(5L, "Test Category 5", new HashSet<>()));
    }

    @Test
    void willGetCategoriesAndReturnOkResponse() throws Exception {
        given(equineStatusService.getCategories()).willReturn(categories);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equine-statuses"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(categories.size()));
    }

    @Test
    void willGetCategoryAndReturnOkResponse() throws Exception {
        given(equineStatusService.getCategory(1L)).willReturn(categories.get(0));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/equine-statuses/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category 1"));
    }

    @Test
    void willCreateCategoryAndReturnCreatedResponse() throws Exception {
        EquineStatus newCategory =  new EquineStatus(6L, "New Category", new HashSet<>());
        given(equineStatusService.createCategory(newCategory)).willReturn(newCategory);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/equine-statuses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(newCategory)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Category"));
    }

    @Test
    void willUpdateCategoryAndReturnOkResponse() throws Exception {
        EquineStatus updatedCategory = new EquineStatus(7L, "Updated Category", new HashSet<>());
        given(equineStatusService.updateCategory(7L, updatedCategory)).willReturn(updatedCategory);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equine-statuses/{id}", updatedCategory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedCategory)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Category"));
    }

    @Test
    void willReturnNotFoundResponseWhenCategoryWasNotFoundAndUpdated() throws Exception {
        EquineStatus updatedCategory = new EquineStatus(7L, "Updated Category", new HashSet<>());
        given(equineStatusService.updateCategory(7L, updatedCategory))
                .willThrow(new EntityNotFoundException());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/equine-statuses/{id}", updatedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCategory)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void willDeleteCategoryAndReturnOkResponse() throws Exception {
        given(equineStatusService.getCategory(1L)).willReturn(categories.get(0));
        Mockito.doNothing().when(equineStatusService).deleteCategory(1L);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/equine-statuses/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}