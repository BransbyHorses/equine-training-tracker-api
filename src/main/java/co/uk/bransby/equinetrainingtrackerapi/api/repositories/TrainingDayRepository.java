package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingDayRepository extends JpaRepository<TrainingDay, Long> {
}
