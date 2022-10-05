package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrainingProgrammeService {

    private final TrainingProgrammeRepository trainingProgrammeRepository;
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

    public TrainingProgramme createProgramme(TrainingProgramme trainingProgramme){
        TrainingProgramme newTrainingProgramme = trainingProgrammeRepository
                .saveAndFlush(trainingProgramme);

        // TODO - check if the equine already has an active training programme, if true, end that trainingProgramme (add endDate)

        List<SkillProgressRecord> skillProgressRecords = new ArrayList<>();
        skillRepository.findAll()
                .forEach(skill -> {
                    SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
                    skillProgressRecord.setTrainingProgramme(trainingProgramme);
                    skillProgressRecord.setSkill(skill);
                    skillProgressRecord.setTime(0);
                    skillProgressRecord.setProgressCode(ProgressCode.NOT_ABLE);
                    skillProgressRecord.setStartDate(null);
                    skillProgressRecord.setEndDate(null);
                    skillProgressRecord.setTime(0);
                    skillProgressRecordRepository.saveAndFlush(skillProgressRecord);
                    skillProgressRecords.add(skillProgressRecord);
                });

        newTrainingProgramme
                .setSkillProgressRecords(skillProgressRecords);
        return trainingProgrammeRepository
                .saveAndFlush(newTrainingProgramme);
    }

    public TrainingProgramme updateProgramme(Long id, TrainingProgramme updatedTrainingProgrammeValues) {
        TrainingProgramme trainingProgrammeToUpdate = trainingProgrammeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedTrainingProgrammeValues, trainingProgrammeToUpdate, "id");
        return trainingProgrammeRepository.saveAndFlush(trainingProgrammeToUpdate);
    }

    public void deleteProgramme(Long id) {
        trainingProgrammeRepository.deleteById(id);
    }

    public TrainingProgramme addSkillTrainingSessionToTrainingProgramme(Long trainingProgrammeId, SkillTrainingSession newSkillTrainingSession) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));

        newSkillTrainingSession.setTrainingProgramme(trainingProgramme);
        SkillTrainingSession savedSkillTrainingSession = skillTrainingSessionRepository.saveAndFlush(newSkillTrainingSession);

        trainingProgramme.addSkillTrainingSession(savedSkillTrainingSession);
        trainingProgramme.getSkillProgressRecords()
                .stream()
                .filter(s -> s.getSkill().getId().equals(savedSkillTrainingSession.getSkill().getId()))
                .findFirst()
                .ifPresent(record -> {
                    if(record.getStartDate() == null) {
                        record.setStartDate(LocalDateTime.now());
                    }
                    record.setProgressCode(savedSkillTrainingSession.getProgressCode());
                    record.logTime(savedSkillTrainingSession.getTrainingTime());
                });

        trainingProgrammeRepository.saveAndFlush(trainingProgramme);
        return trainingProgramme;
    }

}
