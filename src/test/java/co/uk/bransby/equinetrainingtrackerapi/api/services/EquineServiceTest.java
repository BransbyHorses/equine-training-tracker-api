package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EquineServiceTest {

    @Mock
    EquineRepository equineRepository;
    @Mock
    EquineStatusRepository equineStatusRepository;
    @Mock
    YardRepository yardRepository;
    @Mock
    HealthAndSafetyFlagRepository healthAndSafetyFlagRepository;
    @InjectMocks
    private EquineService equineServiceUnderTest;
    private Equine equineInstance;

    @BeforeEach
    void setUp() {
        equineServiceUnderTest = new EquineService(equineRepository, yardRepository, equineStatusRepository, healthAndSafetyFlagRepository);
        equineInstance = new Equine(1L, "First Horse", new Yard(), new EquineStatus(), new ArrayList<>(), new LearnerType(), new ArrayList<>());
    }

    @Test
    void getAllEquines() {
        equineServiceUnderTest.getAllEquines();
        Mockito.verify(equineRepository).findAll();
    }

    @Test
    void getEquine() {
        given(equineRepository.findById(1L)).willReturn(Optional.ofNullable(equineInstance));
        equineServiceUnderTest.getEquine(1L);
        Mockito.verify(equineRepository).findById(equineInstance.getId());
    }

    @Test
    void createEquine() {
        equineServiceUnderTest.createEquine(equineInstance);
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }

    @Test
    void updateEquine() {
        given(equineRepository.findById(1L)).willReturn(Optional.ofNullable(equineInstance));
        equineInstance.setName("Updated Equine");
        equineServiceUnderTest.updateEquine(1L, equineInstance);
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }

    @Test
    @Disabled
    void deleteEquine() {}

    @Test
    void throwsIfEquineNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> equineServiceUnderTest.updateEquine(1L, equineInstance));
    }

    @Test
    void willAssignEquineToYard() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(yardRepository.findById(1L)).willReturn(Optional.of(equineInstance.getYard()));
        equineServiceUnderTest.assignEquineToYard(1L, 1L);
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }

    @Test
    void willThrowEquineNotFoundExceptionWhenAssigningEquineToYard() {
        given(equineRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToYard(1L, 1L)
        );
        Assertions.assertEquals("No equine found with id: 1", exception.getMessage());
    }

    @Test
    void willThrowYardNotFoundExceptionWhenAssigningEquineToYard() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(yardRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToYard(1L, 1L)
        );
        Assertions.assertEquals("No yard found with id: 1", exception.getMessage());
    }

    @Test
    void willAssignEquineToCategory() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(equineStatusRepository.findById(1L)).willReturn(Optional.of(equineInstance.getEquineStatus()));
        equineServiceUnderTest.assignEquineToCategory(1L, 1L);
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }

    @Test
    void willThrowEquineNotFoundExceptionWhenAssigningEquineToCategory() {
        given(equineRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToCategory(1L, 1L)
        );
        Assertions.assertEquals("No equine found with id: 1", exception.getMessage());
    }

    @Test
    void willThrowCategoryNotFoundExceptionWhenAssigningEquineToCategory() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(equineStatusRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToCategory(1L, 1L)
        );
        Assertions.assertEquals("No category found with id: 1", exception.getMessage());
    }

    @Test
    void willGetAllTrainingProgrammesOfAnEquine() {
        Equine testEquine = new Equine();
        TrainingProgramme trainingProgramme1 = new TrainingProgramme();
        TrainingProgramme trainingProgramme2 = new TrainingProgramme();

        trainingProgramme1.setId(1L);
        trainingProgramme1.setEquine(new Equine());
        trainingProgramme1.setStartDate(null);
        trainingProgramme1.setEndDate(null);
        trainingProgramme1.setSkillProgressRecords(new ArrayList<>());
        trainingProgramme1.setSkillTrainingSessions(new ArrayList<>());

        trainingProgramme2.setId(2L);
        trainingProgramme2.setEquine(new Equine());
        trainingProgramme2.setStartDate(null);
        trainingProgramme2.setEndDate(null);
        trainingProgramme2.setSkillProgressRecords(new ArrayList<>());
        trainingProgramme2.setSkillTrainingSessions(new ArrayList<>());

        testEquine.setId(1L);
        testEquine.setTrainingProgrammes(new ArrayList<>(
                List.of(trainingProgramme1, trainingProgramme2)
        ));

        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine));

        List<TrainingProgramme> equineTrainingProgrammes = equineServiceUnderTest
                .getEquineTrainingProgrammes(1L);
        Assertions.assertEquals(2, equineTrainingProgrammes.size());
        Assertions.assertEquals(1L, equineTrainingProgrammes.get(0).getId());
        Assertions.assertEquals(2L, equineTrainingProgrammes.get(1).getId());

    }

    @Test
    void willCreateNewHealthAndSafetyFlagOnEquine() {
        HealthAndSafetyFlag healthAndSafetyFlag = new HealthAndSafetyFlag(1L, "", null);
        given(equineRepository.findById(1L)).willReturn(Optional.ofNullable(equineInstance));
        given(healthAndSafetyFlagRepository.saveAndFlush(healthAndSafetyFlag)).willReturn(healthAndSafetyFlag);

        HealthAndSafetyFlag savedHealthAndSafetyFlag = equineServiceUnderTest.createEquineHealthAndSafetyFlag(1L, healthAndSafetyFlag);
        Assertions.assertEquals(savedHealthAndSafetyFlag, healthAndSafetyFlag);
        Assertions.assertNotNull(savedHealthAndSafetyFlag.getDateCreated());
        Assertions.assertEquals(savedHealthAndSafetyFlag.getEquine(), equineInstance);
    }

    @Test
    void willGetEquineHealthAndSafetyFlags() {
        HealthAndSafetyFlag healthAndSafetyFlag1 = new HealthAndSafetyFlag(1L, "", equineInstance);
        HealthAndSafetyFlag healthAndSafetyFlag2 = new HealthAndSafetyFlag(2L, "", equineInstance);
        equineInstance.setHealthAndSafetyFlags(new ArrayList<>(List.of(healthAndSafetyFlag1, healthAndSafetyFlag2)));
        given(equineRepository.findById(1L)).willReturn(Optional.ofNullable(equineInstance));

        List<HealthAndSafetyFlag> healthAndSafetyFlags = equineServiceUnderTest.getEquineHealthAndSafetyFlags(1L);
        Assertions.assertEquals(healthAndSafetyFlags, new ArrayList<>(List.of(healthAndSafetyFlag1, healthAndSafetyFlag2)));
    }
}