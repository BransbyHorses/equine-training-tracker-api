package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EquineServiceTest {

    @Mock
    EquineRepository equineRepository;
    @Mock
    YardRepository yardRepository;
    @Mock
    HealthAndSafetyFlagRepository healthAndSafetyFlagRepository;
    @Mock
    DisruptionRepository disruptionRepository;
    @InjectMocks
    private EquineService equineServiceUnderTest;
    private Equine equineInstance;

    @BeforeEach
    void setUp() {
        equineServiceUnderTest = new EquineService(equineRepository, yardRepository, healthAndSafetyFlagRepository, disruptionRepository);
        equineInstance = new Equine(1L, "First Horse", new Yard(), EquineStatus.AWAITING_TRAINING, new ArrayList<>(), new LearnerType(), new ArrayList<>(), new ArrayList<>());
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
    void willAssignEquineAwaitingTrainingStatus() {
        Equine equine = new Equine();
        equine.setId(1L);
        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        when(equineRepository.saveAndFlush(equine))
                .thenAnswer(i -> i.getArguments()[0]);
        Equine equineAwaitingTraining = equineServiceUnderTest.assignEquineAStatus(1L, 1L);
        Assertions.assertEquals(EquineStatus.AWAITING_TRAINING, equineAwaitingTraining.getEquineStatus());
    }

    @Test
    void willAssignEquineInTrainingStatus() {
        Equine equine = new Equine();
        equine.setId(1L);
        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        when(equineRepository.saveAndFlush(equine))
                .thenAnswer(i -> i.getArguments()[0]);
        Equine equineAwaitingTraining = equineServiceUnderTest.assignEquineAStatus(1L, 2L);
        Assertions.assertEquals(EquineStatus.IN_TRAINING, equineAwaitingTraining.getEquineStatus());
    }
    
    @Test
    void willThrowEquineNotFoundExceptionWhenAssigningEquineToYard() {
        given(equineRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToYard(1L, 1L)
        );
        assertEquals("No equine found with id: 1", exception.getMessage());
    }

    @Test
    void willThrowYardNotFoundExceptionWhenAssigningEquineToYard() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(yardRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToYard(1L, 1L)
        );
        assertEquals("No yard found with id: 1", exception.getMessage());
    }

    @Test
    void willAssignEquineReturnedStatusAndEndActiveTrainingProgramme() {
        TrainingProgramme activeTrainingProgramme = new TrainingProgramme();
        activeTrainingProgramme.setStartDate(LocalDateTime.of(2022, 10, 15, 7, 30));

        Equine equine = new Equine();
        equine.setId(1L);
        equine.setTrainingProgrammes(new ArrayList<>(List.of(activeTrainingProgramme)));

        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        when(equineRepository.saveAndFlush(equine))
                .thenAnswer(i -> i.getArguments()[0]);
        Equine equineAwaitingTraining = equineServiceUnderTest.assignEquineAStatus(1L, 3L);
        Assertions.assertEquals(EquineStatus.RETURNED_TO_OWNER, equineAwaitingTraining.getEquineStatus());
        Assertions.assertNotNull(equine.getTrainingProgrammes().get(0).getEndDate());
    }

    @Test
    void willAssignEquineRehomedStatusAndEndActiveTrainingProgramme() {
        TrainingProgramme activeTrainingProgramme = new TrainingProgramme();
        activeTrainingProgramme.setStartDate(LocalDateTime.of(2022, 10, 15, 7, 30));

        Equine equine = new Equine();
        equine.setId(1L);
        equine.setTrainingProgrammes(new ArrayList<>(List.of(activeTrainingProgramme)));

        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        when(equineRepository.saveAndFlush(equine))
                .thenAnswer(i -> i.getArguments()[0]);
        Equine equineAwaitingTraining = equineServiceUnderTest.assignEquineAStatus(1L, 4L);
        Assertions.assertEquals(EquineStatus.REHOMED, equineAwaitingTraining.getEquineStatus());
        Assertions.assertNotNull(equine.getTrainingProgrammes().get(0).getEndDate());
    }

    @Test
    void willAssignEquineEuthanisedStatusAndEndActiveTrainingProgramme() {
        TrainingProgramme activeTrainingProgramme = new TrainingProgramme();
        activeTrainingProgramme.setStartDate(LocalDateTime.of(2022, 10, 15, 7, 30));

        Equine equine = new Equine();
        equine.setId(1L);
        equine.setTrainingProgrammes(new ArrayList<>(List.of(activeTrainingProgramme)));

        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        when(equineRepository.saveAndFlush(equine))
                .thenAnswer(i -> i.getArguments()[0]);
        Equine equineAwaitingTraining = equineServiceUnderTest.assignEquineAStatus(1L, 5L);
        Assertions.assertEquals(EquineStatus.EUTHANISED, equineAwaitingTraining.getEquineStatus());
        Assertions.assertNotNull(equine.getTrainingProgrammes().get(0).getEndDate());
    }

    @Test
    void willAssignEquineOtherStatusAndEndActiveTrainingProgramme() {
        TrainingProgramme activeTrainingProgramme = new TrainingProgramme();
        activeTrainingProgramme.setStartDate(LocalDateTime.of(2022, 10, 15, 7, 30));

        Equine equine = new Equine();
        equine.setId(1L);
        equine.setTrainingProgrammes(new ArrayList<>(List.of(activeTrainingProgramme)));

        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        when(equineRepository.saveAndFlush(equine))
                .thenAnswer(i -> i.getArguments()[0]);
        Equine equineAwaitingTraining = equineServiceUnderTest.assignEquineAStatus(1L, 6L);
        Assertions.assertEquals(EquineStatus.OTHER, equineAwaitingTraining.getEquineStatus());
        Assertions.assertNotNull(equine.getTrainingProgrammes().get(0).getEndDate());
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
        assertEquals(2, equineTrainingProgrammes.size());
        assertEquals(1L, equineTrainingProgrammes.get(0).getId());
        assertEquals(2L, equineTrainingProgrammes.get(1).getId());

    }

    @Test
    void willCreateNewHealthAndSafetyFlagOnEquine() {
        HealthAndSafetyFlag healthAndSafetyFlag = new HealthAndSafetyFlag(1L, "", null);
        given(equineRepository.findById(1L)).willReturn(Optional.ofNullable(equineInstance));
        given(healthAndSafetyFlagRepository.saveAndFlush(healthAndSafetyFlag)).willReturn(healthAndSafetyFlag);

        HealthAndSafetyFlag savedHealthAndSafetyFlag = equineServiceUnderTest.createEquineHealthAndSafetyFlag(1L, healthAndSafetyFlag);
        assertEquals(savedHealthAndSafetyFlag, healthAndSafetyFlag);
        Assertions.assertNotNull(savedHealthAndSafetyFlag.getDateCreated());
        assertEquals(savedHealthAndSafetyFlag.getEquine(), equineInstance);
    }

    @Test
    void willGetEquineHealthAndSafetyFlags() {
        HealthAndSafetyFlag healthAndSafetyFlag1 = new HealthAndSafetyFlag(1L, "", equineInstance);
        HealthAndSafetyFlag healthAndSafetyFlag2 = new HealthAndSafetyFlag(2L, "", equineInstance);
        equineInstance.setHealthAndSafetyFlags(new ArrayList<>(List.of(healthAndSafetyFlag1, healthAndSafetyFlag2)));
        given(equineRepository.findById(1L)).willReturn(Optional.ofNullable(equineInstance));

        List<HealthAndSafetyFlag> healthAndSafetyFlags = equineServiceUnderTest.getEquineHealthAndSafetyFlags(1L);
        assertEquals(healthAndSafetyFlags, new ArrayList<>(List.of(healthAndSafetyFlag1, healthAndSafetyFlag2)));
    }

    @Test
    void willGetActiveTrainingProgramme() {
        TrainingProgramme activeTrainingProgramme1 = new TrainingProgramme();
        activeTrainingProgramme1.setStartDate(LocalDateTime.of(2022,10,20,7,30));
        activeTrainingProgramme1.setEndDate(null);

        TrainingProgramme endedTrainingProgramme1 = new TrainingProgramme();
        endedTrainingProgramme1.setStartDate(LocalDateTime.of(2022,9,20,7,30));
        endedTrainingProgramme1.setEndDate(LocalDateTime.of(2022,9,25,7,30));

        TrainingProgramme endedTrainingProgramme2 = new TrainingProgramme();
        endedTrainingProgramme2.setStartDate(LocalDateTime.of(2022,8,20,7,30));
        endedTrainingProgramme2.setEndDate(LocalDateTime.of(2022,8,25,7,30));

        Equine testEquine = new Equine();
        testEquine.setId(1L);
        testEquine.setTrainingProgrammes(new ArrayList<>(List.of(endedTrainingProgramme1, endedTrainingProgramme2, activeTrainingProgramme1)));
        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine));

        TrainingProgramme result = equineServiceUnderTest.getActiveTrainingProgramme(1L);
        assertEquals(result, activeTrainingProgramme1);
    }

    @Test
    void willReturnNullIfEquineHasNoActiveTrainingProgramme () {
        TrainingProgramme endedTrainingProgramme1 = new TrainingProgramme();
        endedTrainingProgramme1.setStartDate(LocalDateTime.of(2022,10,20,7,30));
        endedTrainingProgramme1.setEndDate(LocalDateTime.of(2022,12,20,7,30));
        Equine testEquine1 = new Equine();
        testEquine1.setId(1L);
        testEquine1.setTrainingProgrammes(new ArrayList<>(List.of(endedTrainingProgramme1)));

        Equine testEquine2 = new Equine();
        testEquine1.setId(2L);
        testEquine1.setTrainingProgrammes(new ArrayList<>());

        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine1));
        given(equineRepository.findById(2L)).willReturn(Optional.of(testEquine2));

        TrainingProgramme result1 = equineServiceUnderTest.getActiveTrainingProgramme(1L);
        TrainingProgramme result2 = equineServiceUnderTest.getActiveTrainingProgramme(2L);
        Assertions.assertNull(result1);
        Assertions.assertNull(result2);
    }

    @Test
    void willGetAllEquineSkillTrainingSessions() {
        TrainingProgramme trainingProgramme1 = new TrainingProgramme();
        TrainingProgramme trainingProgramme2 = new TrainingProgramme();
        TrainingProgramme trainingProgramme3 = new TrainingProgramme();

        SkillTrainingSession skillTrainingSession1 = new SkillTrainingSession();
        SkillTrainingSession skillTrainingSession2 = new SkillTrainingSession();
        SkillTrainingSession skillTrainingSession3 = new SkillTrainingSession();

        trainingProgramme1.setSkillTrainingSessions(new ArrayList<>(List.of(skillTrainingSession1)));
        trainingProgramme2.setSkillTrainingSessions((new ArrayList<>(List.of(skillTrainingSession2))));
        trainingProgramme3.setSkillTrainingSessions((new ArrayList<>(List.of(skillTrainingSession3))));

        Equine testEquine = new Equine();
        testEquine.setTrainingProgrammes(new ArrayList<>(List.of(trainingProgramme1, trainingProgramme2, trainingProgramme3)));
        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine));
        List<SkillTrainingSession> skillTrainingSessions = equineServiceUnderTest.getEquineSkillTrainingSessions(1L);
        assertEquals(skillTrainingSessions, new ArrayList<>(List.of(skillTrainingSession1, skillTrainingSession2, skillTrainingSession3)));
    }

    @Test
    void willReturnEmptyListIfNoSkillTrainingSessionsAreFound() {
        TrainingProgramme trainingProgramme1 = new TrainingProgramme();
        TrainingProgramme trainingProgramme2 = new TrainingProgramme();
        TrainingProgramme trainingProgramme3 = new TrainingProgramme();
        Equine testEquine = new Equine();
        testEquine.setTrainingProgrammes(new ArrayList<>(List.of(trainingProgramme1, trainingProgramme2, trainingProgramme3)));
        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine));
        List<SkillTrainingSession> skillTrainingSessions = equineServiceUnderTest.getEquineSkillTrainingSessions(1L);
        assertEquals(skillTrainingSessions.size(), 0);
    }

    @Test
    void willReturnEmptyListIfEquineHasNoTrainingProgrammes() {
        Equine testEquine = new Equine();
        testEquine.setTrainingProgrammes(null);
        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine));
        List<SkillTrainingSession> skillTrainingSessions = equineServiceUnderTest.getEquineSkillTrainingSessions(1L);
        assertEquals(skillTrainingSessions.size(), 0);
    }

    @Test
    void willLogNewDisruption() {
        Equine testEquine = new Equine();
        testEquine.setId(1L);
        given(equineRepository.findById(1L)).willReturn(Optional.of(testEquine));

        when(disruptionRepository.saveAndFlush(Mockito.any(Disruption.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Disruption newDisruption1 = equineServiceUnderTest.logNewDisruption(1, 1L);
        Disruption newDisruption2 = equineServiceUnderTest.logNewDisruption(2, 1L);
        Disruption newDisruption3 = equineServiceUnderTest.logNewDisruption(3, 1L);
        Disruption newDisruption4 = equineServiceUnderTest.logNewDisruption(4, 1L);
        Disruption newDisruption5 = equineServiceUnderTest.logNewDisruption(5, 1L);

        assertEquals(testEquine, newDisruption1.getEquine());
        assertEquals(DisruptionCode.VETERINARY_REVIEW, newDisruption1.getReason());
        Assertions.assertNotNull(newDisruption1.getStartDate());
        Assertions.assertNull(newDisruption1.getEndDate());

        assertEquals(DisruptionCode.TEAM_LOW, newDisruption2.getReason());
        assertEquals(DisruptionCode.WEATHER, newDisruption3.getReason());
        assertEquals(DisruptionCode.YARD_BUSY, newDisruption4.getReason());
        assertEquals(DisruptionCode.EQUINE_WELLBEING, newDisruption5.getReason());
    }

    @Test
    void willEndVetReviewDisruption() {
        Disruption disruption = new Disruption();
        disruption.setId(1L);
        disruption.setReason(DisruptionCode.VETERINARY_REVIEW);

        given(disruptionRepository.findById(1L)).willReturn(Optional.of(disruption));

        when(disruptionRepository.saveAndFlush(Mockito.any(Disruption.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Disruption endedDisruption = equineServiceUnderTest.endDisruption(1L, 1);

        assertEquals(DisruptionCode.VETERINARY_REVIEW, endedDisruption.getReason());
        assertNotNull(endedDisruption.getEndDate());
    }

    @Test
    void willEndTeamLowDisruption() {
        Disruption disruption = new Disruption();
        disruption.setId(2L);
        disruption.setReason(DisruptionCode.TEAM_LOW);

        given(disruptionRepository.findById(2L)).willReturn(Optional.of(disruption));

        when(disruptionRepository.saveAndFlush(Mockito.any(Disruption.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Disruption endedDisruption = equineServiceUnderTest.endDisruption(1L, 2);

        assertEquals(DisruptionCode.TEAM_LOW, endedDisruption.getReason());
        assertNotNull(endedDisruption.getEndDate());
    }

    @Test
    void willEndWeatherDisruption() {
        Disruption disruption = new Disruption();
        disruption.setId(2L);
        disruption.setReason(DisruptionCode.WEATHER);

        given(disruptionRepository.findById(2L)).willReturn(Optional.of(disruption));

        when(disruptionRepository.saveAndFlush(Mockito.any(Disruption.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Disruption endedDisruption = equineServiceUnderTest.endDisruption(1L, 2);

        assertEquals(DisruptionCode.WEATHER, endedDisruption.getReason());
        assertNotNull(endedDisruption.getEndDate());
    }

    @Test
    void willEndYardBusyDisruption() {
        Disruption disruption = new Disruption();
        disruption.setId(2L);
        disruption.setReason(DisruptionCode.YARD_BUSY);

        given(disruptionRepository.findById(2L)).willReturn(Optional.of(disruption));

        when(disruptionRepository.saveAndFlush(Mockito.any(Disruption.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Disruption endedDisruption = equineServiceUnderTest.endDisruption(1L, 2);

        assertEquals(DisruptionCode.YARD_BUSY, endedDisruption.getReason());
        assertNotNull(endedDisruption.getEndDate());
    }

    @Test
    void willEndEquineWellBeingDisruption() {
        Disruption disruption = new Disruption();
        disruption.setId(2L);
        disruption.setReason(DisruptionCode.EQUINE_WELLBEING);

        given(disruptionRepository.findById(2L)).willReturn(Optional.of(disruption));

        when(disruptionRepository.saveAndFlush(Mockito.any(Disruption.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        Disruption endedDisruption = equineServiceUnderTest.endDisruption(1L, 2);

        assertEquals(DisruptionCode.EQUINE_WELLBEING, endedDisruption.getReason());
        assertNotNull(endedDisruption.getEndDate());
    }
}