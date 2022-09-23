package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TrainingProgrammeDto {
    private Long id;
    @JsonIgnoreProperties({"trainingProgrammes"})
    private EquineDto equine;
    @JsonIgnoreProperties({"trainingProgramme"})
    private List<SkillTrainingSessionDto> skillTrainingSessions;
    private Date startDate;
    private Date endDate;
}
