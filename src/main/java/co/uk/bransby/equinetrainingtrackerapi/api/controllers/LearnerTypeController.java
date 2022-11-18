package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.LearnerType;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.LearnerTypeDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.LearnerTypeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/learner-types")
public class LearnerTypeController {

    private final LearnerTypeService learnerTypeService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<LearnerTypeDto>> getLearnerTypes() {
        List<LearnerTypeDto> learnerTypes = learnerTypeService.getLearnerTypes()
                .stream()
                .map(l -> modelMapper.map(l, LearnerTypeDto.class))
                .toList();
        return ResponseEntity
                .ok()
                .body(learnerTypes);
    }

    @GetMapping("{id}")
    public ResponseEntity<LearnerTypeDto> getLearnerType(@PathVariable Long id) {
        LearnerType learnerType = learnerTypeService.getLearnerType(id);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(learnerType, LearnerTypeDto.class));
    }

    @PostMapping
    public ResponseEntity<LearnerTypeDto> createLearnerType(@RequestBody LearnerTypeDto newLearnerType) {
        LearnerType createdLeanerType = learnerTypeService.createLearnerType(modelMapper.map(newLearnerType, LearnerType.class));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelMapper.map(createdLeanerType, LearnerTypeDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<LearnerTypeDto> updateLearnerType(@PathVariable Long id, @RequestBody LearnerTypeDto newLearnerType) {
        LearnerType updatedLearnerType = learnerTypeService.updateLearnerType(
                id,
                modelMapper.map(newLearnerType, LearnerType.class)
        );
        return ResponseEntity
                .ok()
                .body(modelMapper.map(updatedLearnerType, LearnerTypeDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteLearnerType(@PathVariable Long id) {
        learnerTypeService.deleteLearnerType(id);
        return ResponseEntity.ok().body("Learner type deleted with id: " + id);
    }
}
