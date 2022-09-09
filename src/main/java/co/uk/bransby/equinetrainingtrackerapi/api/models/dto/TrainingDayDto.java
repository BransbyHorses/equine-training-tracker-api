package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingDayDto {
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime date;
}
