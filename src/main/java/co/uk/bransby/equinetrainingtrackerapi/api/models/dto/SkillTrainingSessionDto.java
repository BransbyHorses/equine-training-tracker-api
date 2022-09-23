package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.ProgressCode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillTrainingSessionDto {
    private Long id;
    private LocalDateTime date;
    private TrainingProgrammeDto trainingProgramme;
    private SkillDto skill;
    private TrainingMethodDto trainingMethod;
    private TrainingEnvironmentDto environment;
    private ProgressCode progressCode;
    private Long trainingTime;
    private String notes;
}
