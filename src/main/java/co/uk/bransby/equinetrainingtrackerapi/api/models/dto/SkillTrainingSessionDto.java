package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SkillTrainingSessionDto {
    private Long id;
    private LocalDateTime date;
    private TrainingProgrammeDto trainingProgramme;
    private List<SkillDto> skills;
    private TrainingMethodDto trainingMethodDto;
    private EnvironmentDto environment;
    private Long trainingTime;
    private String notes;
}
