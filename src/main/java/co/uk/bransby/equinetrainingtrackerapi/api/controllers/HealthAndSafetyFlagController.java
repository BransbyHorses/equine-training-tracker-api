package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.HealthAndSafetyFlag;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.HealthAndSafetyFlagDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.HealthAndSafetyFlagService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/health-and-safety-flags")
public class HealthAndSafetyFlagController {

    private final ModelMapper modelMapper;
    private final HealthAndSafetyFlagService healthAndSafetyFlagService;

    public HealthAndSafetyFlagController(ModelMapper modelMapper, HealthAndSafetyFlagService healthAndSafetyFlagService) {
        this.modelMapper = modelMapper;
        this.healthAndSafetyFlagService = healthAndSafetyFlagService;
    }

    @PatchMapping("{id}")
    public ResponseEntity<HealthAndSafetyFlagDto> updateHealthAndSafetyFlag(
            @PathVariable Long id, @RequestBody HealthAndSafetyFlagDto updatedHealthAndSafetyFlag)
    {
        HealthAndSafetyFlag healthAndSafetyFlag = healthAndSafetyFlagService.editHealthAndSafetyFlag(
                id,
                modelMapper.map(updatedHealthAndSafetyFlag, HealthAndSafetyFlag.class)
        );
        return ResponseEntity
                .ok()
                .body(modelMapper.map(healthAndSafetyFlag, HealthAndSafetyFlagDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteHealthAndSafetyFlag(@PathVariable Long id) {
        healthAndSafetyFlagService.deleteHealthAndSafetyFlag(id);
        return ResponseEntity
                .ok()
                .body("Healthy & safety flag with id " + id + " deleted");
    }
}
