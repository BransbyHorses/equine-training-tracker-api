package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.EquineStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private YardDto yard;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private EquineStatus equineStatus;
    @JsonIgnoreProperties({"equine"})
    private List<TrainingProgrammeDto> trainingProgrammes;
    private LearnerTypeDto learnerType;
    private List<HealthAndSafetyFlagDto> healthAndSafetyFlags;
}
