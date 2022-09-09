package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.ProgrammeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class ProgrammeService {

    private final ProgrammeRepository programmeRepository;
    private final EquineRepository equineRepository;

    public List<TrainingProgramme> getAllProgrammes() {
        return programmeRepository.findAll();
    }

    public TrainingProgramme getProgramme(Long id) {
        return programmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
    }

    public TrainingProgramme createProgramme(TrainingProgramme TrainingProgramme){
        return programmeRepository.saveAndFlush(TrainingProgramme);
    }

    public TrainingProgramme updateProgramme(Long id, TrainingProgramme updatedTrainingProgrammeValues) {
        TrainingProgramme trainingProgrammeToUpdate = programmeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedTrainingProgrammeValues, trainingProgrammeToUpdate, "id");
        return programmeRepository.saveAndFlush(trainingProgrammeToUpdate);
    }

    public void deleteProgramme(Long id) {
        TrainingProgramme trainingProgramme = programmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
        for(Equine equine : trainingProgramme.getEquines()) {
            trainingProgramme.removeEquine(equine);
            equine.setTrainingProgramme(null);
            equineRepository.saveAndFlush(equine);
        }
        programmeRepository.saveAndFlush(trainingProgramme);
        programmeRepository.deleteById(id);
    }
}
