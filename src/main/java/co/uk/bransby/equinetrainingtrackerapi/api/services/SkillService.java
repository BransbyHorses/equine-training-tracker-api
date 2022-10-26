package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.*;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillProgressRecordRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.SkillRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.TrainingProgrammeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final TrainingProgrammeRepository trainingProgrammeRepository;
    private final SkillProgressRecordRepository skillProgressRecordRepository;

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public Skill create(Skill newSkill) {
        Optional<Skill> skill = Optional.ofNullable(skillRepository.findByName(newSkill.getName()));
        if(skill.isPresent()) {
            throw new EntityExistsException(newSkill.getName() + " already exists");
        }
        Skill savedSkill = skillRepository.save(newSkill);
        addNewSkillToTrainingProgrammes(savedSkill);
        return savedSkill;
    }

    public Skill update(Skill updatedSkill, Long id) {
        return skillRepository.findById(id)
                .map(skill -> {
                    skill = updatedSkill;
                    skill.setId(id);
                    return skillRepository.save(skill);
                })
                .orElseGet(() -> {
                    updatedSkill.setId(id);
                    return skillRepository.save(updatedSkill);
                });
    }

    public Skill findById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
    }

    public void deleteById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
        deleteSkillProgressRecords(skill);
        skillRepository.delete(skill);
    }

    public void deleteSkillProgressRecords(Skill skillToDelete) {
        List<SkillProgressRecord> skillProgressRecords = skillProgressRecordRepository
                .findAll()
                .stream()
                .filter(skillProgressRecord -> skillProgressRecord.getSkill().getId().equals(skillToDelete.getId()))
                .toList();
        skillProgressRecords.forEach(skillProgressRecord -> {
            skillProgressRecordRepository.deleteById(skillProgressRecord.getId());
        });
    }

    public void addNewSkillToTrainingProgrammes(Skill newSkill) {
        List<TrainingProgramme> trainingProgrammes = trainingProgrammeRepository.findAll();
        trainingProgrammes.forEach(trainingProgramme -> {
            SkillProgressRecord skillProgressRecord = new SkillProgressRecord();
            skillProgressRecord.setTrainingProgramme(trainingProgramme);
            skillProgressRecord.setSkill(newSkill);
            skillProgressRecord.setProgressCode(ProgressCode.NOT_ABLE);
            skillProgressRecord.setTime(0);
            skillProgressRecordRepository.saveAndFlush(skillProgressRecord);
            trainingProgramme.addSkillProgressRecord(skillProgressRecord);
            trainingProgrammeRepository.saveAndFlush(trainingProgramme);
        });
    }

}
