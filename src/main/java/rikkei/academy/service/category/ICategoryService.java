package rikkei.academy.service.category;

import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormCategoryRequest;
import rikkei.academy.model.entity.Category;
import rikkei.academy.service.IGenericService;

public interface ICategoryService extends IGenericService<Category, Long> {
    boolean existsByCategoryName(String categoryName);

    Category save(FormCategoryRequest formCategoryRequest, Long categoryID);

    Category save(FormCategoryRequest categoryRequest) throws DataExistException;
}
