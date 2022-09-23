package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingProgrammeServiceTest {

    @Mock
    TrainingProgrammeRepository trainingProgrammeRepository;
    @Mock
    SkillRepository skillRepository;
    @Mock
    SkillTrainingSessionRepository skillTrainingSessionRepository;
    @Mock
    SkillProgressRecordRepository skillProgressRecordRepository;
    @InjectMocks
    TrainingProgrammeService trainingProgrammeService;

    List<TrainingProgramme> trainingProgrammes;

    @BeforeEach
    void setUp() {
        this.trainingProgrammeService = new TrainingProgrammeService(trainingProgrammeRepository, skillRepository, skillTrainingSessionRepository, skillProgressRecordRepository);
        this.trainingProgrammes = new ArrayList<>(List.of(
                new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(2L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(3L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(4L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()),
                new TrainingProgramme(5L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now())
        ));
    }

    @Test
    void canGetAllProgrammes() {
        given(trainingProgrammeRepository.findAll()).willReturn(trainingProgrammes);
        List<TrainingProgramme> returnedTrainingProgrammes = trainingProgrammeService.getAllProgrammes();
        assertEquals(trainingProgrammes, returnedTrainingProgrammes);
    }

    @Test
    void canGetProgrammeById() {
        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        TrainingProgramme trainingProgramme = trainingProgrammeService.getProgramme(1L);
        assertNotNull(trainingProgramme);
        assertEquals(trainingProgrammes.get(0).getId(), trainingProgramme.getId());
    }

    @Test
    void canCreateProgramme() {
        given(trainingProgrammeRepository.saveAndFlush(trainingProgrammes.get(0))).willReturn(trainingProgrammes.get(0));
        TrainingProgramme trainingProgramme = trainingProgrammeService.createProgramme(trainingProgrammes.get(0));
        assertEquals(trainingProgramme, trainingProgrammes.get(0));
    }

    @Test
    void canUpdateProgramme() {
        TrainingProgramme updatedTrainingProgramme = new TrainingProgramme(1L, new TrainingCategory(), new Equine(), new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        TrainingProgramme trainingProgramme = trainingProgrammeService.updateProgramme(1L, updatedTrainingProgramme);
        // TODO - assert entity has updated
    }

}