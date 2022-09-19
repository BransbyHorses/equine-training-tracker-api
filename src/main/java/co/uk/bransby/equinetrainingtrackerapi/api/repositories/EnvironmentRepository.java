package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentRepository extends JpaRepository<TrainingEnvironment, Long> {
}
