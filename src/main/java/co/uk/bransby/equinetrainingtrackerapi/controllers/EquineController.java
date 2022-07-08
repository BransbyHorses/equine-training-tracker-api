package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.dto.EquineDto;
import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.services.EquineService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/data/equines")
public class EquineController {

    private final EquineService equineService;
    private final ModelMapper modelMapper;

    @GetMapping({"{id}"})
    public ResponseEntity<EquineDto> findEquine(@PathVariable Long id){
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

    private EquineDto mapToDto(Equine equine){
        return modelMapper.map(equine, EquineDto.class);
    }

    private Equine mapToEntity(EquineDto equineDto){
        return modelMapper.map(equineDto, Equine.class);
    }
}
