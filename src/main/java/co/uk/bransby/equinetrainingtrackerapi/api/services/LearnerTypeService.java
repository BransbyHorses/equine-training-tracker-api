package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.LearnerType;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.LearnerTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class LearnerTypeService {

    private final LearnerTypeRepository learnerTypeRepository;

    public List<LearnerType> getLearnerTypes() {
        return learnerTypeRepository.findAll();
    };

    public LearnerType getLearnerType(Long id){
        return learnerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No learner type found with id: " + id));
    };

    public LearnerType createLearnerType(LearnerType newLearnerType) {
        return learnerTypeRepository.saveAndFlush(newLearnerType);
    };

    public LearnerType updateLearnerType(Long id, LearnerType updatedLearnerTypeValues) {
        LearnerType updatedLearnerType = learnerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No learner type found with id: " + id));
        BeanUtils.copyProperties(updatedLearnerTypeValues, updatedLearnerType, "id");
        return learnerTypeRepository.saveAndFlush(updatedLearnerType);
    };

    public void deleteLearnerType(Long id) {
        LearnerType learnerType = learnerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No learner type found with id: " + id));
        learnerTypeRepository.deleteById(learnerType.getId());
    };

}
