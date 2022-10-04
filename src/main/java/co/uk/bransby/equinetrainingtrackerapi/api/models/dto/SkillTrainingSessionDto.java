package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.ProgressCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillTrainingSessionDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime date;
    private TrainingProgrammeDto trainingProgramme;
    private SkillDto skill;
    private TrainingMethodDto trainingMethod;
    private TrainingEnvironmentDto environment;
    private ProgressCode progressCode;
    private Integer trainingTime;
    private String notes;
}
