package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingEnvironment;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EnvironmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;

    public List<TrainingEnvironment> getEnvironments() {
        return environmentRepository.findAll();
    }

    public TrainingEnvironment getEnvironment(Long environmentId) {
        return environmentRepository.findById(environmentId)
                .orElseThrow(() -> new EntityNotFoundException("No environment found with id: " + environmentId));
    }

    public TrainingEnvironment createEnvironment(TrainingEnvironment environmentValues) {
        return environmentRepository.saveAndFlush(environmentValues);
    }

    public TrainingEnvironment updateEnvironment(Long environmentId, TrainingEnvironment environmentValues) {
        TrainingEnvironment environment = environmentRepository.findById(environmentId)
                .orElseThrow(() -> new EntityNotFoundException("No environment found with id: " + environmentId));
        BeanUtils.copyProperties(environmentValues, environment, "id");
        environmentRepository.saveAndFlush(environment);
        return environment;
    }

    public void deleteEnvironment(Long environmentId) {
        environmentRepository.deleteById(environmentId);
    }

}
