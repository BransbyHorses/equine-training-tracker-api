package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineStatusRepository;
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
class EquineStatusServiceTest {

    @Mock
    EquineStatusRepository equineStatusRepository;
    @Mock
    EquineRepository equineRepository;

    @InjectMocks
    EquineStatusService equineStatusServiceUnderTest;
    EquineStatus categoryInstance;

    @BeforeEach
    void setup() {
        equineStatusServiceUnderTest = new EquineStatusService(equineStatusRepository, equineRepository);
        categoryInstance = new EquineStatus(1L, "Test Category", new HashSet<>());
    }

    @Test
    void canGetCategories() {
        List<EquineStatus> categoryList = new ArrayList<>(List.of(new EquineStatus(), new EquineStatus()));
        given(equineStatusRepository.findAll()).willReturn(categoryList);
        List<EquineStatus> categories = equineStatusServiceUnderTest.getCategories();
        Assertions.assertEquals(categories, categoryList);
    }

    @Test
    void canGetCategory() {
        given(equineStatusRepository.findById(1L)).willReturn(Optional.ofNullable(categoryInstance));
        EquineStatus category = equineStatusServiceUnderTest.getCategory(1L);
        Assertions.assertEquals(category.getId(), categoryInstance.getId());
        Assertions.assertEquals(category.getName(), categoryInstance.getName());
        Assertions.assertEquals(category.getEquines(), categoryInstance.getEquines());
    }

    @Test
    void canCreateCategory() {
        given(equineStatusRepository.saveAndFlush(categoryInstance)).willReturn(categoryInstance);
        EquineStatus category = equineStatusServiceUnderTest.createCategory(categoryInstance);
        Assertions.assertEquals(category.getId(), categoryInstance.getId());
        Assertions.assertEquals(category.getName(), categoryInstance.getName());
        Assertions.assertEquals(category.getEquines(), categoryInstance.getEquines());
    }

    @Test
    void canUpdateCategory() {
        EquineStatus updatedCategoryValues = new EquineStatus(1L, "Updated Category Value", new HashSet<>());
        given(equineStatusRepository.findById(categoryInstance.getId())).willReturn(Optional.ofNullable(categoryInstance));
        given(equineStatusRepository.saveAndFlush(categoryInstance)).willReturn(categoryInstance);
        EquineStatus updatedCategory = equineStatusServiceUnderTest.updateCategory(categoryInstance.getId(), updatedCategoryValues);
        Assertions.assertEquals(updatedCategoryValues.getId(), 1L);
        Assertions.assertEquals(updatedCategoryValues.getName(), updatedCategory.getName());
        Assertions.assertEquals(updatedCategoryValues.getEquines(), updatedCategory.getEquines());
    }

    @Test
    void willThrowExceptionWhenCategoryWasNotFoundAndUpdated() {
        given(equineStatusRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineStatusServiceUnderTest.updateCategory(1L, categoryInstance)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () -> equineStatusServiceUnderTest.updateCategory(1L, categoryInstance));
        Assertions.assertEquals("No category found with id: " + categoryInstance.getId(), exception.getMessage());
    }

    @Test
    void canDeleteCategory() {
        Equine equine = new Equine(1L, "First Horse", new Yard(), categoryInstance, new ArrayList<>(), new LearnerType());
        categoryInstance.setEquines(new HashSet<>(List.of(equine)));
        given(equineStatusRepository.findById(1L)).willReturn(Optional.of(categoryInstance));
        equineStatusServiceUnderTest.deleteCategory(1L);
        Assertions.assertNull(equine.getEquineStatus());
        Mockito.verify(equineStatusRepository).deleteById(1L);
    }
}