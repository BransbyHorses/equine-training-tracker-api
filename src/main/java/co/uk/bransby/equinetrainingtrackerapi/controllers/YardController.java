package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Yard;
import co.uk.bransby.equinetrainingtrackerapi.services.YardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Yard findYard(@PathVariable Long id) {
        return yardService.readYard(id);
    }
    // read all yards
    @GetMapping
    public List<Yard> findAllYards() {
        return yardService.readAllYards();
    }
    // create yard
    @PostMapping
    public Yard createYard(@RequestBody Yard yard) {
        return yardService.createYard(yard);
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
