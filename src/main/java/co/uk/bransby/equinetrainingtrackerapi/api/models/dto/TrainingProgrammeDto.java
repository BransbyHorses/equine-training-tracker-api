package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Skill;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TrainingProgrammeDto {
    private Long id;
    private String name;
    private EquineDto equine;
    private List<Skill> skills;
    private Date startDate;
    private Date endDate;
}
