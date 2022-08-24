package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingMethod;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.TrainingMethodDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingMethodService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/training-methods")
public class TrainingMethodController {

    private final TrainingMethodService methodService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<TrainingMethodDto>> getMethods() {
        List<TrainingMethodDto> allMethods = methodService.listMethods()
                .stream()
                .map(method -> modelMapper.map(method, TrainingMethodDto.class))
                .toList();
        return ResponseEntity
                .ok()
                .body(allMethods);
    }

    @GetMapping("{id}")
    public ResponseEntity<TrainingMethodDto> getMethod(@PathVariable Long id) {
        TrainingMethod method = methodService.listMethod(id);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(method, TrainingMethodDto.class));
    }

    @PostMapping
    public ResponseEntity<TrainingMethodDto> createMethod(@RequestBody TrainingMethod method) {
        TrainingMethod createdMethod = methodService.createMethod(method);
        return ResponseEntity
                .created(URI.create("/data/training-methods/" + createdMethod.getId()))
                .body(modelMapper.map(createdMethod, TrainingMethodDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<TrainingMethodDto> updateMethod(@PathVariable Long id, @RequestBody TrainingMethod updatedMethodValues) {
        TrainingMethod updatedMethod = methodService.updateMethod(id, updatedMethodValues);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedMethod, TrainingMethodDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteMethod(@PathVariable Long id) {
        methodService.deleteMethod(id);
        return ResponseEntity
                .ok()
                .body(id);
    }

}
