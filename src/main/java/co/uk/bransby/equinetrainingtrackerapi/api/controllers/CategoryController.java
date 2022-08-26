package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.CategoryDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(ModelMapper modelMapper, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        HttpHeaders resHeaders = new HttpHeaders();
        List<CategoryDto> categories = categoryService.getCategories()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
        return new ResponseEntity<>(categories, resHeaders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        Category category = categoryService.getCategory(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .body(modelMapper.map(category, CategoryDto.class));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto newCategory) {
        HttpHeaders resHeaders = new HttpHeaders();
        Category savedCategory = categoryService.createCategory(modelMapper.map(newCategory, Category.class));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(resHeaders)
                .body(modelMapper.map(savedCategory, CategoryDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto updatedCategoryValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        Category savedUpdatedCategory = categoryService.updateCategory(id, modelMapper.map(updatedCategoryValues, Category.class));
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .body(modelMapper.map(savedUpdatedCategory, CategoryDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        categoryService.deleteCategory(id);
        return ResponseEntity
                .ok()
                .headers(resHeaders)
                .build();
    }
}
