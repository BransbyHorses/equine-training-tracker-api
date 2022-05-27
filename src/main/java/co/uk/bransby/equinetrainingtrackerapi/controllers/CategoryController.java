package co.uk.bransby.equinetrainingtrackerapi.controllers;

import co.uk.bransby.equinetrainingtrackerapi.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.services.CategoryService;
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

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getCategories();
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(categories, resHeaders, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        HttpHeaders resHeaders = new HttpHeaders();
        return categoryService.getCategory(id)
                .map(category -> new ResponseEntity<>(category, resHeaders, HttpStatus.OK))
                .orElse(new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
        Category savedCategory = categoryService.createCategory(newCategory);
        HttpHeaders resHeaders = new HttpHeaders();
        return new ResponseEntity<>(savedCategory, resHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategoryValues) {
        HttpHeaders resHeaders = new HttpHeaders();
        try {
            Category updatedYard = categoryService.updateCategory(id, updatedCategoryValues);
            return new ResponseEntity<>(updatedYard, resHeaders, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(resHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        return categoryService.getCategory(id)
                .map(category -> {
                    categoryService.deleteCategory(id);
                    return new ResponseEntity<>(category, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
