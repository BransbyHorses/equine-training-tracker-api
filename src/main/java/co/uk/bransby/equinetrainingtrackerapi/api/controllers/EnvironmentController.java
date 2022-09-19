package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Environment;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.EnvironmentDto;
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
    public ResponseEntity<List<EnvironmentDto>> getEnvironments() {
        List<EnvironmentDto> environments = environmentService.getEnvironments()
                .stream()
                .map(environment -> modelMapper.map(environment, EnvironmentDto.class))
                .toList();
        return ResponseEntity.ok().body(environments);
    }

    @GetMapping("{id}")
    public ResponseEntity<EnvironmentDto> getEnvironment(@PathVariable Long id) {
        Environment environment = environmentService.getEnvironment(id);
        return ResponseEntity.ok().body(modelMapper.map(environment, EnvironmentDto.class));
    }

    @PostMapping
    public ResponseEntity<EnvironmentDto> createEnvironment(@RequestBody EnvironmentDto environment) {
        Environment newEnvironment = environmentService
                .createEnvironment(modelMapper.map(environment, Environment.class));
        return ResponseEntity
                .created(URI.create("/data/environments" + newEnvironment.getId()))
                .body(modelMapper.map(newEnvironment, EnvironmentDto.class));
    }

    @PatchMapping("{id}")
    public ResponseEntity<EnvironmentDto> updateEnvironment(@PathVariable Long id, @RequestBody EnvironmentDto environment) {
        Environment updatedEnvironment = environmentService.updateEnvironment(
                id,
                modelMapper.map(environment, Environment.class)
        );
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedEnvironment, EnvironmentDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEnvironment(@PathVariable Long id) {
        environmentService.deleteEnvironment(id);
        return ResponseEntity
                .ok()
                .body("Environment with id " + id + " deleted");
    }
}
