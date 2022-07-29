package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.ProgrammeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ProgrammeServiceTest {

    @Mock
    ProgrammeRepository programmeRepository;
    @Mock
    EquineRepository equineRepository;
    @InjectMocks
    ProgrammeService programmeService;

    List<Programme> programmes;

    @BeforeEach
    void setUp() {
        this.programmeService = new ProgrammeService(programmeRepository, equineRepository);
        this.programmes = new ArrayList<>(List.of(
                new Programme(1L, "Programme 1", new HashSet<>()),
                new Programme(2L, "Programme 2", new HashSet<>()),
                new Programme(3L, "Programme 3", new HashSet<>()),
                new Programme(4L, "Programme 3", new HashSet<>()),
                new Programme(5L, "Programme 3", new HashSet<>())
        ));
    }

    @Test
    void canGetAllProgrammes() {
        given(programmeRepository.findAll()).willReturn(programmes);
        List<Programme> returnedProgrammes = programmeService.getAllProgrammes();
        assertEquals(programmes, returnedProgrammes);
    }

    @Test
    void canGetProgrammeById() {
        given(programmeRepository.findById(1L)).willReturn(Optional.ofNullable(programmes.get(0)));
        Programme programme = programmeService.getProgramme(1L);
        assertNotNull(programme);
        assertEquals(programmes.get(0).getName(), programme.getName());
    }

    @Test
    void canCreateProgramme() {
        given(programmeRepository.saveAndFlush(programmes.get(0))).willReturn(programmes.get(0));
        Programme programme = programmeService.createProgramme(programmes.get(0));
        assertEquals(programme, programmes.get(0));
    }

    @Test
    void canUpdateProgramme() {
        Programme updatedProgramme = new Programme(1L, "Programme 1 Updated", new HashSet<>());
        given(programmeRepository.findById(1L)).willReturn(Optional.ofNullable(programmes.get(0)));
        Programme programme = programmeService.updateProgramme(1L, updatedProgramme);
        assertEquals(updatedProgramme.getName(), programmes.get(0).getName());
    }

    @Test
    void canDeleteProgramme() {
        Equine equine = new Equine(1L, "Test Horse", new Yard(), new Category(), programmes.get(0), new HashSet<Skill>());
        programmes.get(0).setEquines(new HashSet<>(Set.of(equine)));
        given(programmeRepository.findById(1L)).willReturn(Optional.ofNullable(programmes.get(0)));
        programmeService.deleteProgramme(1L);
        assertNull(equine.getProgramme());
        Mockito.verify(programmeRepository).deleteById(1L);
    }
}