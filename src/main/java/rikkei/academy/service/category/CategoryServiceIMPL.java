package rikkei.academy.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.model.entity.Category;
import rikkei.academy.repository.ICategoryRepository;

import java.util.Optional;

@Service
public class CategoryServiceIMPL implements ICategoryService{
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Category findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Category save(Category entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
