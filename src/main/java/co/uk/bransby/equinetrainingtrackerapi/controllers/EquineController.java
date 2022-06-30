package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.EquineDto;
import co.uk.bransby.equinetrainingtrackerapi.services.EquineService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/data/equines")
public class EquineController {

    private final EquineService equineService;
    private final ModelMapper mapper;

    public EquineController(EquineService equineService, ModelMapper mapper) {
        this.equineService = equineService;
        this.mapper = mapper;
    }

    @GetMapping({"{id}"})
    public ResponseEntity<Equine> findEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> new ResponseEntity<>(equine, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Equine>> findAllEquines() {
        List<Equine> allEquines = equineService.getAllEquines();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allEquines, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Equine> createEquine(@RequestBody EquineDto equineToBeCreated) {
        Equine newEquine = equineService.createEquine(mapToEntity(equineToBeCreated));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(newEquine, resHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Equine> updateEquine(@PathVariable Long id, @RequestBody EquineDto updatedEquineValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        Equine updateEquine = mapToEntity(updatedEquineValues);
        try {
            Equine updatedEquine = equineService.updateEquine(id, updateEquine);
            return new ResponseEntity<>(updatedEquine, resHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Equine> deleteEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> {
                    equineService.deleteEquine(id);
                    return new ResponseEntity<>(equine, resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{equineId}/yards/{yardId}")
    public ResponseEntity<?> assignEquineToYard(@PathVariable Long equineId, @PathVariable Long yardId) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Equine updatedEquine = equineService.assignEquineToYard(equineId, yardId);
            return ResponseEntity.ok().headers(resHeaders).body(mapToDto(updatedEquine));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{equineId}/programme/{programmeId}")
    public ResponseEntity<?> assignEquineToProgramme(@PathVariable Long equineId, @PathVariable Long programmeId) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Equine updatedEquine = equineService.assignEquineToProgramme(equineId, programmeId);
            return ResponseEntity.ok().headers(resHeaders).body(mapToDto(updatedEquine));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // TODO - assign equine to category controller
    // TODO - add a skill to an equine controller

    private EquineDto mapToDto(Equine equine) {
        return mapper.map(equine, EquineDto.class);
    }
    private Equine mapToEntity(EquineDto equineDto) {
        return mapper.map(equineDto, Equine.class);
    }
}
