package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingProgrammeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TrainingProgrammeServiceTest {

    @Mock
    TrainingProgrammeRepository trainingProgrammeRepository;
    @Mock
    EquineRepository equineRepository;
    @Mock
    SkillRepository skillRepository;
    @InjectMocks
    TrainingProgrammeService trainingProgrammeService;

    List<TrainingProgramme> trainingProgrammes;

    @BeforeEach
    void setUp() {
        this.trainingProgrammeService = new TrainingProgrammeService(trainingProgrammeRepository, equineRepository, skillRepository);
        this.trainingProgrammes = new ArrayList<>(List.of(
                new TrainingProgramme(1L, "Programme 1", new Equine(), List.of(new Skill()), new Date(), new Date()),
                new TrainingProgramme(2L, "Programme 2", new Equine(), List.of(new Skill()), new Date(), new Date()),
                new TrainingProgramme(3L, "Programme 3", new Equine(), List.of(new Skill()), new Date(), new Date()),
                new TrainingProgramme(4L, "Programme 3", new Equine(), List.of(new Skill()), new Date(), new Date()),
                new TrainingProgramme(5L, "Programme 3", new Equine(), List.of(new Skill()), new Date(), new Date())
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
        assertEquals(trainingProgrammes.get(0).getName(), trainingProgramme.getName());
    }

    @Test
    void canCreateProgramme() {
        given(trainingProgrammeRepository.saveAndFlush(trainingProgrammes.get(0))).willReturn(trainingProgrammes.get(0));
        TrainingProgramme trainingProgramme = trainingProgrammeService.createProgramme(trainingProgrammes.get(0));
        assertEquals(trainingProgramme, trainingProgrammes.get(0));
    }

    @Test
    void canUpdateProgramme() {
        TrainingProgramme updatedTrainingProgramme = new TrainingProgramme(1L, "Programme 1 Updated", new Equine(), List.of(new Skill()), new Date(), new Date());
        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        TrainingProgramme trainingProgramme = trainingProgrammeService.updateProgramme(1L, updatedTrainingProgramme);
        assertEquals(updatedTrainingProgramme.getName(), trainingProgrammes.get(0).getName());
    }

    @Test
    void canAssignTrainingProgrammeToEquine() {
        TrainingProgramme trainingProgramme = new TrainingProgramme(1L, "Programme 1", new Equine(), List.of(new Skill()), new Date(), new Date());
        Equine equine = new Equine(1L, "Equine", new Yard(), new Category(), List.of());
        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.of(trainingProgramme));
        given(equineRepository.findById(1L)).willReturn(Optional.of(equine));
        TrainingProgramme updatedTrainingProgramme = trainingProgrammeService.assignTrainingProgrammeToEquine(1L, 1L);
        assertEquals(updatedTrainingProgramme.getEquine(), equine);
    }

    @Test
    void canAddSkillToTrainingProgramme() {
        TrainingProgramme trainingProgramme = new TrainingProgramme(1L, "Programme 1", new Equine(), new ArrayList<>(), new Date(), new Date());
        Skill skill = new Skill(1L, "Skill", new ArrayList<>());
        given(trainingProgrammeRepository.findById(1L)).willReturn(Optional.of(trainingProgramme));
        given(skillRepository.findById(1L)).willReturn(Optional.of(skill));
        TrainingProgramme updatedTrainingProgramme = trainingProgrammeService.addSkillToTrainingProgramme(1L, 1L);
        assertEquals(updatedTrainingProgramme.getSkills(), new ArrayList<>(List.of(skill)));
    }

}