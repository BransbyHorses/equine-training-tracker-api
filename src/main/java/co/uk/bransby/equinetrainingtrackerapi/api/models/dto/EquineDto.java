package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.EquineStatus;
import lombok.Data;

import java.util.List;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private YardDto yard;
    private EquineStatusDto equineStatus;
    private List<TrainingProgrammeDto> trainingProgrammes;
    private LearnerTypeDto learnerType;
}
