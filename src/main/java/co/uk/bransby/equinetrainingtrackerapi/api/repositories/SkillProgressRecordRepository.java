package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.SkillProgressRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillProgressRecordRepository extends JpaRepository<SkillProgressRecord, Long> {
}
