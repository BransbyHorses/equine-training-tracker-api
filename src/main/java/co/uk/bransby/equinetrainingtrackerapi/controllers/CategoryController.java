package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.models.dto.CategoryDTO;
import co.uk.bransby.equinetrainingtrackerapi.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/data/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getCategories();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(categories, resHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return categoryService.getCategory(id)
                .map(category -> new ResponseEntity<>(category, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO newCategory) {
        Category savedCategory = categoryService.createCategory(modelMapper.map(newCategory, Category.class));
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(savedCategory, resHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO updatedCategoryValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Category savedUpdatedCategory = categoryService.updateCategory(id, modelMapper.map(updatedCategoryValues, Category.class));
            return new ResponseEntity<>(savedUpdatedCategory, resHeaders, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(category -> {
                    categoryService.deleteCategory(id);
                    return new ResponseEntity<>(category, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
