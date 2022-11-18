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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private Long time;
}
