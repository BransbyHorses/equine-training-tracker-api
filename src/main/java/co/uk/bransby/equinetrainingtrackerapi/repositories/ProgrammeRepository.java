package co.uk.bransby.equinetrainingtrackerapi.repositories;

import co.uk.bransby.equinetrainingtrackerapi.models.Programme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammeRepository extends JpaRepository<Programme, Long> {
}
