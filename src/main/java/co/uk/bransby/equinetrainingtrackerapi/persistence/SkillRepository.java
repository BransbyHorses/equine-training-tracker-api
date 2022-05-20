package co.uk.bransby.equinetrainingtrackerapi.persistence;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
