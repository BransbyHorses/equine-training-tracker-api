package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        categories.add(new Category(1L, "Test Category 1"));
        categories.add(new Category(2L, "Test Category 2"));
        categories.add(new Category(3L, "Test Category 3"));
        categories.add(new Category(4L, "Test Category 4"));
        categories.add(new Category(5L, "Test Category 5"));

    }

    @Test
    void willGetCategoriesAndReturnOkResponse() throws Exception {
        given(categoryService.getCategories()).willReturn(categories);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/data/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(categories.size()));
    }

    @Test
    void willGetCategoryAndReturnOkResponse() {
    }

    @Test
    void willReturnNotFoundResponseWhenCategoryWasNotFound() {

    }

    @Test
    void willCreateCategoryAndReturnCreatedResponse() {
    }

    @Test
    void willUpdateCategoryAndReturnOkResponse() {
    }

    @Test
    void willDeleteCategoryAndReturnOkResponse() {
    }

    @Test
    void WillReturnNotFoundResponseWhenCategoryWasNotFoundAndDeleted(){
    }
}