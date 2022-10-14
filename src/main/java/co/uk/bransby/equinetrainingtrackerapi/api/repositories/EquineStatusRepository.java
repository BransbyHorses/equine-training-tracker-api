package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.EquineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquineStatusRepository extends JpaRepository<EquineStatus, Long> {
}
