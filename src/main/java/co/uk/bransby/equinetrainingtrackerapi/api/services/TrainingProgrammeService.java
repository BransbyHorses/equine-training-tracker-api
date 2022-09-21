package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrainingProgrammeService {

    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final EquineRepository equineRepository;
    private final SkillRepository skillRepository;
    private final SkillTrainingSessionRepository skillTrainingSessionRepository;
    private final SkillProgressRecordRepository skillProgressRecordRepository;

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
        // TODO - handle delete programme
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

    public TrainingProgramme createSkillProgressRecordInTrainingProgramme(Long trainingProgrammeId, Long skillId) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("No skill found with id: " + skillId));

        if(trainingProgramme
                .getSkillProgressRecords()
                .stream()
                .anyMatch(record -> record.getSkill().getId().equals(skillId))
        ) {
            throw new EntityExistsException(
                    "A skill progress record with skill of id: " + skillId + " already exists on training programme: " + trainingProgrammeId
            );
        }

        SkillProgressRecord newSkillProgressRecord = new SkillProgressRecord();
        newSkillProgressRecord.setTrainingProgramme(trainingProgramme);
        newSkillProgressRecord.setSkill(skill);
        newSkillProgressRecord.setProgressCode(ProgressCode.NOT_ABLE);
        newSkillProgressRecord.setStartDate(LocalDateTime.now());
        newSkillProgressRecord.setTime(0);
        SkillProgressRecord savedSkillProgressRecord = skillProgressRecordRepository.saveAndFlush(newSkillProgressRecord);

        trainingProgramme.addSkillProgressRecord(savedSkillProgressRecord);
        trainingProgrammeRepository.saveAndFlush(trainingProgramme);
        return trainingProgramme;
    }

    public TrainingProgramme removeSkillProgressRecordFromTrainingProgramme(Long trainingProgrammeId, Long skillId) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));

        List<SkillProgressRecord> skillProgressRecords = trainingProgramme
                .getSkillProgressRecords()
                .stream()
                .filter(record -> !Objects.equals(record.getId(), skillId))
                .collect(Collectors.toList());

        trainingProgramme.setSkillProgressRecords(skillProgressRecords);
        trainingProgrammeRepository
                .saveAndFlush(trainingProgramme);
        return trainingProgramme;
    }

    public TrainingProgramme addSkillTrainingSessionToTrainingProgramme(
            Long trainingProgrammeId,
            SkillTrainingSession newSkillTrainingSession
            ) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));
        SkillTrainingSession savedSkillTrainingSession = skillTrainingSessionRepository
                .saveAndFlush(newSkillTrainingSession);
        trainingProgramme.addSkillTrainingSession(savedSkillTrainingSession);
        return trainingProgrammeRepository.saveAndFlush(trainingProgramme);
    }
}
