package co.uk.bransby.equinetrainingtrackerapi.persistence;

import co.uk.bransby.equinetrainingtrackerapi.controllers.SkillController;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.services.SkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(controllers = SkillController.class)
@ActiveProfiles("test")
public class SkilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    SkillService skillService;

    private List<Skill> skillList;

    private String urlTemplate = "/data/skills/";

    @BeforeEach
    void setUp() {
        this.skillList = new ArrayList<>();
        this.skillList.add(new Skill(1L, "Accepts presence of humans at close proximity"));
        this.skillList.add(new Skill(2L, "Accepts touch"));
        this.skillList.add(new Skill(3L, "Will wear a head collar"));
        this.skillList.add(new Skill(4L, "Can be led"));
    }

    @Test
    public void fetchesAllSkills() throws Exception {
        given(skillService.findAll()).willReturn(skillList);

        this.mockMvc.perform(get(urlTemplate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(skillList.size()));
    }

    @Test
    public void fetchesSkillById() throws Exception {
        final Long skillId = 1L;
        Skill controlSkill = skillList.get(0);

        given(skillService.findById(skillId)).willReturn(Optional.of(controlSkill));

        this.mockMvc.perform(get(urlTemplate + "{id}", skillId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(controlSkill.getId()))
                .andExpect(jsonPath("$.name").value(controlSkill.getName()));
    }

    @Test
    public void respondsWithNotFoundIfAskedToFetchNonExistentSkill() throws Exception {
       final long skillId = 5L;

       given(skillService.findById(skillId)).willReturn(Optional.empty());

       this.mockMvc.perform(get(urlTemplate + "{id}", skillId))
               .andExpect(status().isNotFound());
    }

    @Test
    public void savesSkillAndRespondsWithCreated() throws Exception {

        Skill newSkill = new Skill("Equine can be saved");
        given(skillService.create(any(Skill.class))).willAnswer((answer) -> answer.getArgument(0));


        this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSkill)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newSkill.getName()));
    }

    @Test
    public void updatesSkill() throws Exception {
        Long skillId = 1L;
        Skill updatedSkill = new Skill(skillId, "Equine can be transformed");

        given(skillService.findById(skillId)).willReturn(Optional.of(updatedSkill));
        given(skillService.update(updatedSkill, updatedSkill.getId())).willReturn(updatedSkill);

        this.mockMvc.perform(put(urlTemplate + "{id}", updatedSkill.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSkill)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedSkill.getName()));
    }




}
