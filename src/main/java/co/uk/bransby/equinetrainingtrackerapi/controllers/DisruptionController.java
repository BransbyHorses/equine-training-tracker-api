package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.DisruptionDto;
import co.uk.bransby.equinetrainingtrackerapi.services.DisruptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/data/disruptions")
public class DisruptionController {

    private final DisruptionService disruptionService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<DisruptionDto>> getDisruptions() {
        HttpHeaders resHeaders = new HttpHeaders();
        List<DisruptionDto> disruptions = disruptionService.getDisruptions()
                .stream()
                .map(disruption -> modelMapper.map(disruption, DisruptionDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().headers(resHeaders).body(disruptions);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<DisruptionDto> getDisruption(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return disruptionService.getDisruption(id)
                .map(disruption -> ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(disruption, DisruptionDto.class)))
                .orElse(ResponseEntity.notFound().headers(resHeaders).build());
    }

    @PostMapping
    public ResponseEntity<DisruptionDto> createDisruption(@RequestBody DisruptionDto newDisruption) {
        HttpHeaders resHeaders = new HttpHeaders();
        Disruption savedDisruption = disruptionService.createDisruption(modelMapper.map(newDisruption, Disruption.class));
        return ResponseEntity.created(URI.create("/data/disruptions/" + savedDisruption.getId())).headers(resHeaders).body(modelMapper.map(savedDisruption, DisruptionDto.class));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> updateDisruption(@PathVariable Long id, @RequestBody DisruptionDto updatedDisruptionValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Disruption updatedDisruption = disruptionService.updateDisruption(id, modelMapper.map(updatedDisruptionValues, Disruption.class));
            return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(updatedDisruption, DisruptionDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().headers(resHeaders).build();
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteDisruption(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return disruptionService.getDisruption(id)
                .map(disruption -> {
                    disruptionService.deleteDisruption(id);
                    return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(disruption, DisruptionDto.class));
                })
                .orElse(ResponseEntity.notFound().headers(resHeaders).build());
    }
}
