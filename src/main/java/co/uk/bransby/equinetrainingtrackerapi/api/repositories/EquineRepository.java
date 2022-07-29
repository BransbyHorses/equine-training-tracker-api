package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquineRepository extends JpaRepository<Equine, Long> {
}
