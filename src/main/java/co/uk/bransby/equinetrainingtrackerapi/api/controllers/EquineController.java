package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.EquineDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EquineService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return equineService.getEquine(id)
                .map(equine -> new ResponseEntity<>(mapToDto(equine), resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EquineDto>> findAllEquines() {
        List<EquineDto> allEquines = equineService.getAllEquines()
                .stream()
                .map(equine -> mapToDto((Equine) equine)).toList();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<List<EquineDto>>(allEquines, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EquineDto> createEquine(@RequestBody EquineDto equineToBeCreated) {
        Equine newlyCreatedEquine = mapToEntity(equineToBeCreated);
        Equine newEquine = equineService.createEquine(newlyCreatedEquine);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(mapToDto(newEquine), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateEquine(@PathVariable Long id, @RequestBody EquineDto updatedEquineValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        Equine updateEquine = mapToEntity(updatedEquineValues);
        try {
            Equine updatedEquine = equineService.updateEquine(id, updateEquine);
            return new ResponseEntity<>(mapToDto(updatedEquine), resHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<EquineDto> deleteEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> {
                    equineService.deleteEquine(id);
                    return new ResponseEntity<>(mapToDto(equine), resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @PatchMapping("{equineId}/programmes/{programmeId}")
    public ResponseEntity<?> assignProgrammeToEquine(@PathVariable Long equineId, @PathVariable Long programmeId) {
        try {
            Equine equine = equineService.assignEquineToProgramme(equineId, programmeId);
            return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{equineId}/yards/{yardId}")
    public ResponseEntity<?> assignYardToEquine(@PathVariable Long equineId, @PathVariable Long yardId) {
        try {
            Equine equine = equineService.assignEquineToYard(equineId, yardId);
            return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{equineId}/categories/{categoryId}")
    public ResponseEntity<?> assignCategoryToEquine(@PathVariable Long equineId, @PathVariable Long categoryId) {
        try {
            Equine equine = equineService.assignEquineToCategory(equineId, categoryId);
            return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{equineId}/skills/{skillId}")
    public ResponseEntity<?> assignSkillToEquine(@PathVariable Long equineId, @PathVariable Long skillId) {
        try {
            Equine equine = equineService.assignEquineASkill(equineId, skillId);
            return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("{equineId}/skills/{skillId}")
    public ResponseEntity<?> deleteEquineSkill(@PathVariable Long equineId, @PathVariable Long skillId) {
        try {
            equineService.deleteEquineSkill(equineId, skillId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
