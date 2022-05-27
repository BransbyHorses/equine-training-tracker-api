package co.uk.bransby.equinetrainingtrackerapi.persistence;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class SkillRepositoryTest {

    @Autowired
    SkillRepository skillRepository;

    @Test
    public void itSavesASkillToTheTable() {
        Skill skill = new Skill();
        skill.setName("Can be added to a database");
        assertNull(skill.getId());
        skillRepository.save(skill);
        assertNotNull(skill.getId());
    }

}
