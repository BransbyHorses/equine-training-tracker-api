package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.CategoryRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    EquineRepository equineRepository;

    @InjectMocks
    CategoryService categoryServiceUnderTest;
    Category categoryInstance;

    @BeforeEach
    void setup() {
        categoryServiceUnderTest = new CategoryService(categoryRepository, equineRepository);
        categoryInstance = new Category(1L, "Test Category", new HashSet<>());
    }

    @Test
    void canGetCategories() {
        List<Category> categoryList = new ArrayList<>(List.of(new Category(), new Category()));
        given(categoryRepository.findAll()).willReturn(categoryList);
        List<Category> categories = categoryServiceUnderTest.getCategories();
        Assertions.assertEquals(categories, categoryList);
    }

    @Test
    void canGetCategory() {
        given(categoryRepository.findById(1L)).willReturn(Optional.ofNullable(categoryInstance));
        Optional<Category> category = categoryServiceUnderTest.getCategory(1L);
        Assertions.assertEquals(category.get().getId(), categoryInstance.getId());
        Assertions.assertEquals(category.get().getName(), categoryInstance.getName());
        Assertions.assertEquals(category.get().getEquines(), categoryInstance.getEquines());
    }

    @Test
    void canCreateCategory() {
        given(categoryRepository.saveAndFlush(categoryInstance)).willReturn(categoryInstance);
        Category category = categoryServiceUnderTest.createCategory(categoryInstance);
        Assertions.assertEquals(category.getId(), categoryInstance.getId());
        Assertions.assertEquals(category.getName(), categoryInstance.getName());
        Assertions.assertEquals(category.getEquines(), categoryInstance.getEquines());
    }

    @Test
    void canUpdateCategory() {
        Category updatedCategoryValues = new Category(1L, "Updated Category Value", new HashSet<>());
        given(categoryRepository.findById(categoryInstance.getId())).willReturn(Optional.ofNullable(categoryInstance));
        given(categoryRepository.saveAndFlush(categoryInstance)).willReturn(categoryInstance);
        Category updatedCategory = categoryServiceUnderTest.updateCategory(categoryInstance.getId(), updatedCategoryValues);
        Assertions.assertEquals(updatedCategoryValues.getId(), 1L);
        Assertions.assertEquals(updatedCategoryValues.getName(), updatedCategory.getName());
        Assertions.assertEquals(updatedCategoryValues.getEquines(), updatedCategory.getEquines());
    }

    @Test
    void willThrowExceptionWhenCategoryWasNotFoundAndUpdated() {
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryServiceUnderTest.updateCategory(1L, categoryInstance)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () -> categoryServiceUnderTest.updateCategory(1L, categoryInstance));
        Assertions.assertEquals("No category found with id: " + categoryInstance.getId(), exception.getMessage());
    }

    @Test
    void canDeleteCategory() {
        Equine equine = new Equine(1L, "First Horse", new Yard(), categoryInstance, new Programme(), new HashSet<Skill>());
        categoryInstance.setEquines(new HashSet<>(List.of(equine)));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(categoryInstance));
        categoryServiceUnderTest.deleteCategory(1L);
        Assertions.assertNull(equine.getCategory());
        Mockito.verify(categoryRepository).deleteById(1L);
    }
}