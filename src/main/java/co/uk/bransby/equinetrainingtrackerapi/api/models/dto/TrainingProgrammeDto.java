package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TrainingProgrammeDto {
    private Long id;
    private String name;
    @JsonIgnoreProperties({"trainingProgrammes"})
    private EquineDto equine;
    private List<SkillDto> skills;
    private List<TrainingDayDto> trainingDayRecord;
    private Date startDate;
    private Date endDate;
}
