package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.LearnerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerTypeRepository extends JpaRepository<LearnerType, Long> {
}
