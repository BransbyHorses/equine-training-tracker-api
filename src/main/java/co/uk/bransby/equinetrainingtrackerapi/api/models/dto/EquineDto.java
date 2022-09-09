package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private YardDto yard;
    private CategoryDto category;
    private List<TrainingProgrammeDto> trainingProgrammes;
}
