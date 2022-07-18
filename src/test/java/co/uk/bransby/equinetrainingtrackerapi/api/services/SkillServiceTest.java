package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private EquineRepository equineRepository;

    @InjectMocks
    private SkillService skillServiceTest;
    private Skill skillTestInstance;

    @BeforeEach
    void setUp() {
        skillServiceTest = new SkillService(skillRepository, equineRepository);
        skillTestInstance = new Skill(1L, "Skill service can service skills", new HashSet<>());
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
        Assertions.assertEquals(skillTestInstance.getName() + " already exists", exception.getMessage());
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
        Optional<Skill> skill = skillServiceTest.findById(1L);
        assertThat(skill.get().getId()).isEqualTo(skillTestInstance.getId());
        Mockito.verify(skillRepository).findById(1L);
    }

    @Test
    void canDeleteSkillById() {
        // given
        Equine equine = new Equine(1L, "First Horse", new Yard(), new Category(), new Programme(), new HashSet<Skill>(List.of(skillTestInstance)));
        Set<Equine> equines = new HashSet<>(List.of(equine));
        skillTestInstance.setEquines(equines);
        given(skillRepository.findById(1L)).willReturn(Optional.of(skillTestInstance));
        given(equineRepository.getById(1L)).willReturn(equine);
        // when
        skillServiceTest.deleteById(1L);
        // then
        assertThat(skillTestInstance.getEquines()).hasSize(0);
        assertThat(equine.getSkills()).hasSize(0);
    }


}
