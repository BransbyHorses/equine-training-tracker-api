package co.uk.bransby.equinetrainingtrackerapi.api.services;

import co.uk.bransby.equinetrainingtrackerapi.api.models.Category;
import co.uk.bransby.equinetrainingtrackerapi.api.models.Equine;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.CategoryRepository;
import co.uk.bransby.equinetrainingtrackerapi.api.repositories.EquineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EquineRepository equineRepository;

    public CategoryService(CategoryRepository categoryRepository, EquineRepository equineRepository) {
        this.categoryRepository = categoryRepository;
        this.equineRepository = equineRepository;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.saveAndFlush(category);
    }

    public Category updateCategory(Long id, Category updatedCategoryValues) {
        Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(updatedCategoryValues, categoryToUpdate, "id");
        return categoryRepository.saveAndFlush(categoryToUpdate);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
        for(Equine equine : category.getEquines()) {
            category.removeEquine(equine);
            Equine equineDb = equineRepository.getById(equine.getId());
            equineDb.setCategory(null);
            equineRepository.saveAndFlush(equineDb);
        }
        categoryRepository.saveAndFlush(category);
        categoryRepository.deleteById(id);
    }
}
