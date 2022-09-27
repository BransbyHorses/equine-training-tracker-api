package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.SkillTrainingSession;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.SkillTrainingSessionDto;
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
    public ResponseEntity<List<TrainingProgrammeDto>> findTrainingAllProgrammes() {
        HttpHeaders resHeaders = new HttpHeaders();
        List<TrainingProgrammeDto> allProgrammes = trainingProgrammeService.getAllProgrammes()
                .stream()
                .map(programme -> modelMapper.map(programme, TrainingProgrammeDto.class)).toList();
        return ResponseEntity.ok().headers(resHeaders).body(allProgrammes);
    }

    @PostMapping
    public ResponseEntity<TrainingProgrammeDto> createTrainingProgramme(@RequestBody TrainingProgrammeDto newProgramme) {
        TrainingProgramme savedNewTrainingProgramme = trainingProgrammeService.createProgramme(modelMapper.map(newProgramme, TrainingProgramme.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(modelMapper.map(savedNewTrainingProgramme, TrainingProgrammeDto.class), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTrainingProgramme(@PathVariable Long id, @RequestBody TrainingProgrammeDto updatedProgrammeValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        TrainingProgramme updatedTrainingProgramme = trainingProgrammeService.updateProgramme(id, modelMapper.map(updatedProgrammeValues, TrainingProgramme.class));
            return ResponseEntity
                    .ok()
                    .headers(resHeaders)
                    .body(modelMapper.map(updatedTrainingProgramme, TrainingProgrammeDto.class));
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

}
