package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
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
    @JsonIgnoreProperties({"trainingProgrammes"})
    private List<Skill> skills;
    private Date startDate;
    private Date endDate;
}
