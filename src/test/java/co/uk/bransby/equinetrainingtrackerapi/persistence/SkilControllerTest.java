package co.uk.bransby.equinetrainingtrackerapi.persistence;

import co.uk.bransby.equinetrainingtrackerapi.controllers.SkillController;
import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@WebMvcTest(controllers = SkillController.class)
@ActiveProfiles("test")
public class SkilControllerTest {

    @MockBean
    SkillRepository skillRepository;

    @Test
    public void itSavesASkillToTheTable() {
        Skill skill = new Skill();
        skill.setName("Can be added to a database");
        assertNull(skill.getId());
        skill = skillRepository.save(skill);
        System.out.println("SKILL ID");
        System.out.println(skill.getId());
    }

}
