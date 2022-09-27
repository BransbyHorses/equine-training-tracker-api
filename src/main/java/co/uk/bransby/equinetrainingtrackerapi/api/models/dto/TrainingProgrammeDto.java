package co.uk.bransby.equinetrainingtrackerapi.api.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TrainingProgrammeDto {
    private Long id;
    private TrainingCategoryDto trainingCategory;
    @JsonIgnoreProperties({"trainingProgrammes"})
    private EquineDto equine;
    @JsonIgnoreProperties({"trainingProgramme"})
    private List<SkillProgressRecordDto> skillProgressRecords;
    @JsonIgnoreProperties({"trainingProgramme"})
    private List<SkillTrainingSessionDto> skillTrainingSessions;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
}
