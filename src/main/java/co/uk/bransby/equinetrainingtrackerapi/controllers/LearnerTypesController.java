package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.LearnerTypes;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.LearnerTypesDTO;
import co.uk.bransby.equinetrainingtrackerapi.services.LearnerTypesService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/data/learner-types")
public class LearnerTypesController {

    private final LearnerTypesService learnerTypesService;
    private final ModelMapper modelMapper;

    public LearnerTypesController(LearnerTypesService learnerTypesService) {
        this.learnerTypesService = learnerTypesService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<LearnerTypes> findLearnerTypes(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return learnerTypesService.getLearnerTypes(id)
                .map(learnerType -> new ResponseEntity<>(learnerType, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<LearnerTypes>> findAllLearnerTypes() {
        List<LearnerTypes> allLearnerTypes = learnerTypesService.getAllLearnerTypes();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(allLearnerTypes, resHeaders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LearnerTypes> createLearnerTypes(@RequestBody LearnerTypesDTO newLearnerTypes) {
        LearnerTypes createdLearnerTypes = learnerTypesService.createLearnerTypes(modelMapper.map(newLearnerTypes, LearnerTypes.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(createdLearnerTypes, resHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<LearnerTypes> updateLearnerTypes(@PathVariable Long id, @RequestBody LearnerTypesDTO updatedLearnerTypesValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            LearnerTypes updatedLearnerTypes =  learnerTypesService.updateLearnerTypes(id, modelMapper.map(updatedLearnerTypesValues, LearnerTypes.class));
            return new ResponseEntity<>(updatedLearnerTypes, resHeaders, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<LearnerTypes> deleteLearnerTypes(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return learnerTypesService.getLearnerTypes(id)
                .map(learnerTypes -> {
                    learnerTypesService.deleteLearnerTypes(id);
                    return new ResponseEntity<>(learnerTypes, resHeaders, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }
}
