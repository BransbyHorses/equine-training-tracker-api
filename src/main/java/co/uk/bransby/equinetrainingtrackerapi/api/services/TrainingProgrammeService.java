package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingProgrammeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Service
public class TrainingProgrammeService {

    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final EquineRepository equineRepository;
    private final SkillRepository skillRepository;

    public List<TrainingProgramme> getAllProgrammes() {
        return trainingProgrammeRepository.findAll();
    }

    public TrainingProgramme getProgramme(Long id) {
        return trainingProgrammeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
    }

    public TrainingProgramme createProgramme(TrainingProgramme TrainingProgramme){
        return trainingProgrammeRepository.saveAndFlush(TrainingProgramme);
    }

    public TrainingProgramme updateProgramme(Long id, TrainingProgramme updatedTrainingProgrammeValues) {
        TrainingProgramme trainingProgrammeToUpdate = trainingProgrammeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedTrainingProgrammeValues, trainingProgrammeToUpdate, "id");
        return trainingProgrammeRepository.saveAndFlush(trainingProgrammeToUpdate);
    }

    public void deleteProgramme(Long id) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
        trainingProgrammeRepository.saveAndFlush(trainingProgramme);
        trainingProgrammeRepository.deleteById(id);
    }

    public TrainingProgramme assignTrainingProgrammeToEquine(Long trainingProgrammeId, Long equineId) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));

        Equine equine = equineRepository.findById(equineId)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));

        trainingProgramme.setEquine(equine);
        trainingProgrammeRepository.saveAndFlush(trainingProgramme);
        return trainingProgramme;
    }

    public TrainingProgramme addSkillToTrainingProgramme(Long trainingProgrammeId, Long skillId) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("No skill found with id: " + skillId));
        trainingProgramme.getSkills().add(skill);
        trainingProgrammeRepository.saveAndFlush(trainingProgramme);
        return trainingProgramme;
    }

    public TrainingProgramme removeSkillFromTrainingProgramme(Long trainingProgrammeId, Long skillId) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("No skill found with id: " + skillId));
        trainingProgramme.getSkills().remove(skill);
        return trainingProgrammeRepository.saveAndFlush(trainingProgramme);
    }
}
