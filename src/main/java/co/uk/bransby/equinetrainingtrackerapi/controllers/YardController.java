package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.services.YardService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/data/yards")
public class YardController {

    private final YardService yardService;

    public YardController(YardService yardService) {
        this.yardService = yardService;
    }

    // read yards
    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<Yard> findYard(@PathVariable Long id) {
        Optional<Yard> yard = yardService.getYard(id);
        HttpHeaders resHeaders = new HttpHeaders();
        return yard.map(data -> new ResponseEntity<Yard>(data, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    // read all yards
    @GetMapping
    public ResponseEntity<List<Yard>> findAllYards() {
        List<Yard> allYards = yardService.getAllYards();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<List<Yard>>(allYards, resHeaders, HttpStatus.OK);
    }
    // create yard
    @PostMapping
    public ResponseEntity<Yard> createYard(@RequestBody Yard yard) {
        Yard newYard = yardService.createYard(yard);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<Yard>(newYard, resHeaders, HttpStatus.CREATED);
    }
    // update yard
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Yard> updateYard(@PathVariable Long id, @RequestBody Yard updatedYardValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Yard updatedYard =  yardService.updateYard(id, updatedYardValues);
            return new ResponseEntity<Yard>(updatedYard, resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }
    // delete yard
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteYard(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        yardService.deleteYard(id);
        return new ResponseEntity(resHeaders, HttpStatus.OK);
    }
}
