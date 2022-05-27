package co.uk.bransby.equinetrainingtrackerapi.repositories;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YardRepository extends JpaRepository<Yard, Long> {
}
