package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.ProgressCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillProgressRecordDto {
    private Long id;
    private TrainingProgrammeDto trainingProgramme;
    private SkillDto skill;
    private ProgressCode progressCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long time;
}
