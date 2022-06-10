package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryServiceUnderTest;
    Category categoryInstance;

    @BeforeEach
    void setup() {
        categoryServiceUnderTest = new CategoryService(categoryRepository);
        categoryInstance = new Category(1L, "Test Category");
    }

    @Test
    void canGetCategories() {
        categoryServiceUnderTest.getCategories();
        Mockito.verify(categoryRepository).findAll();
    }

    @Test
    void canGetCategory() {
        categoryServiceUnderTest.getCategory(1L);
        Mockito.verify(categoryRepository).findById(1L);
    }

    @Test
    void canCreateCategory() {
        categoryServiceUnderTest.createCategory(categoryInstance);
        Mockito.verify(categoryRepository).saveAndFlush(categoryInstance);
    }

    @Test
    void canUpdateCategory() {
        given(categoryRepository.findById(categoryInstance.getId())).willReturn(Optional.ofNullable(categoryInstance));
        categoryServiceUnderTest.updateCategory(categoryInstance.getId(), categoryInstance);
        Mockito.verify(categoryRepository).saveAndFlush(categoryInstance);
    }

    @Test
    void willThrowExceptionWhenCategoryWasNotFoundAndUpdated() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryServiceUnderTest.updateCategory(1L, categoryInstance));
    }

    @Test
    void canDeleteCategory() {
        categoryServiceUnderTest.deleteCategory(1L);
        Mockito.verify(categoryRepository).deleteById(1L);
    }
}