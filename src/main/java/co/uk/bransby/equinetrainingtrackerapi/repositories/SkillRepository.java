package co.uk.bransby.equinetrainingtrackerapi.repositories;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Skill findByName(String name);

}
