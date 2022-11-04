package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.HealthAndSafetyFlag;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.*;
import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EquineService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/data/equines")
public class EquineController {

    private final EquineService equineService;
    private final ModelMapper modelMapper;

    private EquineDto mapToDto(Equine equine) {
        return modelMapper.map(equine, EquineDto.class);
    }
    private Equine mapToEntity(EquineDto equineDto) {
        return modelMapper.map(equineDto, Equine.class);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<EquineDto> findEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        Equine equine = equineService.getEquine(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .body(mapToDto(equine));
    }

    @GetMapping
    public ResponseEntity<List<EquineDto>> findAllEquines() {
        List<EquineDto> allEquines = equineService.getAllEquines()
                .stream()
                .map(this::mapToDto).toList();
        return ResponseEntity
                .ok()
                .body(allEquines);
    }

    @PostMapping
    public ResponseEntity<EquineDto> createEquine(@RequestBody EquineDto equineToBeCreated) {
        Equine newlyCreatedEquine = mapToEntity(equineToBeCreated);
        Equine newEquine = equineService.createEquine(newlyCreatedEquine);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(mapToDto(newEquine), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<EquineDto> updateEquine(@PathVariable Long id, @RequestBody EquineDto updatedEquineValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        Equine updatedEquine = equineService.updateEquine(id, mapToEntity(updatedEquineValues));
        return new ResponseEntity<>(mapToDto(updatedEquine), resHeaders, HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        equineService.deleteEquine(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .build();
    }

    @PatchMapping("{equineId}/yards/{yardId}")
    public ResponseEntity<?> assignYardToEquine(@PathVariable Long equineId, @PathVariable Long yardId) {
        Equine equine = equineService.assignEquineToYard(equineId, yardId);
        return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
    }

    @PatchMapping("{equineId}/learner-types/{learnerTypeId}")
    public ResponseEntity<?> assignEquineALearnerType(@PathVariable Long equineId, @PathVariable Long learnerTypeId) {
        Equine updatedEquine = equineService.assignEquineALearnerType(equineId, learnerTypeId);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedEquine, EquineDto.class));
    }



    @PatchMapping("{equineId}/equine-status/{equineStatusId}")
    public ResponseEntity<?> assignEquineAStatus(@PathVariable Long equineId, @PathVariable Long equineStatusId){
        Equine updatedEquine = equineService.assignEquineAStatus(equineId, equineStatusId);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedEquine, EquineDto.class));
    }

    @PatchMapping("{equineId}/learner-type/{learnerTypeId}")
    public ResponseEntity<?> assignEquineLearnerType(@PathVariable Long equineId, @PathVariable Long learnerTypeId){
        Equine updatedEquine = equineService.assignEquineALearnerType(equineId, learnerTypeId);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedEquine, EquineDto.class));
    }

    @GetMapping("{equineId}/training-programmes")
    public ResponseEntity<List<TrainingProgrammeDto>> getEquineTrainingProgrammes(@PathVariable Long equineId) {
        List<TrainingProgrammeDto> trainingProgrammes = equineService
                .getEquineTrainingProgrammes(equineId)
                .stream()
                .map(trainingProgramme -> modelMapper.map(trainingProgramme, TrainingProgrammeDto.class))
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(trainingProgrammes);
    }

    @PostMapping("{equineId}/health-and-safety-flags")
    public ResponseEntity<HealthAndSafetyFlagDto> createEquineHealthAndSafetyFlag(@PathVariable Long equineId, @RequestBody HealthAndSafetyFlagDto newHealthAndSafetyFlag) {
        HealthAndSafetyFlag savedHealthAndSafetyFlag = equineService.createEquineHealthAndSafetyFlag(
                equineId, modelMapper.map(newHealthAndSafetyFlag, HealthAndSafetyFlag.class)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelMapper.map(savedHealthAndSafetyFlag, HealthAndSafetyFlagDto.class));
    }

    @GetMapping("{equineId}/health-and-safety-flags")
        public ResponseEntity<List<HealthAndSafetyFlagDto>> getEquineHealthAndSafetyFlags(@PathVariable Long equineId) {
        List<HealthAndSafetyFlagDto> equineHealthAndSafetyFlags =
                equineService.getEquineHealthAndSafetyFlags(equineId)
                        .stream()
                        .map(hsf -> modelMapper.map(hsf, HealthAndSafetyFlagDto.class))
                        .toList();
        return ResponseEntity
                .ok()
                .body(equineHealthAndSafetyFlags);
    }

    @GetMapping("{equineId}/training-programmes/latest")
    public ResponseEntity<TrainingProgrammeDto> getActiveTrainingProgramme(@PathVariable Long equineId) {
        TrainingProgramme activeTrainingProgramme = equineService.getActiveTrainingProgramme(equineId);

        return activeTrainingProgramme == null ?
                ResponseEntity
                        .noContent()
                        .build()
                : ResponseEntity
                        .ok()
                        .body(modelMapper.map(activeTrainingProgramme, TrainingProgrammeDto.class));
    }

    @GetMapping("{equineId}/skill-training-sessions")
    public ResponseEntity<List<SkillTrainingSessionDto>> getEquineSkillTrainingSessions(@PathVariable Long equineId) {
        List<SkillTrainingSessionDto> allSkillTrainingSessions = equineService.getEquineSkillTrainingSessions(equineId)
                .stream()
                .map(skillTrainingSession -> modelMapper.map(skillTrainingSession, SkillTrainingSessionDto.class))
                .toList();
        return ResponseEntity
                .ok()
                .body(allSkillTrainingSessions);
    }

    @PostMapping("{equineId}/disruptions/{disruptionCodeId}/start")
    public ResponseEntity<DisruptionDto> logNewDisruption(@PathVariable Long equineId, @PathVariable int disruptionCodeId) {
        Disruption savedDisruption = equineService.logNewDisruption(disruptionCodeId, equineId);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(savedDisruption, DisruptionDto.class));
    }

    @PostMapping("{equineId}/disruptions/{disruptionId}/end")
    public ResponseEntity<DisruptionDto> endDisruption(@PathVariable int disruptionId, @PathVariable Long equineId) {
        Disruption updatedDisruption = equineService.endDisruption(equineId, disruptionId);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedDisruption, DisruptionDto.class));
    }
}
