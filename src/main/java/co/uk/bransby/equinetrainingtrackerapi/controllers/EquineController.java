package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.dtos.EquineDto;
import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
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

    @GetMapping
    @RequestMapping({"{id}"})
    public ResponseEntity<Equine> findEquine(@PathVariable Long id){
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> new ResponseEntity<>(equine, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }
    @GetMapping
    public ResponseEntity<List<Equine>> findAllEquines() {
        List<Equine> allEquines = equineService.getAllEquines();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<List<Equine>>(allEquines, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Equine> createEquine(@RequestBody EquineDto equineToBeCreated) {
        Equine newlyCreatedEquine = mapToEntity(equineToBeCreated);
        Equine newEquine = equineService.createEquine(newlyCreatedEquine);
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

    private EquineDto mapToDto(Equine equine){
        return mapper.map(equine, EquineDto.class);
    }

    private Equine mapToEntity(EquineDto equineDto){
        return mapper.map(equineDto, Equine.class);
    }
}
