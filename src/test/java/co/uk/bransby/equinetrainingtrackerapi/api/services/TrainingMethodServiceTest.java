package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingMethodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingMethodServiceTest {

    @Mock
    TrainingMethodRepository trainingMethodRepository;

    @InjectMocks
    TrainingMethodService trainingMethodService;
    TrainingMethod trainingMethod;

    @BeforeEach
    void setup() {
        trainingMethodService = new TrainingMethodService(trainingMethodRepository);
        trainingMethod = new TrainingMethod(1L, "Test Training Method", "Description...");
    }

    @Test
    void willListMethods() {
        List<TrainingMethod> mockTrainingMethods = new ArrayList<>(List.of(
           new TrainingMethod(), new TrainingMethod(), new TrainingMethod()
        ));
        given(trainingMethodRepository.findAll()).willReturn(mockTrainingMethods);
        List< TrainingMethod> returnedTrainingMethods = trainingMethodService.listMethods();
        Assertions.assertEquals(mockTrainingMethods, returnedTrainingMethods);
    }

    @Test
    void willListMethod() {
        given(trainingMethodRepository.findById(1L)).willReturn(Optional.ofNullable(trainingMethod));
        TrainingMethod returnedTrainingMethod = trainingMethodService.listMethod(1L);
        Assertions.assertEquals(returnedTrainingMethod.getId(), trainingMethod.getId());
        Assertions.assertEquals(returnedTrainingMethod.getName(), trainingMethod.getName());
        Assertions.assertEquals(returnedTrainingMethod.getDescription(), trainingMethod.getDescription());
    }

    @Test
    void willThrowExceptionWhenTrainingMethodNotFoundByIdAndReturned() {
        given(trainingMethodRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> trainingMethodService.listMethod(1L)
        );
        assertThrows(EntityNotFoundException.class, () -> trainingMethodService.listMethod(1L));
        assertEquals("No training method found with id: 1", exception.getMessage());
    }

    @Test
    void willCreateMethod() {
        given(trainingMethodRepository.saveAndFlush(trainingMethod)).willReturn(trainingMethod);
        TrainingMethod createdTrainingMethod = trainingMethodService.createMethod(trainingMethod);
        Assertions.assertEquals(createdTrainingMethod.getId(), trainingMethod.getId());
        Assertions.assertEquals(createdTrainingMethod.getName(), trainingMethod.getName());
        Assertions.assertEquals(createdTrainingMethod.getDescription(), trainingMethod.getDescription());
    }

    @Test
    void willUpdateMethod() {
        TrainingMethod updatedTrainingMethodValues = new TrainingMethod(1L, "Updated Training Method Name", "Updated Description");
        given(trainingMethodRepository.findById(1L)).willReturn(Optional.ofNullable(trainingMethod));
        given(trainingMethodRepository.saveAndFlush(trainingMethod)).willReturn(trainingMethod);
        TrainingMethod updatedAndSavedTrainingMethod = trainingMethodService.updateMethod(1L, updatedTrainingMethodValues);
        Assertions.assertEquals(1L, updatedAndSavedTrainingMethod.getId());
        Assertions.assertEquals(updatedAndSavedTrainingMethod.getName(), updatedTrainingMethodValues.getName());
        Assertions.assertEquals(updatedAndSavedTrainingMethod.getDescription(), updatedTrainingMethodValues.getDescription());
    }

    @Test
    void willDeleteMethod() {
        given(trainingMethodRepository.existsById(1L)).willReturn(true);
        trainingMethodService.deleteMethod(1L);
        Mockito.verify(trainingMethodRepository).deleteById(1L);
    }

    @Test
    void willThrowExceptionWhenTrainingMethodNotFoundAndDeleted() {
        given(trainingMethodRepository.existsById(1L)).willReturn(false);
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> trainingMethodService.deleteMethod(1L)
        );
        assertThrows(EntityNotFoundException.class, () -> trainingMethodService.deleteMethod(1L));
        assertEquals("No training method found with id: 1", exception.getMessage());
    }
}