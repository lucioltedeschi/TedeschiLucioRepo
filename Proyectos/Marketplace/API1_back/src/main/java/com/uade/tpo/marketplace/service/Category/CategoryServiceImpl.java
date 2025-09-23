package com.uade.tpo.marketplace.service.Category;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.Role;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.exceptions.Auth.ForbiddenException;
import com.uade.tpo.marketplace.service.User.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.exceptions.Category.CategoryDuplicateException;
import com.uade.tpo.marketplace.exceptions.Category.CategoryNotFoundException;
import com.uade.tpo.marketplace.repository.CategoryRepository;

import static com.uade.tpo.marketplace.entity.Role.ADMIN;


@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAll(pageable);
    }
    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    private final UserService userService;



    @Override
    public Category createCategory(String description) throws CategoryDuplicateException {
        User currentUser = userService.getCurrentUser();
        List<Category> categories = categoryRepository.findByDescription(description);

        if (!ADMIN.equals(currentUser.getRole())) {
            throw new ForbiddenException("Solo un seller puede crear un producto");
        }

        if (categories.isEmpty()) {
            Category cat = categoryRepository.save(new Category(description));
            System.out.println("Guardada categoría con id: " + cat.getId());
            return cat;
        }
        throw new CategoryDuplicateException();
    }

    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()){
            categoryRepository.deleteById(categoryId);
            return;
        }
        throw new CategoryNotFoundException();
    }

    @Override
    public Category updateCategory(Long categoryId, String description) throws CategoryDuplicateException, CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(categoryId);
        Category c;
        if (category.isPresent()) {
            List<Category> categories = categoryRepository.findByDescription(description);
            c = category.get();
            if (categories.isEmpty()) {
                c.setDescription(description);
                return categoryRepository.save(c);
            }
            throw new CategoryDuplicateException();
        }
        throw new CategoryNotFoundException();
    }

}