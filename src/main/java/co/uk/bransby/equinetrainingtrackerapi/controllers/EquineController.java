package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.services.EquineService;
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

    public EquineController(EquineService equineService) {
        this.equineService = equineService;
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
    public ResponseEntity<Equine> createEquine(@RequestBody Equine equine) {
        Equine newEquine = equineService.createEquine(equine);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(newEquine, resHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Equine> updateEquine(@PathVariable Long id, @RequestBody Equine updatedEquineValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Equine updatedEquine = equineService.updateEquine(id, updatedEquineValues);
            return new ResponseEntity<>(updatedEquine, resHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Equine> deleteEquine(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return equineService.getEquine(id)
                .map(equine -> {
                    equineService.deleteEquine(id);
                    return new ResponseEntity<>(equine, resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }
}
