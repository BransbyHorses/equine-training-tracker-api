package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.ProgrammeDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.ProgrammeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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
        try {
            Programme programme = programmeService.getProgramme(id);
            return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(programme, ProgrammeDto.class));
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProgrammeDto>> findAllProgrammes() {
        List<ProgrammeDto> allProgrammes = programmeService.getAllProgrammes()
                .stream()
                .map(programme -> modelMapper.map(programme, ProgrammeDto.class)).toList();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allProgrammes, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProgrammeDto> createProgramme(@RequestBody ProgrammeDto newProgramme) {
        Programme savedNewProgramme = programmeService.createProgramme(modelMapper.map(newProgramme, Programme.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(modelMapper.map(savedNewProgramme, ProgrammeDto.class), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProgramme(@PathVariable Long id, @RequestBody ProgrammeDto updatedProgrammeValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Programme updatedProgramme =  programmeService.updateProgramme(id, modelMapper.map(updatedProgrammeValues, Programme.class));
            return new ResponseEntity<>(modelMapper.map(updatedProgramme, ProgrammeDto.class), resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping ("{id}")
    public ResponseEntity<?> deleteProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Programme programme = programmeService.getProgramme(id);
            programmeService.deleteProgramme(id);
            return ResponseEntity.ok().headers(resHeaders).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(resHeaders).build();
        }
    }
}


