package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.LearnerTypes;
import co.uk.bransby.equinetrainingtrackerapi.repositories.LearnerTypesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class LearnerTypesService {

    private final LearnerTypesRepository learnerTypesRepository;

    public LearnerTypesService(LearnerTypesRepository learnerTypesRepository) {
        this.learnerTypesRepository = learnerTypesRepository;
    }

    public List<LearnerTypes> getAllLearnerTypes() {
        return learnerTypesRepository.findAll();
    }

    public Optional<LearnerTypes> getLearnerTypes(Long id) {
        return learnerTypesRepository.findById(id);
    }

    public LearnerTypes createLearnerTypes(LearnerTypes learnerTypes){
        return learnerTypesRepository.saveAndFlush(learnerTypes);
    }

    public LearnerTypes updateLearnerTypes(Long id, LearnerTypes updatedLearnerTypesValues) throws EntityNotFoundException {
        LearnerTypes learnerTypesToUpdate = learnerTypesRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedLearnerTypesValues, learnerTypesToUpdate, "id");
        return learnerTypesRepository.saveAndFlush(learnerTypesToUpdate);
    }

    public void deleteLearnerTypes(Long id) {
        learnerTypesRepository.deleteById(id);
    }
}