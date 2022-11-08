package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingCategory;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class TrainingCategoryService {

    private final TrainingCategoryRepository trainingCategoryRepository;

    public List<TrainingCategory> getTrainingCategories() {
        return trainingCategoryRepository.findAll();
    }

    public TrainingCategory getTrainingCategory(Long id) {
        return trainingCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No training category found with id: " + id));
    }

    public TrainingCategory createTrainingCategory(TrainingCategory newTrainingCategory) {
        return trainingCategoryRepository.saveAndFlush(newTrainingCategory);
    }

    public TrainingCategory updateTrainingCategory(Long id, TrainingCategory updatedTrainingCategory) {
        TrainingCategory trainingCategory = trainingCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No training category found with id: " + id));
        BeanUtils.copyProperties(updatedTrainingCategory, trainingCategory, "id");
        return trainingCategoryRepository.saveAndFlush(trainingCategory);
    }

    public void deleteTrainingCategory(Long id) {
        TrainingCategory trainingCategory = trainingCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No training category found with id: " + id));
        trainingCategoryRepository.delete(trainingCategory);
    }
}
