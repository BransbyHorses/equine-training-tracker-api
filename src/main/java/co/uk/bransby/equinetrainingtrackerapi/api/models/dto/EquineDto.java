package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import lombok.Data;

import java.util.Set;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private YardDto yard;
    private CategoryDto category;
    private ProgrammeDto programme;
    private Set<SkillDto> skills;
//    private Long trainerId;
//    private String training;
//    private Boolean onHold;
}
