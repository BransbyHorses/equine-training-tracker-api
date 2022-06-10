package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Programme;
import co.uk.bransby.equinetrainingtrackerapi.services.ProgrammeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/data/programmes")
public class ProgrammeController {

    private final ProgrammeService programmeService;

    public ProgrammeController(ProgrammeService programmeService) {
        this.programmeService = programmeService;
    }
    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<Programme> findProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return programmeService.getProgramme(id)
                .map(programme -> new ResponseEntity<>(programme, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }


    @GetMapping
    public ResponseEntity<List<Programme>> findAllProgrammes() {
        List<Programme> allProgrammes = programmeService.getAllProgrammes();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allProgrammes, resHeaders, HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<Programme> createProgramme(@RequestBody Programme programme) {
        Programme newProgramme = programmeService.createProgramme(programme);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(newProgramme, resHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Programme> updateProgramme(@PathVariable Long id, @RequestBody Programme updatedProgrammeValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Programme updatedProgramme =  programmeService.updateProgramme(id, updatedProgrammeValues);
            return new ResponseEntity<>(updatedProgramme, resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Programme> deleteProgramme(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return programmeService.getProgramme(id)
                .map(programme -> {
                    programmeService.deleteProgramme(id);
                    return new ResponseEntity<>(programme, resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }
}


