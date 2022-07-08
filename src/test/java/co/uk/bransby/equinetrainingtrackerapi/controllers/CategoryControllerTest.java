package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.services.CategoryService;
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

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    private List<Category> categories;

    @BeforeEach
    void setUp() {
        this.categories = new ArrayList<>();
        categories.add(new Category(1L, "Test Category 1", new HashSet<>()));
        categories.add(new Category(2L, "Test Category 2", new HashSet<>()));
        categories.add(new Category(3L, "Test Category 3", new HashSet<>()));
        categories.add(new Category(4L, "Test Category 4", new HashSet<>()));
        categories.add(new Category(5L, "Test Category 5", new HashSet<>()));
    }

    @Test
    void willGetCategoriesAndReturnOkResponse() throws Exception {
        given(categoryService.getCategories()).willReturn(categories);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(categories.size()));
    }

    @Test
    void willGetCategoryAndReturnOkResponse() throws Exception {
        given(categoryService.getCategory(1L)).willReturn(Optional.of(categories.get(0)));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/categories/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category 1"));
    }

    @Test
    void willReturnNotFoundResponseWhenCategoryWasNotFound() throws Exception {
        given(categoryService.getCategory(1L)).willReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/categories/{id}", 6))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void willCreateCategoryAndReturnCreatedResponse() throws Exception {
        Category newCategory =  new Category(6L, "New Category", new HashSet<>());
        given(categoryService.createCategory(newCategory)).willReturn(newCategory);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/data/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(newCategory)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Category"));
    }

    @Test
    void willUpdateCategoryAndReturnOkResponse() throws Exception {
        Category updatedCategory = new Category(7L, "Updated Category", new HashSet<>());
        given(categoryService.updateCategory(7L, updatedCategory)).willReturn(updatedCategory);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/categories/{id}", updatedCategory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedCategory)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Category"));
    }

    @Test
    void willReturnNotFoundResponseWhenCategoryWasNotFoundAndUpdated() throws Exception {
        Category updatedCategory = new Category(7L, "Updated Category", new HashSet<>());
        given(categoryService.updateCategory(7L, updatedCategory))
                .willThrow(new EntityNotFoundException());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/data/categories/{id}", updatedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCategory)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void willDeleteCategoryAndReturnOkResponse() throws Exception {
        given(categoryService.getCategory(1L)).willReturn(Optional.of(categories.get(0)));
        Mockito.doNothing().when(categoryService).deleteCategory(1L);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/categories/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category 1"));
    }

    @Test
    void WillReturnNotFoundResponseWhenCategoryWasNotFoundAndDeleted() throws Exception {
        given(categoryService.getCategory(1L)).willReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/data/categories/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}