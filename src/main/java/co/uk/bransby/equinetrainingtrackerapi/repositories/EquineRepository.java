package co.uk.bransby.equinetrainingtrackerapi.repositories;

import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquineRepository extends JpaRepository<Equine, Long> {
}
