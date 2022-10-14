package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.EquineStatus;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.EquineStatusDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.EquineStatusService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/equine-statuses")
public class EquineStatusController {

    private final EquineStatusService equineStatusService;
    private final ModelMapper modelMapper;

    public EquineStatusController(ModelMapper modelMapper, EquineStatusService equineStatusService) {
        this.modelMapper = modelMapper;
        this.equineStatusService = equineStatusService;
    }


    @GetMapping
    public ResponseEntity<List<EquineStatusDto>> getAllCategories() {
        HttpHeaders resHeaders = new HttpHeaders();
        List<EquineStatusDto> categories = equineStatusService.getCategories()
                .stream()
                .map(category -> modelMapper.map(category, EquineStatusDto.class))
                .toList();
        return new ResponseEntity<>(categories, resHeaders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<EquineStatusDto> getCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        EquineStatus category = equineStatusService.getCategory(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .body(modelMapper.map(category, EquineStatusDto.class));
    }

    @PostMapping
    public ResponseEntity<EquineStatusDto> createCategory(@RequestBody EquineStatusDto newCategory) {
        HttpHeaders resHeaders = new HttpHeaders();
        EquineStatus savedCategory = equineStatusService.createCategory(modelMapper.map(newCategory, EquineStatus.class));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(resHeaders)
                .body(modelMapper.map(savedCategory, EquineStatusDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<EquineStatusDto> updateCategory(@PathVariable Long id, @RequestBody EquineStatusDto updatedCategoryValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        EquineStatus savedUpdatedCategory = equineStatusService.updateCategory(id, modelMapper.map(updatedCategoryValues, EquineStatus.class));
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .body(modelMapper.map(savedUpdatedCategory, EquineStatusDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        equineStatusService.deleteCategory(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .build();
    }
}
