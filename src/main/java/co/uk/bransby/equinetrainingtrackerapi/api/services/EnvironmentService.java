package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Environment;
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

    public List<Environment> getEnvironments() {
        return environmentRepository.findAll();
    }

    public Environment getEnvironment(Long environmentId) {
        return environmentRepository.findById(environmentId)
                .orElseThrow(() -> new EntityNotFoundException("No environment found with id: " + environmentId));
    }

    public Environment createEnvironment(Environment environmentValues) {
        return environmentRepository.saveAndFlush(environmentValues);
    }

    public Environment updateEnvironment(Long environmentId, Environment environmentValues) {
        Environment environment = environmentRepository.findById(environmentId)
                .orElseThrow(() -> new EntityNotFoundException("No environment found with id: " + environmentId));
        BeanUtils.copyProperties(environmentValues, environment, "id");
        environmentRepository.saveAndFlush(environment);
        return environment;
    }

    public void deleteEnvironment(Long environmentId) {
        environmentRepository.deleteById(environmentId);
    }

}
