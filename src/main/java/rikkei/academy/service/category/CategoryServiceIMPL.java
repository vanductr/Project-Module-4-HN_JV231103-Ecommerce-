package rikkei.academy.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormCategoryRequest;
import rikkei.academy.model.entity.Category;
import rikkei.academy.repository.ICategoryRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryServiceIMPL implements ICategoryService{
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Category findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElseThrow(() -> new NoSuchElementException("Không tồn tại Category có Id này"));
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category save(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài nguyên"));
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    public Category save(FormCategoryRequest formCategoryRequest, Long categoryID) {
        Category category = categoryRepository.findById(categoryID).orElseThrow(() -> new NoSuchElementException("Không tồn tại tài nguyên"));
        category.setCategoryName(formCategoryRequest.getCategoryName());
        category.setDescription(formCategoryRequest.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public Category save(FormCategoryRequest categoryRequest) throws DataExistException {
        if (categoryRepository.existsByCategoryName(categoryRequest.getCategoryName())) {
            throw new DataExistException("Tên Danh mục đã tồn tại", "categoryName");
        }
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setDescription(categoryRequest.getDescription());
        category.setStatus(true);
        return categoryRepository.save(category);
    }
}
