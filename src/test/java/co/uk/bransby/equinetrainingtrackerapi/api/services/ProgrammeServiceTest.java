package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.ProgrammeRepository;
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
class ProgrammeServiceTest {

    @Mock
    ProgrammeRepository programmeRepository;
    @Mock
    EquineRepository equineRepository;
    @InjectMocks
    ProgrammeService programmeService;

    List<TrainingProgramme> trainingProgrammes;

    @BeforeEach
    void setUp() {
        this.programmeService = new ProgrammeService(programmeRepository, equineRepository);
        this.trainingProgrammes = new ArrayList<>(List.of(
                new TrainingProgramme(1L, "Programme 1", new HashSet<>()),
                new TrainingProgramme(2L, "Programme 2", new HashSet<>()),
                new TrainingProgramme(3L, "Programme 3", new HashSet<>()),
                new TrainingProgramme(4L, "Programme 3", new HashSet<>()),
                new TrainingProgramme(5L, "Programme 3", new HashSet<>())
        ));
    }

    @Test
    void canGetAllProgrammes() {
        given(programmeRepository.findAll()).willReturn(trainingProgrammes);
        List<TrainingProgramme> returnedTrainingProgrammes = programmeService.getAllProgrammes();
        assertEquals(trainingProgrammes, returnedTrainingProgrammes);
    }

    @Test
    void canGetProgrammeById() {
        given(programmeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        TrainingProgramme trainingProgramme = programmeService.getProgramme(1L);
        assertNotNull(trainingProgramme);
        assertEquals(trainingProgrammes.get(0).getName(), trainingProgramme.getName());
    }

    @Test
    void canCreateProgramme() {
        given(programmeRepository.saveAndFlush(trainingProgrammes.get(0))).willReturn(trainingProgrammes.get(0));
        TrainingProgramme trainingProgramme = programmeService.createProgramme(trainingProgrammes.get(0));
        assertEquals(trainingProgramme, trainingProgrammes.get(0));
    }

    @Test
    void canUpdateProgramme() {
        TrainingProgramme updatedTrainingProgramme = new TrainingProgramme(1L, "Programme 1 Updated", new HashSet<>());
        given(programmeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        TrainingProgramme trainingProgramme = programmeService.updateProgramme(1L, updatedTrainingProgramme);
        assertEquals(updatedTrainingProgramme.getName(), trainingProgrammes.get(0).getName());
    }

    @Test
    void canDeleteProgramme() {
        Equine equine = new Equine(1L, "Test Horse", new Yard(), new Category(), trainingProgrammes.get(0), new HashSet<Skill>());
        trainingProgrammes.get(0).setEquines(new HashSet<>(Set.of(equine)));
        given(programmeRepository.findById(1L)).willReturn(Optional.ofNullable(trainingProgrammes.get(0)));
        programmeService.deleteProgramme(1L);
        assertNull(equine.getTrainingProgramme());
        Mockito.verify(programmeRepository).deleteById(1L);
    }
}