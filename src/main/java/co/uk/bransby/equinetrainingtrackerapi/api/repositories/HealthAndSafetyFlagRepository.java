package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.HealthAndSafetyFlag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthAndSafetyFlagRepository extends JpaRepository<HealthAndSafetyFlag, Long> {
}
