package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.YardDto;
import co.uk.bransby.equinetrainingtrackerapi.services.YardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/data/yards")
public class YardController {

    private final YardService yardService;
    private final ModelMapper modelMapper;

    public YardController(YardService yardService) {
        this.yardService = yardService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<Yard> findYard(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return yardService.getYard(id)
                .map(yard -> new ResponseEntity<>(yard, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Yard>> findAllYards() {
        List<Yard> allYards = yardService.getAllYards();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allYards, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Yard> createYard(@RequestBody YardDto newYard) {
        Yard createdYard = yardService.createYard(modelMapper.map(newYard, Yard.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(createdYard, resHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Yard> updateYard(@PathVariable Long id, @RequestBody YardDto updatedYardValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Yard updatedYard =  yardService.updateYard(id, modelMapper.map(updatedYardValues, Yard.class));
            return new ResponseEntity<>(updatedYard, resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Yard> deleteYard(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return yardService.getYard(id)
                .map(yard -> {
                    yardService.deleteYard(id);
                    return new ResponseEntity<>(yard, resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }
}
