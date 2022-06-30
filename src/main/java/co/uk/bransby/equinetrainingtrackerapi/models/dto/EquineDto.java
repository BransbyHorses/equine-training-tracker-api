package co.uk.bransby.equinetrainingtrackerapi.models.dto;

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
    private Boolean onHold;
    //    private String training;
    //    private Long trainerId;

}
