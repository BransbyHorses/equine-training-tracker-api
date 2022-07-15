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

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EquineServiceTest {

    @Mock
    EquineRepository equineRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    YardRepository yardRepository;
    @Mock
    SkillRepository skillRepository;
    @Mock
    ProgrammeRepository programmeRepository;
    @InjectMocks
    private EquineService equineServiceUnderTest;
    private Equine equineInstance;

    @BeforeEach
    void setUp() {
        equineServiceUnderTest = new EquineService(equineRepository, programmeRepository, yardRepository, categoryRepository, skillRepository);
        equineInstance = new Equine(1L, "First Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>());
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
    void deleteEquine() {
        equineServiceUnderTest.deleteEquine(equineInstance.getId());
        Mockito.verify(equineRepository).deleteById(equineInstance.getId());
    }

    @Test
    void throwsIfEquineNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> equineServiceUnderTest.updateEquine(equineInstance.getId(), equineInstance));
    }

    @Test
    void willAssignEquineToProgramme() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(programmeRepository.findById(1L)).willReturn(Optional.of(equineInstance.getProgramme()));
        equineServiceUnderTest.assignEquineToProgramme(1L, 1L);
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }

    @Test
    void willThrowEquineNotFoundExceptionWhenAssigningEquineToProgramme() {
        given(equineRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToProgramme(1L, 1L)
        );
        Assertions.assertEquals("No equine found with id: 1", exception.getMessage());
    }

    @Test
    void willThrowProgrammeNotFoundExceptionWhenAssigningEquineToProgramme() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(programmeRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToProgramme(1L, 1L)
        );
        Assertions.assertEquals("No programme found with id: 1", exception.getMessage());
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
        given(categoryRepository.findById(1L)).willReturn(Optional.of(equineInstance.getCategory()));
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
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineToCategory(1L, 1L)
        );
        Assertions.assertEquals("No category found with id: 1", exception.getMessage());
    }

    @Test
    void willAssignSkillToEquine() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(skillRepository.findById(1L)).willReturn(Optional.of(new Skill()));
        equineServiceUnderTest.assignEquineASkill(1L, 1L);
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }

    @Test
    void willThrowEquineNotFoundExceptionWhenAssigningSkillToEquine() {
        given(equineRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineASkill(1L, 1L)
        );
        Assertions.assertEquals("No equine found with id: 1", exception.getMessage());
    }

    @Test
    void willThrowSkillNotFoundExceptionWhenAssigningSkillToEquine() {
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        given(skillRepository.findById(1L)).willReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> equineServiceUnderTest.assignEquineASkill(1L, 1L)
        );
        Assertions.assertEquals("No skill found with id: 1", exception.getMessage());
    }

    @Test
    @Disabled
    void willThrowSkillExistsExceptionWhenAssigningSkillToEquine() {
    }

    @Test
    void willDeleteSkillFromEquine() {
        Skill removedSkill = new Skill(1L, "Skill 1", new HashSet<>());
        Set<Skill> skills =
                new HashSet<Skill>(
                        Arrays.asList(
                                removedSkill,
                                new Skill(2L, "Skill 2", new HashSet<>()),
                                new Skill(3L, "Skill 3", new HashSet<>()
                                )));
        equineInstance.setSkills(skills);
        given(equineRepository.findById(1L)).willReturn(Optional.of(equineInstance));
        equineServiceUnderTest.deleteEquineSkill(1L, 1L);
        Assertions.assertEquals(equineInstance.getSkills().size(), 2);
        Assertions.assertFalse(equineInstance.getSkills().contains(removedSkill));
        Mockito.verify(equineRepository).saveAndFlush(equineInstance);
    }
}