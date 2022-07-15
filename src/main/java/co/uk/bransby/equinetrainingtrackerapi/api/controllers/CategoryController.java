package co.uk.bransby.equinetrainingtrackerapi.api.controllers;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.api.models.dto.CategoryDto;
import co.uk.bransby.equinetrainingtrackerapi.api.services.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/data/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getCategories()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(categories, resHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return categoryService.getCategory(id)
                .map(category -> new ResponseEntity<>(modelMapper.map(category, CategoryDto.class), resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto newCategory) {
        Category savedCategory = categoryService.createCategory(modelMapper.map(newCategory, Category.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<CategoryDto>(modelMapper.map(savedCategory, CategoryDto.class), resHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto updatedCategoryValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Category savedUpdatedCategory = categoryService.updateCategory(id, modelMapper.map(updatedCategoryValues, Category.class));
            return new ResponseEntity<CategoryDto>(modelMapper.map(savedUpdatedCategory, CategoryDto.class), resHeaders, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(category -> {
                    categoryService.deleteCategory(id);
                    return new ResponseEntity<CategoryDto>(modelMapper.map(category, CategoryDto.class), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
