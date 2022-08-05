package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Disruption;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.DisruptionDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.DisruptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
                .map(disruption -> modelMapper.map(disruption, DisruptionDto.class)).toList();
        return ResponseEntity.ok().headers(resHeaders).body(disruptions);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<DisruptionDto> getDisruption(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        Disruption disruption = disruptionService.getDisruption(id);
        return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(disruption, DisruptionDto.class));

    }

    @PostMapping
    public ResponseEntity<DisruptionDto> createDisruption(@RequestBody DisruptionDto newDisruption) {
        HttpHeaders resHeaders = new HttpHeaders();
        Disruption savedDisruption = disruptionService.createDisruption(modelMapper.map(newDisruption, Disruption.class));
        return ResponseEntity.created(URI.create("/data/disruptions/" + savedDisruption.getId())).headers(resHeaders).body(modelMapper.map(savedDisruption, DisruptionDto.class));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<DisruptionDto> updateDisruption(@PathVariable Long id, @RequestBody DisruptionDto updatedDisruptionValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        Disruption updatedDisruption = disruptionService.updateDisruption(id, modelMapper.map(updatedDisruptionValues, Disruption.class));
        return ResponseEntity.ok().headers(resHeaders).body(modelMapper.map(updatedDisruption, DisruptionDto.class));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteDisruption(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        disruptionService.deleteDisruption(id);
        return ResponseEntity.ok().headers(resHeaders).build();

    }
}
