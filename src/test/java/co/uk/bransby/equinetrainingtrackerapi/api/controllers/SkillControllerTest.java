package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.controllers.SkillController;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.services.SkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SkillController.class)
@ActiveProfiles("test")
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    SkillService skillService;

    private List<Skill> skillList;

    private final String urlTemplate = "/data/skills/";

    @BeforeEach
    void setUp() {
        this.skillList = new ArrayList<>();
        this.skillList.add(new Skill(1L, "Accepts presence of humans at close proximity", List.of(new TrainingProgramme())));
        this.skillList.add(new Skill(2L, "Accepts touch", List.of(new TrainingProgramme())));
        this.skillList.add(new Skill(3L, "Will wear a head collar", List.of(new TrainingProgramme())));
        this.skillList.add(new Skill(4L, "Can be led", List.of(new TrainingProgramme())));
    }

    @Test
    void fetchesAllSkills() throws Exception {
        given(skillService.findAll()).willReturn(skillList);

        this.mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(skillList.size()));
    }

    @Test
    void fetchesSkillById() throws Exception {
        final Long skillId = 1L;
        Skill controlSkill = skillList.get(0);

        given(skillService.findById(skillId)).willReturn(controlSkill);

        this.mockMvc.perform(get(urlTemplate + "{id}", skillId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(controlSkill.getId()))
                .andExpect(jsonPath("$.name").value(controlSkill.getName()));
    }

    @Test
    void respondsWithNotFoundIfAskedToFetchNonExistentSkill() throws Exception {
       final long skillId = 5L;
       given(skillService.findById(skillId)).willThrow(new EntityNotFoundException("No skill found with id: 5"));
       this.mockMvc.perform(get(urlTemplate + "{id}", skillId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.errorMessage").value("No skill found with id: 5"));
    }

    @Test
    void savesSkillAndRespondsWithCreated() throws Exception {

        Skill newSkill = new Skill(1L, "Test Skill", List.of(new TrainingProgramme()));
        given(skillService.create(any(Skill.class))).willAnswer((answer) -> answer.getArgument(0));


        this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSkill)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newSkill.getName()));
    }

    @Test
    void updatesSkill() throws Exception {
        Long skillId = 1L;
        Skill updatedSkill = new Skill(skillId, "Equine can be transformed", List.of(new TrainingProgramme()));

        given(skillService.findById(skillId)).willReturn(updatedSkill);
        given(skillService.update(updatedSkill, updatedSkill.getId())).willReturn(updatedSkill);

        this.mockMvc.perform(put(urlTemplate + "{id}", updatedSkill.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSkill)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedSkill.getName()));
    }

    @Test
    void deletesSkill() throws Exception {
        Long skillId = 1L;
        Skill deletedSkill = skillList.get(0);
        given(skillService.findById(skillId)).willReturn(deletedSkill);
        doNothing().when(skillService).deleteById(deletedSkill.getId());
        this.mockMvc.perform(delete(urlTemplate + "{id}", deletedSkill.getId()))
                .andExpect(status().isOk());
    }

}
