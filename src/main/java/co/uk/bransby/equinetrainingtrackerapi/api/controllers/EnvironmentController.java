package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingEnvironment;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.TrainingEnvironmentDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EnvironmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/environments")
public class EnvironmentController {

    private final EnvironmentService environmentService;
    private  final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<TrainingEnvironmentDto>> getEnvironments() {
        List<TrainingEnvironmentDto> environments = environmentService.getEnvironments()
                .stream()
                .map(environment -> modelMapper.map(environment, TrainingEnvironmentDto.class))
                .toList();
        return ResponseEntity.ok().body(environments);
    }

    @GetMapping("{id}")
    public ResponseEntity<TrainingEnvironmentDto> getEnvironment(@PathVariable Long id) {
        TrainingEnvironment environment = environmentService.getEnvironment(id);
        return ResponseEntity.ok().body(modelMapper.map(environment, TrainingEnvironmentDto.class));
    }

    @PostMapping
    public ResponseEntity<TrainingEnvironmentDto> createEnvironment(@RequestBody TrainingEnvironmentDto environment) {
        TrainingEnvironment newEnvironment = environmentService
                .createEnvironment(modelMapper.map(environment, TrainingEnvironment.class));
        return ResponseEntity
                .created(URI.create("/data/environments" + newEnvironment.getId()))
                .body(modelMapper.map(newEnvironment, TrainingEnvironmentDto.class));
    }

    @PatchMapping("{id}")
    public ResponseEntity<TrainingEnvironmentDto> updateEnvironment(@PathVariable Long id, @RequestBody TrainingEnvironmentDto environment) {
        TrainingEnvironment updatedEnvironment = environmentService.updateEnvironment(
                id,
                modelMapper.map(environment, TrainingEnvironment.class)
        );
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedEnvironment, TrainingEnvironmentDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEnvironment(@PathVariable Long id) {
        environmentService.deleteEnvironment(id);
        return ResponseEntity
                .ok()
                .body("Environment with id " + id + " deleted");
    }
}
