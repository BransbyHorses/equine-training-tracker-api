package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillProgressRecordRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingProgrammeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;
    @Mock
    private TrainingProgrammeRepository trainingProgrammeRepository;
    @Mock
    private SkillProgressRecordRepository skillProgressRecordRepository;


    @InjectMocks
    private SkillService skillServiceTest;
    private Skill skillTestInstance;

    @BeforeEach
    void setUp() {
        skillServiceTest = new SkillService(skillRepository, trainingProgrammeRepository, skillProgressRecordRepository);
        skillTestInstance = new Skill(1L, "Skill service can service skills");
    }

    @Test
    void canFindAllSkills() {
        skillServiceTest.findAll();
        Mockito.verify(skillRepository).findAll();
    }

    @Test
    void canSaveASkill() {
        given(skillRepository.save(skillTestInstance)).willReturn(skillTestInstance);
        Skill savedSkill = skillServiceTest.create(skillTestInstance);
        assertThat(savedSkill).isNotNull();
    }

    @Test
    void willThrowEntityExistsException() {
        given(skillRepository.findByName(skillTestInstance.getName())).willReturn(skillTestInstance);
        Exception exception = Assertions.assertThrows(
                EntityExistsException.class,
                () -> skillServiceTest.create(skillTestInstance)
        );
        assertEquals(skillTestInstance.getName() + " already exists", exception.getMessage());
    }

    @Test
    void canUpdateSkill() {
        String updatedName = "This skill's name has been updated";
        when(skillRepository.save(any(Skill.class))).thenReturn(skillTestInstance);
        Skill addedSkill = skillServiceTest.create(skillTestInstance);
        assertThat(addedSkill.getName()).isEqualTo(skillTestInstance.getName());
        addedSkill.setName(updatedName);
        Skill updatedSkill = skillServiceTest.update(skillTestInstance, skillTestInstance.getId());
        assertThat(updatedSkill.getId()).isEqualTo(skillTestInstance.getId());
        assertThat(updatedSkill.getName()).isEqualTo(updatedName);
    }

    @Test
    void canFindSkillById() {
        given(skillRepository.findById(1L)).willReturn(Optional.of(skillTestInstance));
        Skill skill = skillServiceTest.findById(1L);
        assertThat(skill.getId()).isEqualTo(skillTestInstance.getId());
        Mockito.verify(skillRepository).findById(1L);
    }

    @Test
    void willAddNewSkillToTrainingProgrammes() {
        Skill newSkill = new Skill(1L, "New Skill");
        TrainingProgramme trainingProgramme = new TrainingProgramme();
        trainingProgramme.setSkillProgressRecords(new ArrayList<>());

        given(trainingProgrammeRepository.findAll()).willReturn(new ArrayList<>(List.of(trainingProgramme)));

        skillServiceTest.addNewSkillToTrainingProgrammes(newSkill);

        assertEquals(1, trainingProgramme.getSkillProgressRecords().size());
        assertEquals(newSkill, trainingProgramme.getSkillProgressRecords().get(0).getSkill());
        assertEquals(0, trainingProgramme.getSkillProgressRecords().get(0).getTime());
        assertEquals(ProgressCode.NOT_ABLE, trainingProgramme.getSkillProgressRecords().get(0).getProgressCode());
        assertNull(trainingProgramme.getSkillProgressRecords().get(0).getStartDate());
        assertNull(trainingProgramme.getSkillProgressRecords().get(0).getEndDate());
        assertEquals(trainingProgramme, trainingProgramme.getSkillProgressRecords().get(0).getTrainingProgramme());
    }

}
