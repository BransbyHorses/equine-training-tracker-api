package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingMethodRepository extends JpaRepository<TrainingMethod, Long> {
}
