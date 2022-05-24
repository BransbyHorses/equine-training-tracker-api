package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.services.YardService;
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

    public YardController(YardService yardService) {
        this.yardService = yardService;
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
    public ResponseEntity<Yard> createYard(@RequestBody Yard yard) {
        Yard newYard = yardService.createYard(yard);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(newYard, resHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Yard> updateYard(@PathVariable Long id, @RequestBody Yard updatedYardValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Yard updatedYard =  yardService.updateYard(id, updatedYardValues);
            return new ResponseEntity<>(updatedYard, resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
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
