package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.CategoryDto;
import co.uk.bransby.equinetrainingtrackerapi.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getCategories().stream().map(c -> modelMapper.map(c, CategoryDto.class)).collect(Collectors.toList());
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(categories, resHeaders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return categoryService.getCategory(id)
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .map(categoryDto -> new ResponseEntity<>(categoryDto, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto newCategory) {
        Category savedCategory = categoryService.createCategory(modelMapper.map(newCategory, Category.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(modelMapper.map(savedCategory, CategoryDto.class), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto updatedCategoryValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Category savedCategory = categoryService.updateCategory(id, modelMapper.map(updatedCategoryValues, Category.class));
            return new ResponseEntity<>(modelMapper.map(savedCategory, CategoryDto.class), resHeaders, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            // make error model - status code, url, error message
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(category -> {
                    categoryService.deleteCategory(id);
                    return new ResponseEntity<>(modelMapper.map(category, CategoryDto.class), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
