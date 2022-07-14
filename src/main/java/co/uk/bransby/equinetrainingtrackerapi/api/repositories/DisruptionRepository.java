package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Disruption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisruptionRepository extends JpaRepository<Disruption, Long> {
}
