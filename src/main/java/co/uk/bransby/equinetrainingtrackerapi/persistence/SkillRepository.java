package co.uk.bransby.equinetrainingtrackerapi.persistence;

import co.uk.bransby.equinetrainingtrackerapi.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Skill findByName(String name);

}
