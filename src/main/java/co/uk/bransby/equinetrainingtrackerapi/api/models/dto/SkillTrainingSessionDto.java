package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import java.time.LocalDateTime;

public class SkillTrainingSessionDto {
    private Long id;
    private LocalDateTime date;
    private TrainingProgrammeDto trainingProgramme;
    private SkillDto skill;
    private TrainingMethodDto trainingMethodDto;
    private TrainingEnvironmentDto environment;
    private Long trainingTime;
    private String notes;
}
