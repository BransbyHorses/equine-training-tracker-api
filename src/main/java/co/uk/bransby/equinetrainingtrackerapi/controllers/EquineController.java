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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data/equines")
public class EquineController {

    private final EquineService equineService;
    private final ModelMapper modelMapper;

    public EquineController(EquineService equineService, ModelMapper mapper) {
        this.equineService = equineService;
        this.modelMapper = mapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<EquineDto> findEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> new ResponseEntity<>(mapToDto(equine), resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EquineDto>> findAllEquines() {
        List<EquineDto> allEquines = equineService.getAllEquines()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allEquines, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EquineDto> createEquine(@RequestBody EquineDto equineToBeCreated) {
        Equine newEquine = equineService.createEquine(mapToEntity(equineToBeCreated));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(mapToDto(newEquine), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<EquineDto> updateEquine(@PathVariable Long id, @RequestBody EquineDto updatedEquineValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Equine updatedEquine = equineService.updateEquine(id, mapToEntity(updatedEquineValues));
            return new ResponseEntity<>(mapToDto(updatedEquine), resHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<EquineDto> deleteEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> {
                    equineService.deleteEquine(id);
                    return new ResponseEntity<>(mapToDto(equine), resHeaders, HttpStatus.OK);
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

    @PatchMapping("/{equineId}/programmes/{programmeId}")
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
    @PatchMapping("/{equineId}/categories/{categoryId}")
    public ResponseEntity<?> assignEquineToCategory(@PathVariable Long equineId, @ PathVariable Long  categoryId) {
        return null;
    }

    // TODO - add a skill to an equine controller
    @PatchMapping("/{equineId}/skills/{skillId}")
    public ResponseEntity<?> assignEquineASkill(@PathVariable Long equineId, @ PathVariable Long  skillId) {
        return null;
    }

    private EquineDto mapToDto(Equine equine) {
        return modelMapper.map(equine, EquineDto.class);
    }
    private Equine mapToEntity(EquineDto equineDto) {
        return modelMapper.map(equineDto, Equine.class);
    }
}
