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
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrainingProgrammeService {

    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final SkillRepository skillRepository;
    private final SkillTrainingSessionRepository skillTrainingSessionRepository;
    private final SkillProgressRecordRepository skillProgressRecordRepository;
    private final EquineRepository equineRepository;
    private final TrainingCategoryRepository trainingCategoryRepository;

    public List<TrainingProgramme> getAllProgrammes() {
        return trainingProgrammeRepository.findAll();
    }

    public TrainingProgramme getProgramme(Long id) {
        return trainingProgrammeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No programme found with id: " + id));
    }

    public TrainingProgramme createProgramme(Long trainingCategoryId, Long equineId){
        Equine equine = equineRepository.findById(equineId)
                .orElseThrow(() -> new EntityNotFoundException("No equine found with id: " + equineId));

        TrainingCategory trainingCategory = trainingCategoryRepository.findById(trainingCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("No training category found with id: " + trainingCategoryId));

        TrainingProgramme lastTrainingProgramme =
                equine.getTrainingProgrammes() == null || equine.getTrainingProgrammes().size() == 0 ?
                        null
                        :
                        equine.getTrainingProgrammes()
                                .stream()
                                .sorted(Comparator.comparing(TrainingProgramme::getCreatedOn).reversed())
                                .toList()
                                .get(0);

        TrainingProgramme newTrainingProgramme = new TrainingProgramme();
        newTrainingProgramme.setEquine(equine);
        newTrainingProgramme.setTrainingCategory(trainingCategory);
        trainingProgrammeRepository.saveAndFlush(newTrainingProgramme);

        List<SkillProgressRecord> newSkillProgressRecords;

        if(lastTrainingProgramme == null) {
            newSkillProgressRecords = createNewSkillProgressRecords(newTrainingProgramme);
        } else {
            lastTrainingProgramme.setEndDate(LocalDateTime.now());
            trainingProgrammeRepository.saveAndFlush(lastTrainingProgramme);
            newSkillProgressRecords = transferSkillProgressRecords(lastTrainingProgramme, newTrainingProgramme);
        }

        newTrainingProgramme.setSkillProgressRecords(newSkillProgressRecords);
        trainingProgrammeRepository.saveAndFlush(newTrainingProgramme);
        return newTrainingProgramme;
    }

    public void deleteProgramme(Long id) {
        trainingProgrammeRepository.deleteById(id);
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

    public List<SkillProgressRecord> createNewSkillProgressRecords(TrainingProgramme trainingProgramme) {
        List<SkillProgressRecord> skillProgressRecords = new ArrayList<>();
        skillRepository
                .findAll()
                .forEach(skill -> {
                    SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
                    skillProgressRecord.setTrainingProgramme(trainingProgramme);
                    skillProgressRecord.setSkill(skill);
                    skillProgressRecord.setProgressCode(ProgressCode.NOT_ABLE);
                    skillProgressRecord.setStartDate(null);
                    skillProgressRecord.setEndDate(null);
                    skillProgressRecord.setTime(0);
                    skillProgressRecordRepository.saveAndFlush(skillProgressRecord);
                    skillProgressRecords.add(skillProgressRecord);
                });
        return skillProgressRecords;
    }

    public List<SkillProgressRecord> transferSkillProgressRecords(
            TrainingProgramme oldTrainingProgramme,
            TrainingProgramme newTrainingProgramme
    ) {
        List<SkillProgressRecord> skillProgressRecords = new ArrayList<>();
        oldTrainingProgramme
                .getSkillProgressRecords()
                .forEach(skillProgressRecord -> {
                    SkillProgressRecord newSkillProgressRecord = new SkillProgressRecord();
                    newSkillProgressRecord.setTrainingProgramme(newTrainingProgramme);
                    newSkillProgressRecord.setSkill(skillProgressRecord.getSkill());
                    newSkillProgressRecord.setProgressCode(skillProgressRecord.getProgressCode());
                    newSkillProgressRecord.setStartDate(null);
                    newSkillProgressRecord.setEndDate(null);
                    newSkillProgressRecord.setTime(0);
                    skillProgressRecordRepository.saveAndFlush(newSkillProgressRecord);
                    skillProgressRecords.add(newSkillProgressRecord);
                });

        return skillProgressRecords;
    }

    public TrainingProgramme updateTrainingProgramme(Long id, TrainingProgramme updatedTrainingProgramme) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No training programme found with id: " + id));
        BeanUtils.copyProperties(updatedTrainingProgramme, trainingProgramme, "id");
        return trainingProgramme;
    }
}
