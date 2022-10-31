package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.SkillTrainingSession;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingCategory;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.SkillTrainingSessionDto;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.TrainingCategoryDto;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.TrainingProgrammeDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingProgrammeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/training-programmes")
public class TrainingProgrammeController {

    private final TrainingProgrammeService trainingProgrammeService;
    private final ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<TrainingProgrammeDto> findTrainingProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        TrainingProgramme trainingProgramme = trainingProgrammeService.getProgramme(id);
        return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(trainingProgramme, TrainingProgrammeDto.class));
    }

    @GetMapping
    public ResponseEntity<List<TrainingProgrammeDto>> findAllTrainingProgrammes() {
        HttpHeaders resHeaders = new HttpHeaders();
        List<TrainingProgrammeDto> allProgrammes = trainingProgrammeService.getAllProgrammes()
                .stream()
                .map(programme -> modelMapper.map(programme, TrainingProgrammeDto.class)).toList();
        return ResponseEntity.ok().headers(resHeaders).body(allProgrammes);
    }

    @PostMapping("{trainingCategoryId}/equine/{equineId}")
    public ResponseEntity<TrainingProgrammeDto> createTrainingProgramme(
            @PathVariable Long trainingCategoryId,
            @PathVariable Long equineId
    ) {
        TrainingProgramme savedNewTrainingProgramme = trainingProgrammeService.createProgramme(trainingCategoryId, equineId);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(modelMapper.map(savedNewTrainingProgramme, TrainingProgrammeDto.class), resHeaders, HttpStatus.CREATED);
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<?> deleteTrainingProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        trainingProgrammeService.deleteProgramme(id);
        return ResponseEntity.ok().headers(resHeaders).build();
    }

    @PostMapping("{trainingProgrammeId}/skill-training-session")
    public ResponseEntity<TrainingProgrammeDto> addSkillTrainingSessionToTrainingProgramme(
            @PathVariable Long trainingProgrammeId,
            @RequestBody SkillTrainingSessionDto newSkillTrainingSession
    ) {
        TrainingProgramme updatedTrainingProgramme = trainingProgrammeService.addSkillTrainingSessionToTrainingProgramme(
                trainingProgrammeId,
                modelMapper.map(newSkillTrainingSession, SkillTrainingSession.class)
        );
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedTrainingProgramme, TrainingProgrammeDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<TrainingProgrammeDto> updateTrainingProgramme(@PathVariable Long id, @RequestBody TrainingProgrammeDto trainingProgrammeUpdatedValues) {
        TrainingProgramme updatedTrainingProgramme = trainingProgrammeService.updateTrainingProgramme(id,
                modelMapper.map(trainingProgrammeUpdatedValues, TrainingProgramme.class));

        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedTrainingProgramme, TrainingProgrammeDto.class));
    }

}
