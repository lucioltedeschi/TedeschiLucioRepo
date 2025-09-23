
package com.uade.tpo.marketplace.service.Category;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.Category.CategoryDuplicateException; //falta
import com.uade.tpo.marketplace.exceptions.Category.CategoryNotFoundException; //falta

public interface CategoryService {

    public Page<Category> getCategories(PageRequest pageRequest);

    public Optional<Category> getCategoryById(Long categoryId);

    public Category createCategory(String description) throws CategoryDuplicateException;

    public void deleteCategory(Long categoryId) throws CategoryNotFoundException;

    public Category updateCategory(Long categoryId, String description) throws CategoryDuplicateException, CategoryNotFoundException;
}
