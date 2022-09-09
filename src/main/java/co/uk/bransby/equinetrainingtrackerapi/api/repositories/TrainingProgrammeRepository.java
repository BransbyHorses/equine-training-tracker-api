package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgrammeRepository extends JpaRepository<TrainingProgramme, Long> {
}
