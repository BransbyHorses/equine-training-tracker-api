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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        // given
        Equine equine = new Equine(1L, "First Horse", new Yard(), categoryInstance, new Programme(), new HashSet<Skill>());
        categoryInstance.setEquines(new HashSet<>(List.of(equine)));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(categoryInstance));
        given(equineRepository.getById(1L)).willReturn(equine);
        // when
        categoryServiceUnderTest.deleteCategory(1L);
        // then
        Assertions.assertNull(equine.getCategory());
        Mockito.verify(categoryRepository).deleteById(1L);
    }
}