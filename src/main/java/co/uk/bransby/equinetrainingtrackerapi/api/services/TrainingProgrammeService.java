package co.uk.bransby.equinetrainingtrackerapi.api.services;


import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.*;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrainingProgrammeService {

    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final SkillRepository skillRepository;
    private final SkillTrainingSessionRepository skillTrainingSessionRepository;
    private final SkillProgressRecordRepository skillProgressRecordRepository;
    private final EquineRepository equineRepository;

    public List<TrainingProgramme> getAllProgrammes() {
        return trainingProgrammeRepository.findAll();
    }

    public TrainingProgramme getProgramme(Long id) {
        return trainingProgrammeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
    }

    public TrainingProgramme createProgramme(TrainingProgramme newTrainingProgramme){

        Optional<TrainingProgramme> liveTrainingProgramme =
                equineRepository.getById(newTrainingProgramme.getEquine().getId())
                        .getTrainingProgrammes()
                        .stream()
                        .filter(tp -> tp.getEndDate() == null)
                        .findFirst();

        TrainingProgramme savedTrainingProgramme = trainingProgrammeRepository.saveAndFlush(newTrainingProgramme);
        List<SkillProgressRecord> newSkillProgressRecords = new ArrayList<>();

        if(liveTrainingProgramme.isPresent()) {
            liveTrainingProgramme.get().setEndDate(LocalDateTime.now());
            trainingProgrammeRepository.saveAndFlush(liveTrainingProgramme.get());
            List<SkillProgressRecord> skillProgressRecords = liveTrainingProgramme.get().getSkillProgressRecords();
            skillProgressRecords
                    .forEach(spr -> {
                        SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
                        skillProgressRecord.setTrainingProgramme(savedTrainingProgramme);
                        skillProgressRecord.setSkill(spr.getSkill());
                        skillProgressRecord.setProgressCode(spr.getProgressCode());
                        skillProgressRecord.setStartDate(null);
                        skillProgressRecord.setEndDate(null);
                        skillProgressRecord.setTime(0);
                        skillProgressRecordRepository.saveAndFlush(skillProgressRecord);
                        newSkillProgressRecords.add(skillProgressRecord);
                    });
        }

        if(liveTrainingProgramme.isEmpty()) {
            skillRepository.findAll()
                .forEach(skill -> {
                    SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
                    skillProgressRecord.setTrainingProgramme(savedTrainingProgramme);
                    skillProgressRecord.setSkill(skill);
                    skillProgressRecord.setTime(0);
                    skillProgressRecord.setProgressCode(ProgressCode.NOT_ABLE);
                    skillProgressRecord.setStartDate(null);
                    skillProgressRecord.setEndDate(null);
                    skillProgressRecord.setTime(0);
                    skillProgressRecordRepository.saveAndFlush(skillProgressRecord);
                    newSkillProgressRecords.add(skillProgressRecord);
                });
        }

        savedTrainingProgramme.setSkillProgressRecords(newSkillProgressRecords);
        trainingProgrammeRepository.saveAndFlush(savedTrainingProgramme);
        return savedTrainingProgramme;
    }

    public void deleteProgramme(Long id) {
        // TODO - handle delete programme
    }

    public TrainingProgramme addSkillTrainingSessionToTrainingProgramme(Long trainingProgrammeId, SkillTrainingSession newSkillTrainingSession) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(trainingProgrammeId)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + trainingProgrammeId));

        if(trainingProgramme.getStartDate() == null) {
            trainingProgramme.setStartDate(LocalDateTime.now());
        }

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
