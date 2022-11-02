package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private YardDto yard;
    private EquineStatusDto equineStatus;
    @JsonIgnoreProperties({"equine"})
    private List<TrainingProgrammeDto> trainingProgrammes;
    private LearnerTypeDto learnerType;
    private List<HealthAndSafetyFlagDto> healthAndSafetyFlags;
    private List<DisruptionDto> disruptions;
}
