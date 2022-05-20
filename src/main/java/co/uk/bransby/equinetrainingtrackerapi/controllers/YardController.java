package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.services.YardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Optional<Yard> yard = yardService.readYard(id);
        HttpHeaders resHeaders = new HttpHeaders();
        return yard.map(data -> new ResponseEntity<Yard>(data, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    // read all yards
    @GetMapping
    public ResponseEntity<List<Yard>> findAllYards() {
        List<Yard> allYards = yardService.readAllYards();
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
    // delete yard
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Long deleteYard(@PathVariable Long id) {
        return null;
    }
    // update yard
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Yard updateYard(@RequestBody Yard yard) {
        return yardService.updateYard(yard.getId(), yard);
    }
}
