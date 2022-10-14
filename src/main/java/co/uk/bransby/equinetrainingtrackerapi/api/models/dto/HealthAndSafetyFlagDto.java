package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthAndSafetyFlagDto {
    private Long id;
    private String content;
    private LocalDateTime dateCreated;
}
