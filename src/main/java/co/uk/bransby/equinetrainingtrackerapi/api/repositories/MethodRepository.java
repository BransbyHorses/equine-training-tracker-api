package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Method;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MethodRepository extends JpaRepository<Method, Long> {
}
