package co.uk.bransby.equinetrainingtrackerapi.services;

import co.uk.bransby.equinetrainingtrackerapi.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.repositories.DisruptionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class DisruptionService {

    private final DisruptionRepository disruptionRepository;

    public DisruptionService(DisruptionRepository disruptionRepository) {
        this.disruptionRepository = disruptionRepository;
    }

    public List<Disruption> getDisruptions() {
        return disruptionRepository.findAll();
    }

    public Optional<Disruption> getDisruption(Long id) {
        return disruptionRepository.findById(id);
    }

    public Disruption createDisruption(Disruption newDisruption) {
        return disruptionRepository.saveAndFlush(newDisruption);
    }

    public Disruption updateDisruption(Long id, Disruption updatedDisruptionValues) {
        Disruption disruptionToUpdate = disruptionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedDisruptionValues, disruptionToUpdate, "id");
        return disruptionRepository.saveAndFlush(disruptionToUpdate);
    }

    public void deleteDisruption(Long id) {
        disruptionRepository.deleteById(id);
    }
}
