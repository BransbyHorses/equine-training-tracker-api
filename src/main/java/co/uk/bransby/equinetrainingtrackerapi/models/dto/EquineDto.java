package co.uk.bransby.equinetrainingtrackerapi.models.dto;

import lombok.Data;

import java.util.Set;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private YardDTO yard;
    private CategoryDTO category;
    private ProgrammeDTO programme;
    private Set<SkillDto> skills;
    private Boolean onHold;
    //    private String training;
    //    private Long trainerId;

}
