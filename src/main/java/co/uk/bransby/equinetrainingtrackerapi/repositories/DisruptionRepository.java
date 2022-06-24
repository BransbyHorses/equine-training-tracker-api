package co.uk.bransby.equinetrainingtrackerapi.repositories;

import co.uk.bransby.equinetrainingtrackerapi.models.Disruption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisruptionRepository extends JpaRepository<Disruption, Long> {
}
