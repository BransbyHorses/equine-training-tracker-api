package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillProgressRecordDto {
    private Long id;
    private TrainingProgrammeDto trainingProgramme;
    private SkillDto skill;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long time;
}
