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

    @PatchMapping("{equineId}/programmes/{programmeId}")
    public ResponseEntity<?> assignProgrammeToEquine(@PathVariable Long equineId, @PathVariable Long programmeId) {
        Equine equine = equineService.assignEquineToProgramme(equineId, programmeId);
        return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
    }

    @PatchMapping("{equineId}/yards/{yardId}")
    public ResponseEntity<?> assignYardToEquine(@PathVariable Long equineId, @PathVariable Long yardId) {
        Equine equine = equineService.assignEquineToYard(equineId, yardId);
        return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
    }

    @PatchMapping("{equineId}/categories/{categoryId}")
    public ResponseEntity<?> assignCategoryToEquine(@PathVariable Long equineId, @PathVariable Long categoryId) {
        Equine equine = equineService.assignEquineToCategory(equineId, categoryId);
        return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
    }

    @PatchMapping("{equineId}/skills/{skillId}")
    public ResponseEntity<?> assignSkillToEquine(@PathVariable Long equineId, @PathVariable Long skillId) {
        Equine equine = equineService.assignEquineASkill(equineId, skillId);
        return ResponseEntity.ok().body(modelMapper.map(equine, EquineDto.class));
    }

    @DeleteMapping("{equineId}/skills/{skillId}")
    public ResponseEntity<?> deleteEquineSkill(@PathVariable Long equineId, @PathVariable Long skillId) {
        equineService.deleteEquineSkill(equineId, skillId);
        return ResponseEntity.ok().build();
    }
}
