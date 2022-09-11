package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
}
