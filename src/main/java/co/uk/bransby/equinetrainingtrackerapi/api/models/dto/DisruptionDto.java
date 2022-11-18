package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import co.uk.bransby.equinetrainingtrackerapi.api.models.DisruptionCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DisruptionDto {
    Long id;
    DisruptionCode reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endDate;
}
