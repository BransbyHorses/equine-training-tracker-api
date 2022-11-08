package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingCategory;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingCategoryRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TrainingCategoryServiceTest {

    @Mock
    TrainingCategoryRepository trainingCategoryRepository;

    @InjectMocks
    TrainingCategoryService trainingCategoryService;

    TrainingCategory trainingCategory;


    @BeforeEach
    void setUp() {
        this.trainingCategoryService = new TrainingCategoryService(trainingCategoryRepository);
        this.trainingCategory = new TrainingCategory(1L, "Test Training Category");
    }

    @Test
    void getTrainingCategories() {
        given(trainingCategoryRepository.findAll()).willReturn(List.of(trainingCategory));
        List<TrainingCategory> trainingCategories = trainingCategoryService.getTrainingCategories();
        assertEquals(List.of(trainingCategory), trainingCategories);
    }

    @Test
    void getTrainingCategory() {
        given(trainingCategoryRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(trainingCategory));
        TrainingCategory trainingCategoryFromDb = trainingCategoryService.getTrainingCategory(1L);
        assertEquals(trainingCategory, trainingCategoryFromDb);
    }

    @Test
    void createTrainingCategory() {
        given(trainingCategoryRepository.saveAndFlush(trainingCategory)).willReturn(trainingCategory);
        TrainingCategory savedTrainingCategory = trainingCategoryService.createTrainingCategory(trainingCategory);
        assertEquals(1L, savedTrainingCategory.getId());
        assertEquals("Test Training Category", savedTrainingCategory.getName());
    }

    @Test
    void updateTrainingCategory() {
        TrainingCategory updatedTrainingCategory = new TrainingCategory(1L, "Updated Training Category");
        given(trainingCategoryRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(trainingCategory));
        given(trainingCategoryRepository.saveAndFlush(trainingCategory)).willReturn(trainingCategory);
        TrainingCategory savedUpdatedTrainingCategory = trainingCategoryService.updateTrainingCategory(1L, updatedTrainingCategory);
        assertEquals(1L, savedUpdatedTrainingCategory.getId());
        assertEquals("Updated Training Category", savedUpdatedTrainingCategory.getName());
    }

    @Test
    void deleteTrainingCategory() {
        given(trainingCategoryRepository.findById(1L)).willReturn(Optional.of(trainingCategory));
        trainingCategoryService.deleteTrainingCategory(1L);
        Mockito.verify(trainingCategoryRepository, times(1)).delete(trainingCategory);
    }
}