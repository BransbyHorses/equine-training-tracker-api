package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.repositories.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    private final String skillTestInstanceName = "Skill service can service skills";

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillServiceTest;
    private Skill skillTestInstance;

    @BeforeEach
    void setUp() {
        skillServiceTest = new SkillService(skillRepository);
        skillTestInstance = new Skill(1L, skillTestInstanceName, new HashSet<>());
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
    void canUpdateSkill() {
        String updatedName = "This skill's name has been updated";
        when(skillRepository.save(any(Skill.class))).thenReturn(skillTestInstance);
        Skill addedSkill = skillServiceTest.create(skillTestInstance);
        assertThat(addedSkill.getName()).isEqualTo(skillTestInstanceName);
        addedSkill.setName(updatedName);
        Skill updatedSkill = skillServiceTest.update(skillTestInstance, skillTestInstance.getId());
        assertThat(updatedSkill.getId()).isEqualTo(skillTestInstance.getId());
        assertThat(updatedSkill.getName()).isEqualTo(updatedName);

    }






}
