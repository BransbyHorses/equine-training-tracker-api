package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.EquineStatus;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineStatusRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class EquineStatusService {

    private final EquineStatusRepository equineStatusRepository;
    private final EquineRepository equineRepository;

    public List<EquineStatus> getCategories() {
        return equineStatusRepository.findAll();
    }

    public EquineStatus getCategory(Long id) {
        return equineStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
    }

    public EquineStatus createCategory(EquineStatus category) {
        return equineStatusRepository.saveAndFlush(category);
    }

    public EquineStatus updateCategory(Long id, EquineStatus updatedCategoryValues) {
        EquineStatus categoryToUpdate = equineStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
        BeanUtils.copyProperties(updatedCategoryValues, categoryToUpdate, "id");
        return equineStatusRepository.saveAndFlush(categoryToUpdate);
    }

    public void deleteCategory(Long id) {
        // TODO - handle delete equineStatus
    }
}

