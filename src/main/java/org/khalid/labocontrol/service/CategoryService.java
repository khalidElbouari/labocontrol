package org.khalid.labocontrol.service;

import org.khalid.labocontrol.entities.Category;
import org.khalid.labocontrol.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }
    public Category getCategoryById(long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        return optionalCategory.orElse(null);
    }
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
