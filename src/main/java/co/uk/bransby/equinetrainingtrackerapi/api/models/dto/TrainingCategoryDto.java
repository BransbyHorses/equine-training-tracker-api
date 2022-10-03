package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import lombok.Data;

@Data
public class TrainingCategoryDto {
    private Long id;
    private String name;
    private String description;
}
