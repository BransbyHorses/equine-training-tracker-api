package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.DisruptionDTO;
import co.uk.bransby.equinetrainingtrackerapi.services.DisruptionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/data/disruptions")
public class DisruptionController {

    private final DisruptionService disruptionService;
    private final ModelMapper disruptionModelMapper;

    public DisruptionController(DisruptionService disruptionService) {
        this.disruptionService = disruptionService;
        this.disruptionModelMapper = new ModelMapper();
    }

    @GetMapping
    public List<Disruption> getDisruptions() {
        return disruptionService.getDisruptions();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Disruption> getDisruption(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return disruptionService.getDisruption(id)
                .map(disruption -> ResponseEntity.ok().headers(resHeaders).body(disruption))
                .orElse(ResponseEntity.notFound().headers(resHeaders).build());
    }

    @PostMapping
    public ResponseEntity<Disruption> createDisruption(@RequestBody DisruptionDTO newDisruption) {
        HttpHeaders resHeaders = new HttpHeaders();
        Disruption savedDisruption = disruptionService.createDisruption(disruptionModelMapper.map(newDisruption, Disruption.class));
        return ResponseEntity.created(URI.create("/data/disruptions/" + savedDisruption.getId())).headers(resHeaders).body(savedDisruption);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Disruption> updateDisruption(@PathVariable Long id, @RequestBody DisruptionDTO updatedDisruptionValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Disruption updatedDisruption = disruptionService.updateDisruption(id, disruptionModelMapper.map(updatedDisruptionValues, Disruption.class));
            return ResponseEntity.ok().headers(resHeaders).body(updatedDisruption);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().headers(resHeaders).build();
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Disruption> deleteDisruption(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return disruptionService.getDisruption(id)
                .map(disruption -> {
                    disruptionService.deleteDisruption(id);
                    return ResponseEntity.ok().headers(resHeaders).body(disruption);
                })
                .orElse(ResponseEntity.notFound().headers(resHeaders).build());
    }
}
