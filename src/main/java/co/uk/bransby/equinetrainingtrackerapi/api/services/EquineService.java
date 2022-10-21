package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@AllArgsConstructor
@Service
public class EquineService {

    private final EquineRepository equineRepository;
    private final YardRepository yardRepository;
    private final EquineStatusRepository equineStatusRepository;
    private final HealthAndSafetyFlagRepository healthAndSafetyFlagRepository;

    public List<Equine> getAllEquines(){
        return equineRepository.findAll();
    }

    public Equine getEquine(Long id) {
        return equineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + id));
    }

    public Equine createEquine(Equine equine){
        return equineRepository.saveAndFlush(equine);
    }

    public Equine updateEquine(Long id, Equine updatedEquineValues) {
        Equine equineToUpdate = equineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedEquineValues, equineToUpdate, "id");
        return equineRepository.saveAndFlush(equineToUpdate);
    }

    public void deleteEquine(Long id){
        Equine equine = equineRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(("No equine found with id: " + id)));
        // TODO - remove relationships before deleting
        equine.setYard(null);
        equineRepository.saveAndFlush(equine);
        equineRepository.deleteById(id);
    }

    public Equine assignEquineToYard(Long equineId, Long yardId) {
        Optional<Equine> equineInDb = equineRepository.findById(equineId);
        if(equineInDb.isPresent()) {
            Yard yard = yardRepository.findById(yardId)
                    .orElseThrow(() -> new EntityNotFoundException("No yard found with id: " + yardId));
            Equine equine = equineInDb.get();
            equine.setYard(yard);
            return equineRepository.saveAndFlush(equine);
        } else {
            throw new EntityNotFoundException("No equine found with id: " + equineId);
        }
    }

    public Equine assignEquineToCategory(Long equineId, Long categoryId) {
        Optional<Equine> equineInDb = equineRepository.findById(equineId);
        if(equineInDb.isPresent()) {
            EquineStatus category = equineStatusRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + categoryId));
            Equine equine = equineInDb.get();
            return equineRepository.saveAndFlush(equine);
        } else {
            throw new EntityNotFoundException("No equine found with id: " + equineId);
        }
    }

    public List<TrainingProgramme> getEquineTrainingProgrammes(Long id) {
        Equine equine = equineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + id));
        return equine.getTrainingProgrammes();
    }

    public HealthAndSafetyFlag createEquineHealthAndSafetyFlag(Long id, HealthAndSafetyFlag newHealthAndSafetyFlag) {
        Equine equine = equineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + id));
        newHealthAndSafetyFlag.setEquine(equine);
        return healthAndSafetyFlagRepository.saveAndFlush(newHealthAndSafetyFlag);
    }

    public List<HealthAndSafetyFlag> getEquineHealthAndSafetyFlags(Long id) {
        Equine equine = equineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + id));
        return equine.getHealthAndSafetyFlags();
    }

    public TrainingProgramme getActiveTrainingProgramme(Long id) {
        Equine equine = equineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + id));

        if(equine.getTrainingProgrammes() == null) {
            return null;
        }

        Optional<TrainingProgramme> activeTrainingProgramme = equine
                .getTrainingProgrammes()
                .stream()
                .filter(trainingProgramme -> trainingProgramme.getEndDate() == null)
                .findFirst();

        return activeTrainingProgramme.isEmpty() ? null : activeTrainingProgramme.get();
    }

    public List<SkillTrainingSession> getEquineSkillTrainingSessions(Long id) {
        Equine equine = equineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + id));
        List<SkillTrainingSession> allSkillTrainingSessions = new ArrayList<>();
        if(equine.getTrainingProgrammes() == null) {
            return new ArrayList<>();
        }
        equine.getTrainingProgrammes()
                .forEach((trainingProgramme -> {
                    if(trainingProgramme.getSkillTrainingSessions() != null) {
                        allSkillTrainingSessions.addAll(trainingProgramme.getSkillTrainingSessions());
                    }
                }));
        return allSkillTrainingSessions;
    }
}
