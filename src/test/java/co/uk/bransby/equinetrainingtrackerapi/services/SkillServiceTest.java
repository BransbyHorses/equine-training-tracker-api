package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.controllers.SkillController;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.repositories.SkillRepository;
import co.uk.bransby.equinetrainingtrackerapi.services.SkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;


    @Test
    void savesSkill() {

        Skill skill = new Skill("Horse can be saved");

     //   given(skillRepository.findById(skill.getId())).willReturn(Optional.empty());

        given(skillRepository.save(skill)).willReturn(skill);

        Skill savedSkill = skillService.create(skill);

        assertThat(savedSkill).isNotNull();


    }

    void findsAllSkills() {
        Skill skillOne = new Skill("A skill can be found");
        Skill skillTwo = new Skill("All skills can be found");

        given(skillRepository.findAll()).willReturn(List.of(skillOne, skillTwo));

        List<Skill> skillsList = skillRepository.findAll();

        assertThat(skillsList).isNotNull();
        assertThat(skillsList).hasSize(2);
    }



}
