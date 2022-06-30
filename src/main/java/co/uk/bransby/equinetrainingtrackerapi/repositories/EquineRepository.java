package co.uk.bransby.equinetrainingtrackerapi.repositories;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquineRepository extends JpaRepository<Equine, Long> {
    List<Equine> findAllByYard(Yard yard);
    List<Equine> findAllByProgramme(Programme programme);
    List<Equine> findAllByCategory(Category category);
}
