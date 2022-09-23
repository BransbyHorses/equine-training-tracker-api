package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.TrainingCategory;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.TrainingCategoryDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.TrainingCategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/training-categories")
public class TrainingCategoryController {

    private final TrainingCategoryService trainingCategoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<TrainingCategoryDto>> getTrainingCategories() {
        List<TrainingCategoryDto> trainingCategories = trainingCategoryService.getTrainingCategories()
                .stream()
                .map(trainingCategory -> modelMapper.map(trainingCategory, TrainingCategoryDto.class))
                .toList();

        return ResponseEntity
                .ok()
                .body(trainingCategories);
    }

    @GetMapping("{id}")
    public ResponseEntity<TrainingCategoryDto> getTrainingMethod(@PathVariable Long id) {
        TrainingCategory trainingCategory = trainingCategoryService.getTrainingCategory(id);
        return ResponseEntity
                .ok()
                .body(modelMapper.map(trainingCategory, TrainingCategoryDto.class));
    }

    @PostMapping
    public ResponseEntity<TrainingCategoryDto> createTrainingCategory(@RequestBody TrainingCategoryDto newTrainingCategory) {
        TrainingCategory trainingCategory = trainingCategoryService.createTrainingCategory(
               modelMapper.map(newTrainingCategory, TrainingCategory.class)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(modelMapper.map(trainingCategory, TrainingCategoryDto.class));
    }


}
