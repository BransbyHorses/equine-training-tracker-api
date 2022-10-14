package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.SkillTrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillTrainingSessionRepository extends JpaRepository<SkillTrainingSession, Long> {
}
