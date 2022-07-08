package co.uk.bransby.equinetrainingtrackerapi.models.dto;

import lombok.Data;

@Data
public class EquineDto {
    private Long id;
    private String name;
    private String yard;
    private String category;
    private String programme;
    private String skills;
//    private Long trainerId;
//    private String training;
//    private Boolean onHold;

}
