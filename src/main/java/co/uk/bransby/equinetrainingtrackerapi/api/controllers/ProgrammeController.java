package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingProgramme;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.ProgrammeDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.ProgrammeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/programmes")
public class ProgrammeController {

    private final ProgrammeService programmeService;
    private final ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<ProgrammeDto> findProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        TrainingProgramme trainingProgramme = programmeService.getProgramme(id);
        return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(trainingProgramme, ProgrammeDto.class));

    }

    @GetMapping
    public ResponseEntity<List<ProgrammeDto>> findAllProgrammes() {
        HttpHeaders resHeaders = new HttpHeaders();
        List<ProgrammeDto> allProgrammes = programmeService.getAllProgrammes()
                .stream()
                .map(programme -> modelMapper.map(programme, ProgrammeDto.class)).toList();
        return ResponseEntity.ok().headers(resHeaders).body(allProgrammes);
    }

    @PostMapping
    public ResponseEntity<ProgrammeDto> createProgramme(@RequestBody ProgrammeDto newProgramme) {
        TrainingProgramme savedNewTrainingProgramme = programmeService.createProgramme(modelMapper.map(newProgramme, TrainingProgramme.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(modelMapper.map(savedNewTrainingProgramme, ProgrammeDto.class), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProgramme(@PathVariable Long id, @RequestBody ProgrammeDto updatedProgrammeValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        TrainingProgramme updatedTrainingProgramme = programmeService.updateProgramme(id, modelMapper.map(updatedProgrammeValues, TrainingProgramme.class));
            return ResponseEntity
                    .ok()
                    .headers(resHeaders)
                    .body(modelMapper.map(updatedTrainingProgramme, ProgrammeDto.class));
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<?> deleteProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        programmeService.deleteProgramme(id);
        return ResponseEntity.ok().headers(resHeaders).build();
    }
}


