package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.LearnerType;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.LearnerTypeRepository;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LearnerTypeServiceTest {

    @Mock
    LearnerTypeRepository learnerTypeRepository;

    @InjectMocks
    LearnerTypeService learnerTypeService;

    LearnerType learnerType;

    @BeforeEach
    void setUp() {
        this.learnerTypeService = new LearnerTypeService(learnerTypeRepository);
         this.learnerType = new LearnerType(1L,"Learner Type");
    }

    @Test
    void getLearnerTypes() {
        given(learnerTypeRepository.findAll()).willReturn(List.of(learnerType));
        List<LearnerType> learnerTypes = learnerTypeService.getLearnerTypes();
        assertEquals(List.of(learnerType), learnerTypes);
    }

    @Test
    void getLearnerType() {
        given(learnerTypeRepository.findById(1L)).willReturn(Optional.of(learnerType));
        LearnerType learnerTypeFromDb = learnerTypeService.getLearnerType(1L);
        assertEquals(learnerType, learnerTypeFromDb);
    }

    @Test
    void createLearnerType() {
        given(learnerTypeRepository.saveAndFlush(learnerType)).willReturn(learnerType);
        LearnerType savedLearnerType = learnerTypeService.createLearnerType(learnerType);
        assertEquals(1L, savedLearnerType.getId());
        assertEquals("Learner Type", learnerType.getName());
    }

    @Test
    void updateLearnerType() {
        LearnerType updatedLearnerType = new LearnerType(1L, "Updated Learner Type");
        given(learnerTypeRepository.findById(1L)).willReturn(Optional.of(learnerType));
        given(learnerTypeRepository.saveAndFlush(learnerType)).willReturn(learnerType);
        LearnerType savedUpdatedLearnerType = learnerTypeService.updateLearnerType(1L, updatedLearnerType);
        assertEquals(1L, savedUpdatedLearnerType.getId());
        assertEquals("Updated Learner Type", savedUpdatedLearnerType.getName());
    }

    @Test
    void deleteLearnerType() {
        given(learnerTypeRepository.findById(1L)).willReturn(Optional.of(learnerType));
        learnerTypeService.deleteLearnerType(1L);
        Mockito.verify(learnerTypeRepository, times(1)).delete(learnerType);
    }
}