package co.uk.bransby.equinetrainingtrackerapi.api.repositories;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingCategoryRepository extends JpaRepository<TrainingCategory, Long> {
}
