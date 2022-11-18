package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingMethodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class TrainingMethodService {

    private final TrainingMethodRepository trainingMethodRepository;

    public List<TrainingMethod> listMethods() {
        return trainingMethodRepository.findAll();
    }

    public TrainingMethod listMethod(Long id) {
        return trainingMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No training method found with id: " + id));
    }

    public TrainingMethod createMethod(TrainingMethod newMethod) {
        return trainingMethodRepository.saveAndFlush(newMethod);
    }

    public TrainingMethod updateMethod(Long id, TrainingMethod updatedMethod) {
        TrainingMethod method = trainingMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No training method found with id: " + id));
        BeanUtils.copyProperties(updatedMethod, method, "id");
        return trainingMethodRepository.saveAndFlush(method);
    }

    public void deleteMethod(Long id) {
        if(trainingMethodRepository.existsById(id)) {
            trainingMethodRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("No training method found with id: " + id);
        }
    }

}
