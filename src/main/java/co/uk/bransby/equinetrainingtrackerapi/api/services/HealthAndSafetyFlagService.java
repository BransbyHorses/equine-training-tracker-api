package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.HealthAndSafetyFlag;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.HealthAndSafetyFlagRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class HealthAndSafetyFlagService {

    private final HealthAndSafetyFlagRepository healthAndSafetyFlagRepository;

    public HealthAndSafetyFlagService(HealthAndSafetyFlagRepository healthAndSafetyFlagRepository) {
        this.healthAndSafetyFlagRepository = healthAndSafetyFlagRepository;
    }

    public HealthAndSafetyFlag editHealthAndSafetyFlag(Long id, HealthAndSafetyFlag updatedHealthAndSafetyFlagValues) {
        HealthAndSafetyFlag healthAndSafetyFlag = healthAndSafetyFlagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No health and safety flag found with id: " + id));
        BeanUtils.copyProperties(updatedHealthAndSafetyFlagValues, healthAndSafetyFlag, "id");
        return healthAndSafetyFlagRepository.saveAndFlush(healthAndSafetyFlag);
    }

    public void deleteHealthAndSafetyFlag(Long id) {
        HealthAndSafetyFlag healthAndSafetyFlag = healthAndSafetyFlagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No health and safety flag found with id: " + id));
        healthAndSafetyFlagRepository.delete(healthAndSafetyFlag);
    }
}